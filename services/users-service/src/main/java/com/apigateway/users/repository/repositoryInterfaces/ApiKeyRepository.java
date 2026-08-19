package com.apigateway.users.repository.repositoryInterfaces;


import com.apigateway.users.model.ApiKey;

import java.util.List;
import java.util.Optional;

public interface ApiKeyRepository {
    List<ApiKey> findAll();
    Optional<ApiKey> findById(Long id);
    List<ApiKey> findByClientId(Long clientId);
    ApiKey save(ApiKey apiKey);
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByClientId(Long clientId);
}