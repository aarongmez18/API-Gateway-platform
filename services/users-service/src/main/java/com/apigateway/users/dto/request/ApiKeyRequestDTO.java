package com.apigateway.users.dto.request;

import jakarta.validation.constraints.NotNull;

public record ApiKeyRequestDTO(

        @NotNull(message = "El cliente es obligatorio")
        Long clientId,

        Boolean active

) {
}