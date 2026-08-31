package com.apigateway.users.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ClientResponseDTO(
        Long id,
        String name,
        Boolean active,
        Integer rateLimitPerMinute,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDateTime createdAt
) {}