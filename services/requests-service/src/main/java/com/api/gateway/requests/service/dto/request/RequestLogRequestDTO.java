package com.api.gateway.requests.service.dto.request;

import jakarta.validation.constraints.*;
import java.time.OffsetDateTime;

public record RequestLogRequestDTO(Long clientId, String clientName, @NotBlank String apiCode, @NotBlank String endpoint, @NotBlank String method, @NotNull @Min(100) @Max(599) Integer statusCode, @NotNull @Min(0) Long durationMs, @NotNull OffsetDateTime requestedAt) {}