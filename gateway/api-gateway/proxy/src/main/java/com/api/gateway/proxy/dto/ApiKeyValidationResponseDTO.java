package com.api.gateway.proxy.dto;

public record ApiKeyValidationResponseDTO(Long clientId, String clientName, Integer rateLimitPerMinute) {}