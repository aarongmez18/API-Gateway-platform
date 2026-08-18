package com.apigateway.api.controller;

import com.apigateway.api.dto.ApiRequestDTO;
import com.apigateway.api.dto.ApiResponseDTO;
import com.apigateway.api.logic.ApiService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apis")
public class ApiController {

    private final ApiService service;

    public ApiController(ApiService service) {
        this.service = service;
    }

    @GetMapping
    public List<ApiResponseDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ApiResponseDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponseDTO create(@Valid @RequestBody ApiRequestDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public ApiResponseDTO update(@PathVariable Long id, @Valid @RequestBody ApiRequestDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}