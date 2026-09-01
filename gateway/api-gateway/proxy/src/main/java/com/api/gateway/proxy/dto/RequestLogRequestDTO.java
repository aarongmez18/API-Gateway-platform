package com.api.gateway.proxy.dto;

import java.time.OffsetDateTime;

public record RequestLogRequestDTO(Long clientId, String clientName, String apiCode, String endpoint, String method, Integer statusCode, Long durationMs, OffsetDateTime requestedAt) {}