package com.api.gateway.requests.service.logic;

import com.api.gateway.requests.service.dto.request.RequestLogRequestDTO;
import com.api.gateway.requests.service.dto.response.RequestLogResponseDTO;
import com.api.gateway.requests.service.mapper.RequestLogMapper;
import com.api.gateway.requests.service.model.RequestLog;
import com.api.gateway.requests.service.repository.repositoryInterfaces.RequestLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.api.gateway.requests.service.dto.response.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class RequestLogService {

    private static final Logger LOG = LoggerFactory.getLogger(RequestLogService.class);

    private final RequestLogRepository repository;
    private final RequestLogMapper mapper;

    public RequestLogService(RequestLogRepository repository, RequestLogMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public void create(RequestLogRequestDTO dto) {
        LOG.info("ENTRY -- RequestLogService -- create -- clientId={} -- apiCode={} -- method={} -- status={}", dto.clientId(), dto.apiCode(), dto.method(), dto.statusCode());

        RequestLog requestLog = mapper.toEntity(dto);
        RequestLog saved = repository.save(requestLog);

        LOG.info("OK -- RequestLogService -- create -- id={}", saved.getId());
    }

    public Page<RequestLogResponseDTO> find(Long clientId, String apiCode, Integer statusCode, int page, int size) {
        LOG.info("ENTRY -- RequestLogService -- find -- clientId={} -- apiCode={} -- statusCode={} -- page={} -- size={}", clientId, apiCode, statusCode, page, size);

        Pageable pageable = PageRequest.of(page, Math.min(Math.max(size, 1), 100));
        Page<RequestLogResponseDTO> result = repository.findFiltered(clientId, normalize(apiCode), statusCode, pageable).map(mapper::toDto);

        LOG.info("OK -- RequestLogService -- find -- totalElements={}", result.getTotalElements());

        return result;
    }

    public RequestDashboardResponseDTO dashboard() {
        LOG.info("ENTRY -- RequestLogService -- dashboard");

        ZoneId zone = ZoneId.systemDefault();
        LocalDate today = LocalDate.now(zone);
        OffsetDateTime from = today.atStartOfDay(zone).toOffsetDateTime();
        OffsetDateTime to = today.plusDays(1).atStartOfDay(zone).toOffsetDateTime();

        Long totalRequests = repository.countAll();
        Long todayRequests = repository.countBetween(from, to);
        Long errors = repository.countErrorsBetween(from, to);
        Double average = repository.averageDurationBetween(from, to);

        List<HourlyMetricResponseDTO> requestsByHour = completeHours(repository.countByHour(from, to));
        List<HourlyMetricResponseDTO> errorsByHour = completeHours(repository.countErrorsByHour(from, to));

        List<ApiUsageResponseDTO> topApis = repository.topApis(from, to, 5).stream().map(row -> new ApiUsageResponseDTO((String) row[0], ((Number) row[1]).longValue())).toList();
        List<ClientUsageResponseDTO> topClients = repository.topClients(from, to, 5).stream().map(row -> new ClientUsageResponseDTO(((Number) row[0]).longValue(), (String) row[1], ((Number) row[2]).longValue())).toList();

        RequestDashboardResponseDTO result = new RequestDashboardResponseDTO(totalRequests, todayRequests, errors, average == null ? 0L : Math.round(average), requestsByHour, errorsByHour, topApis, topClients);

        LOG.info("OK -- RequestLogService -- dashboard -- totalRequests={} -- todayRequests={} -- errors={}", totalRequests, todayRequests, errors);
        return result;
    }

    private List<HourlyMetricResponseDTO> completeHours(List<Object[]> rows) {
        List<HourlyMetricResponseDTO> result = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            int currentHour = hour;
            long count = rows.stream().filter(row -> ((Number) row[0]).intValue() == currentHour).map(row -> ((Number) row[1]).longValue()).findFirst().orElse(0L);
            result.add(new HourlyMetricResponseDTO(hour, count));
        }
        return result;
    }

    private String normalize(String value) { return value == null || value.isBlank() ? null : value; }
}