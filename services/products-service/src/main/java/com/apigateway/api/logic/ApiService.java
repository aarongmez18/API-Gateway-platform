package com.apigateway.api.logic;

import com.apigateway.api.dto.ApiResponseDTO;
import com.apigateway.api.dto.ApiRequestDTO;
import com.apigateway.api.exception.ApiNotFoundException;
import com.apigateway.api.mapper.ApiMapper;
import com.apigateway.api.model.Api;
import com.apigateway.api.repository.repositoryInterfaces.ApiRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiService {

    private final ApiMapper mapper;
    private final ApiRepository repository;

    private static final Logger LOG = LoggerFactory.getLogger(ApiService.class);

    public ApiService(ApiRepository repository, ApiMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    public List<ApiResponseDTO> findAll() {
        LOG.info("ENTRY -- ApiService -- findAll");
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }


    public ApiResponseDTO findById(Long id) {
        LOG.info("ENTRY -- ApiService -- findById");
        Api api = repository.findById(id).orElseThrow(() -> new ApiNotFoundException(id));
        LOG.info("OK -- ApiService -- findById");
        return mapper.toDto(api);
    }


    public ApiResponseDTO create(ApiRequestDTO dto) {
        LOG.info("ENTRY -- ApiService -- create");
        Api api = mapper.toEntity(dto);
        api.setCode(generateUniqueCode(dto.getName()));
        Api saved = repository.save(api);
        LOG.info("OK -- ApiService -- create -- code={}", saved.getCode());
        return mapper.toDto(saved);
    }


    public ApiResponseDTO update(Long id, ApiRequestDTO dto) {
        LOG.info("ENTRY -- ApiService -- update");
        Api api = repository.findById(id).orElseThrow(() -> new ApiNotFoundException(id));
        LOG.info("OK -- ApiService -- update");

        mapper.updateEntity(dto, api);
        Api updated = repository.save(api);
        LOG.info("SAVING OK -- ApiService -- update");
        return mapper.toDto(updated);
    }


    public void delete(Long id) {
        LOG.info("ENTRY -- ApiService -- delete");

        if (!repository.existsById(id)) {
            LOG.warn("ID no found -- ApiService -- delete -- id={}", id);
            throw new ApiNotFoundException(id);
        }

        repository.deleteById(id);

        LOG.info("OK -- ApiService -- delete -- id={}", id);
    }

    private String generateUniqueCode(String name) {
        String baseCode = generateCode(name);
        String code = baseCode;
        int suffix = 2;

        while (repository.existsByCode(code)) {
            code = baseCode + "-" + suffix++;
        }

        return code;
    }

    private String generateCode(String name) {
        String normalized = java.text.Normalizer.normalize(name, java.text.Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return normalized.trim().toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
    }
}