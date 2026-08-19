package com.apigateway.users.dto.response;

import java.time.LocalDateTime;

public record ClientResponseDTO(
        Long id,
        String name,
        Boolean active,
        LocalDateTime createdAt
) {
}