package com.apigateway.api.controller;

import com.apigateway.api.dto.ApiRequestDTO;
import com.apigateway.api.dto.ApiResponseDTO;
import com.apigateway.api.logic.ApiService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/apis")
public class ApiController {

    private final ApiService service;
    private static final Logger LOG = LoggerFactory.getLogger(ApiController.class);

    public ApiController(ApiService service) {
        this.service = service;
    }

    @GetMapping
    public List<ApiResponseDTO> findAll() {
        LOG.info("ENTRY -- ApiController -- findAll");
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ApiResponseDTO findById(@PathVariable Long id) {
        LOG.info("ENTRY -- ApiController -- findById -- id={}", id);
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponseDTO create(@Valid @RequestBody ApiRequestDTO dto) {
        LOG.info("ENTRY -- ApiController -- create");
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public ApiResponseDTO update(@PathVariable Long id, @Valid @RequestBody ApiRequestDTO dto) {
        LOG.info("ENTRY -- ApiController -- update");
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        LOG.info("ENTRY -- ApiController -- delete");
        service.delete(id);
    }
}