package com.apigateway.users.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClientRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String name,

        Boolean active,

        @NotNull(message = "El límite de peticiones es obligatorio")
        @Min(value = 1, message = "El límite debe ser mayor que 0")
        Integer rateLimitPerMinute
) {}