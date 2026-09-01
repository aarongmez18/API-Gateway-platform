package com.api.gateway.requests.service.controller;

import com.api.gateway.requests.service.dto.request.RequestLogRequestDTO;
import com.api.gateway.requests.service.dto.response.RequestDashboardResponseDTO;
import com.api.gateway.requests.service.dto.response.RequestLogResponseDTO;
import com.api.gateway.requests.service.logic.RequestLogService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class RequestLogController {

    private static final Logger LOG = LoggerFactory.getLogger(RequestLogController.class);
    private final RequestLogService service;

    public RequestLogController(RequestLogService service) { this.service = service; }

    @PostMapping("/internal/request-logs")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody RequestLogRequestDTO dto) {
        LOG.info("ENTRY -- RequestLogController -- create -- apiCode={} -- method={}", dto.apiCode(), dto.method());
        service.create(dto);
    }

    @GetMapping("/request-logs")
    public Page<RequestLogResponseDTO> find(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) String apiCode,
            @RequestParam(required = false) Integer statusCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        LOG.info("ENTRY -- RequestLogController -- find -- clientId={} -- apiCode={} -- statusCode={}", clientId, apiCode, statusCode);

        return service.find(clientId, apiCode, statusCode, page, size);
    }

    @GetMapping("/request-logs/dashboard")
    public RequestDashboardResponseDTO dashboard() {
        LOG.info("ENTRY -- RequestLogController -- dashboard");
        return service.dashboard();
    }
}