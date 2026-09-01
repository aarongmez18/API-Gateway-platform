package com.api.gateway.requests.service.dto.response;

import java.util.List;

public record RequestDashboardResponseDTO(Long totalRequests, Long todayRequests, Long errors, Long averageResponseTimeMs, List<HourlyMetricResponseDTO> requestsByHour, List<HourlyMetricResponseDTO> errorsByHour, List<ApiUsageResponseDTO> topApis, List<ClientUsageResponseDTO> topClients) {}