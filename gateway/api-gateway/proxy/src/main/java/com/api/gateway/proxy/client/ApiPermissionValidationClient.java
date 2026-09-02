package com.api.gateway.proxy.client;

import com.api.gateway.proxy.dto.PermissionCheckResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Value;


@Component
public class ApiPermissionValidationClient {

    private final WebClient webClient;

    public ApiPermissionValidationClient(WebClient.Builder builder, @Value("${services.users.url:http://localhost:8082}") String usersServiceUrl) { this.webClient = builder.baseUrl(usersServiceUrl).build(); }

    public Mono<PermissionCheckResponseDTO> check(Long clientId, String apiCode) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/users-management/permissions/check")
                        .queryParam("clientId", clientId)
                        .queryParam("apiCode", apiCode)
                        .build())
                .retrieve()
                .bodyToMono(PermissionCheckResponseDTO.class);
    }
}