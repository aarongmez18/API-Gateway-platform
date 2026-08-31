package com.api.gateway.requests.service.mapper;


import com.api.gateway.requests.service.dto.request.RequestLogRequestDTO;
import com.api.gateway.requests.service.dto.response.RequestLogResponseDTO;
import com.api.gateway.requests.service.model.RequestLog;
import org.springframework.stereotype.Component;

@Component
public class RequestLogMapper {

    public RequestLog toEntity(RequestLogRequestDTO dto) {
        return new RequestLog(dto.clientId(), dto.clientName(), dto.apiCode(), dto.endpoint(), dto.method(), dto.statusCode(), dto.durationMs(), dto.requestedAt());
    }

    public RequestLogResponseDTO toDto(RequestLog requestLog) {
        return new RequestLogResponseDTO(requestLog.getId(), requestLog.getClientId(), requestLog.getClientName(), requestLog.getApiCode(), requestLog.getEndpoint(), requestLog.getMethod(), requestLog.getStatusCode(), requestLog.getDurationMs(), requestLog.getRequestedAt());
    }
}