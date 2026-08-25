package com.apigateway.api.repository.repositoryInterfaces;


import com.apigateway.api.model.Api;

import java.util.List;
import java.util.Optional;

public interface ApiRepository {

    List<Api> findAll();

    Optional<Api> findById(Long id);

    Api save(Api api);

    void deleteById(Long id);

    boolean existsByCode(String code);

    boolean existsById(Long id);
}