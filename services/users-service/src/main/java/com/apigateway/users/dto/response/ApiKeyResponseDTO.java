package com.apigateway.users.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ApiKeyResponseDTO(
        Long id,
        Long clientId,
        String clientName,
        Boolean active,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDateTime createdAt
) {
}