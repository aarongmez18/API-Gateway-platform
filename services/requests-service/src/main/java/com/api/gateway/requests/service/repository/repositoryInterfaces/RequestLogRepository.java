package com.api.gateway.requests.service.repository.repositoryInterfaces;

import com.api.gateway.requests.service.model.RequestLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RequestLogRepository {
    RequestLog save(RequestLog requestLog);
    Page<RequestLog> findFiltered(Long clientId, String apiCode, Integer statusCode, Pageable pageable);
}