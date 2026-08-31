package com.api.gateway.proxy.filter;

import com.api.gateway.proxy.client.ApiPermissionValidationClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ApiPermissionGatewayFilterFactory extends AbstractGatewayFilterFactory<ApiPermissionGatewayFilterFactory.Config> {

    private final ApiPermissionValidationClient permissionClient;

    public ApiPermissionGatewayFilterFactory(ApiPermissionValidationClient permissionClient) {
        super(Config.class);
        this.permissionClient = permissionClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            Long clientId = exchange.getAttribute(ApiKeyAuthenticationGatewayFilterFactory.AUTHENTICATED_CLIENT_ID_ATTR);

            if (clientId == null) return completeWithStatus(exchange, HttpStatus.UNAUTHORIZED);
            if (config.getApiCode() == null || config.getApiCode().isBlank()) return completeWithStatus(exchange, HttpStatus.INTERNAL_SERVER_ERROR);

            return permissionClient.check(clientId, config.getApiCode())
                    .flatMap(permission -> {
                        if (!permission.allowed()) return completeWithStatus(exchange, HttpStatus.FORBIDDEN);
                        return chain.filter(exchange);
                    })
                    .onErrorResume(WebClientRequestException.class, ex -> completeWithStatus(exchange, HttpStatus.SERVICE_UNAVAILABLE));
        };
    }

    private Mono<Void> completeWithStatus(ServerWebExchange exchange, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
        private String apiCode;

        public String getApiCode() { return apiCode; }

        public void setApiCode(String apiCode) { this.apiCode = apiCode; }
    }
}