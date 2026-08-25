package com.api.gateway.proxy.filter;

import com.api.gateway.proxy.client.ApiKeyValidationClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ApiKeyAuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<ApiKeyAuthenticationGatewayFilterFactory.Config> {

    private static final String API_KEY_HEADER = "X-API-Key";
    private static final String CLIENT_ID_HEADER = "X-Client-ID";
    private static final String CLIENT_NAME_HEADER = "X-Client-Name";

    public static final String AUTHENTICATED_CLIENT_ID_ATTR = "authenticatedClientId";

    private final ApiKeyValidationClient validationClient;

    public ApiKeyAuthenticationGatewayFilterFactory(ApiKeyValidationClient validationClient) {
        super(Config.class);
        this.validationClient = validationClient;
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {
            String apiKey = exchange.getRequest()
                    .getHeaders()
                    .getFirst(API_KEY_HEADER);

            if (apiKey == null || apiKey.isBlank()) {
                return completeWithStatus(exchange, HttpStatus.UNAUTHORIZED);
            }

            return validationClient.validate(apiKey)
                    .flatMap(client -> {
                        ServerHttpRequest request = exchange.getRequest()
                                .mutate()
                                .headers(headers -> {
                                    headers.remove(CLIENT_ID_HEADER);
                                    headers.remove(CLIENT_NAME_HEADER);
                                    headers.set(CLIENT_ID_HEADER, client.clientId().toString());
                                    headers.set(CLIENT_NAME_HEADER, client.clientName());
                                })
                                .build();

                        ServerWebExchange authenticatedExchange = exchange.mutate().request(request).build();
                        authenticatedExchange.getAttributes().put(AUTHENTICATED_CLIENT_ID_ATTR, client.clientId());
                        return chain.filter(authenticatedExchange);
                    }).onErrorResume(WebClientResponseException.Unauthorized.class,
                            ex -> completeWithStatus(exchange, HttpStatus.UNAUTHORIZED)
                    )
                    .onErrorResume(WebClientRequestException.class,
                            ex -> completeWithStatus(exchange, HttpStatus.SERVICE_UNAVAILABLE)
                    );
        };
    }

    private Mono<Void> completeWithStatus(ServerWebExchange exchange, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        return exchange.getResponse().setComplete();
    }

    public static class Config {}
}