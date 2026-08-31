package com.api.gateway.requests.service.dto.response;

import java.time.LocalDateTime;

public record ErrorResponseDTO(LocalDateTime timestamp, int status, String message) {}