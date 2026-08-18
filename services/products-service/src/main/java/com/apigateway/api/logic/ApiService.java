package com.apigateway.api.logic;

import com.apigateway.api.dto.ApiResponseDTO;
import com.apigateway.api.dto.ApiRequestDTO;
import com.apigateway.api.mapper.ApiMapper;
import com.apigateway.api.model.Api;
import com.apigateway.api.repository.repositoryInterfaces.ApiRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiService {

    private final ApiRepository repository;
    private final ApiMapper mapper;

    public ApiService(ApiRepository repository, ApiMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    public List<ApiResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }


    public ApiResponseDTO findById(Long id) {
        Api api = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("API no encontrada: " + id)
                );

        return mapper.toDto(api);
    }


    public ApiResponseDTO create(ApiRequestDTO dto) {
        Api api = mapper.toEntity(dto);
        Api saved = repository.save(api);
        return mapper.toDto(saved);
    }


    public ApiResponseDTO update(Long id, ApiRequestDTO dto) {
        Api api = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("API no encontrada: " + id)
                );

        mapper.updateEntity(dto, api);
        Api updated = repository.save(api);
        return mapper.toDto(updated);
    }


    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException(
                    "API no encontrada: " + id
            );
        }

        repository.deleteById(id);
    }
}