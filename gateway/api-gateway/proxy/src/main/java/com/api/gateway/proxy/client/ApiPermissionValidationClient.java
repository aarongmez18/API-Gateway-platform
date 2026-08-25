package com.api.gateway.proxy.client;

import com.api.gateway.proxy.dto.PermissionCheckResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ApiPermissionValidationClient {

    private final WebClient webClient;

    public ApiPermissionValidationClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:8082")
                .build();
    }

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