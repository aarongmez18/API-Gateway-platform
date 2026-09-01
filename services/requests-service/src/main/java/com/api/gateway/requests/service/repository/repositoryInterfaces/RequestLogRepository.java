package com.api.gateway.requests.service.repository.repositoryInterfaces;

import com.api.gateway.requests.service.model.RequestLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;

public interface RequestLogRepository {
    RequestLog save(RequestLog requestLog);
    Page<RequestLog> findFiltered(Long clientId, String apiCode, Integer statusCode, Pageable pageable);
    Long countAll();
    Long countBetween(OffsetDateTime from, OffsetDateTime to);
    Long countErrorsBetween(OffsetDateTime from, OffsetDateTime to);
    Double averageDurationBetween(OffsetDateTime from, OffsetDateTime to);
    List<Object[]> countByHour(OffsetDateTime from, OffsetDateTime to);
    List<Object[]> countErrorsByHour(OffsetDateTime from, OffsetDateTime to);
    List<Object[]> topApis(OffsetDateTime from, OffsetDateTime to, int limit);
    List<Object[]> topClients(OffsetDateTime from, OffsetDateTime to, int limit);
}