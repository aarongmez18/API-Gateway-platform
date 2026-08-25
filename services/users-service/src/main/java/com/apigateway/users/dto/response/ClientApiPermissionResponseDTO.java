package com.apigateway.users.dto.response;

import java.time.LocalDateTime;

public record ClientApiPermissionResponseDTO(
        Long id,
        Long clientId,
        String apiCode,
        LocalDateTime createdAt
) {}