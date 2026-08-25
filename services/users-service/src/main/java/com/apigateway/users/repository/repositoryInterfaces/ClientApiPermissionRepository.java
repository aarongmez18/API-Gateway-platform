package com.apigateway.users.repository.repositoryInterfaces;

import com.apigateway.users.model.ClientApiPermission;

import java.util.List;

public interface ClientApiPermissionRepository {

    ClientApiPermission save(ClientApiPermission permission);

    List<ClientApiPermission> findByClientId(Long clientId);

    boolean existsByClientIdAndApiCode(Long clientId, String apiCode);

    void deleteByClientIdAndApiCode(Long clientId, String apiCode);
}