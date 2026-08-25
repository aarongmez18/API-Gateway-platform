package com.apigateway.users.logic;

import com.apigateway.users.dto.request.ClientApiPermissionRequestDTO;
import com.apigateway.users.dto.response.ClientApiPermissionResponseDTO;
import com.apigateway.users.exception.ClientNotFoundException;
import com.apigateway.users.exception.PermissionAlreadyExistsException;
import com.apigateway.users.model.Client;
import com.apigateway.users.model.ClientApiPermission;
import com.apigateway.users.repository.repositoryInterfaces.ClientApiPermissionRepository;
import com.apigateway.users.repository.repositoryInterfaces.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientApiPermissionService {

    private final ClientApiPermissionRepository permissionRepository;
    private final ClientRepository clientRepository;

    public ClientApiPermissionService(ClientApiPermissionRepository permissionRepository, ClientRepository clientRepository) {
        this.permissionRepository = permissionRepository;
        this.clientRepository = clientRepository;
    }

    public ClientApiPermissionResponseDTO grant(ClientApiPermissionRequestDTO dto) {
        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() -> new ClientNotFoundException(dto.clientId()));

        if (permissionRepository.existsByClientIdAndApiCode(dto.clientId(), dto.apiCode())) {
            throw new PermissionAlreadyExistsException(dto.clientId(), dto.apiCode());
        }

        ClientApiPermission permission =
                new ClientApiPermission(client, dto.apiCode());

        return toResponse(permissionRepository.save(permission));
    }

    public List<ClientApiPermissionResponseDTO> findByClientId(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new ClientNotFoundException(clientId);
        }

        return permissionRepository.findByClientId(clientId).stream().map(this::toResponse).toList();
    }

    public boolean hasPermission(Long clientId, String apiCode) {
        return permissionRepository.existsByClientIdAndApiCode(clientId, apiCode);
    }

    public void revoke(Long clientId, String apiCode) {
        permissionRepository.deleteByClientIdAndApiCode(clientId, apiCode);
    }

    private ClientApiPermissionResponseDTO toResponse(ClientApiPermission permission) {
        return new ClientApiPermissionResponseDTO(
                permission.getId(),
                permission.getClient().getId(),
                permission.getApiCode(),
                permission.getCreatedAt()
        );
    }
}