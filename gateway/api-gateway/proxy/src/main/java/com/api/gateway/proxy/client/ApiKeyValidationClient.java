package com.api.gateway.proxy.client;

import com.api.gateway.proxy.dto.ApiKeyValidationResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ApiKeyValidationClient {

    private final WebClient webClient;

    public ApiKeyValidationClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:8082")
                .build();
    }

    public Mono<ApiKeyValidationResponseDTO> validate(String apiKey) {

        return webClient.get()
                .uri("/users-management/api-keys/validate")
                .header("X-API-Key", apiKey)
                .retrieve()
                .bodyToMono(ApiKeyValidationResponseDTO.class);
    }
}