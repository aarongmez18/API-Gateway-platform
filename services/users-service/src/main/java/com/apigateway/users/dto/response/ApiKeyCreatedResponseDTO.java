package com.apigateway.users.dto.response;

import java.time.LocalDateTime;

public record ApiKeyCreatedResponseDTO(
        Long id,
        Long clientId,
        String clientName,
        Boolean active,
        LocalDateTime createdAt,
        String apiKey
) {
}