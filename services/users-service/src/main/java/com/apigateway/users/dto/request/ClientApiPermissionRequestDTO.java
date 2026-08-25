package com.apigateway.users.dto.request;

import jakarta.validation.constraints.NotNull;

public record ClientApiPermissionRequestDTO(
        @NotNull Long clientId,
        @NotNull String apiCode
) {}