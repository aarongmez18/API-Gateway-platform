package com.apigateway.users.controller;


import com.apigateway.users.dto.request.ApiKeyRequestDTO;
import com.apigateway.users.dto.response.ApiKeyResponseDTO;
import com.apigateway.users.logic.ApiKeyService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-keys")
public class ApiKeyController {

    private static final Logger LOG = LoggerFactory.getLogger(ApiKeyController.class);
    private final ApiKeyService service;

    public ApiKeyController(ApiKeyService service) {
        this.service = service;
    }

    @GetMapping
    public List<ApiKeyResponseDTO> findAll() {
        LOG.info("ENTRY -- ApiKeyController -- findAll");
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ApiKeyResponseDTO findById(@PathVariable Long id) {
        LOG.info("ENTRY -- ApiKeyController -- findById -- id={}", id);
        return service.findById(id);
    }

    @GetMapping("/client/{clientId}")
    public List<ApiKeyResponseDTO> findByClientId(@PathVariable Long clientId) {
        LOG.info("ENTRY -- ApiKeyController -- findByClientId -- clientId={}", clientId);
        return service.findByClientId(clientId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiKeyResponseDTO create(@Valid @RequestBody ApiKeyRequestDTO dto) {
        LOG.info("ENTRY -- ApiKeyController -- create -- clientId={}", dto.clientId());
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public ApiKeyResponseDTO update(@PathVariable Long id, @Valid @RequestBody ApiKeyRequestDTO dto) {
        LOG.info("ENTRY -- ApiKeyController -- update -- id={}", id);
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        LOG.info("ENTRY -- ApiKeyController -- delete -- id={}", id);
        service.delete(id);
    }
}