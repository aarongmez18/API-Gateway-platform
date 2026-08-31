package com.api.gateway.requests.service.dto.response;

import java.time.OffsetDateTime;

public record RequestLogResponseDTO(Long id, Long clientId, String clientName, String apiCode, String endpoint, String method, Integer statusCode, Long durationMs, OffsetDateTime requestedAt) {}