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

    private String normalize(String value) { return value == null || value.isBlank() ? null : value; }
}