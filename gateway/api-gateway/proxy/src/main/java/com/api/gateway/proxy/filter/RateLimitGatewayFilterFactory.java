package com.api.gateway.proxy.filter;

import com.api.gateway.proxy.service.RateLimitService;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RateLimitGatewayFilterFactory extends AbstractGatewayFilterFactory<RateLimitGatewayFilterFactory.Config> {

    private static final String RATE_LIMIT_LIMIT_HEADER = "X-RateLimit-Limit";
    private static final String RATE_LIMIT_REMAINING_HEADER = "X-RateLimit-Remaining";
    private static final String RATE_LIMIT_RESET_HEADER = "X-RateLimit-Reset";

    private final RateLimitService rateLimitService;

    public RateLimitGatewayFilterFactory(RateLimitService rateLimitService) {
        super(Config.class);
        this.rateLimitService = rateLimitService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            Long clientId = exchange.getAttribute(ApiKeyAuthenticationGatewayFilterFactory.AUTHENTICATED_CLIENT_ID_ATTR);
            Integer limit = exchange.getAttribute(ApiKeyAuthenticationGatewayFilterFactory.AUTHENTICATED_CLIENT_RATE_LIMIT_ATTR);

            if (clientId == null) return completeWithStatus(exchange, HttpStatus.UNAUTHORIZED);
            if (limit == null || limit < 1) return completeWithStatus(exchange, HttpStatus.INTERNAL_SERVER_ERROR);

            return rateLimitService.consume(clientId, limit)
                    .flatMap(result -> {
                        exchange.getResponse().getHeaders().set(RATE_LIMIT_LIMIT_HEADER, String.valueOf(result.limit()));
                        exchange.getResponse().getHeaders().set(RATE_LIMIT_REMAINING_HEADER, String.valueOf(result.remaining()));
                        exchange.getResponse().getHeaders().set(RATE_LIMIT_RESET_HEADER, String.valueOf(result.retryAfterSeconds()));

                        if (!result.allowed()) {
                            exchange.getResponse().getHeaders().set(HttpHeaders.RETRY_AFTER, String.valueOf(result.retryAfterSeconds()));
                            return completeWithStatus(exchange, HttpStatus.TOO_MANY_REQUESTS);
                        }

                        return chain.filter(exchange);
                    })
                    .onErrorResume(DataAccessException.class, ex -> completeWithStatus(exchange, HttpStatus.SERVICE_UNAVAILABLE));
        };
    }

    private Mono<Void> completeWithStatus(ServerWebExchange exchange, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    public static class Config {}
}