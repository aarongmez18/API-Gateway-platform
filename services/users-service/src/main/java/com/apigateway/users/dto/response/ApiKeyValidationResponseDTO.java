package com.apigateway.users.dto.response;

public record ApiKeyValidationResponseDTO(Long clientId, String clientName, Integer rateLimitPerMinute) {}