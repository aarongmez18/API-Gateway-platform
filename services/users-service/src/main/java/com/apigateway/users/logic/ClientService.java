package com.apigateway.users.logic;

import com.apigateway.users.dto.request.ClientRequestDTO;
import com.apigateway.users.dto.response.ClientResponseDTO;
import com.apigateway.users.exception.ClientHasApiKeysException;
import com.apigateway.users.exception.ClientNotFoundException;
import com.apigateway.users.mapper.ClientMapper;
import com.apigateway.users.model.Client;
import com.apigateway.users.repository.repositoryInterfaces.ApiKeyRepository;
import com.apigateway.users.repository.repositoryInterfaces.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private static final Logger LOG = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository repository;
    private final ApiKeyRepository apiKeyRepository;
    private final ClientMapper mapper;

    public ClientService(
            ClientRepository repository,
            ApiKeyRepository apiKeyRepository,
            ClientMapper mapper) {

        this.repository = repository;
        this.apiKeyRepository = apiKeyRepository;
        this.mapper = mapper;
    }

    public List<ClientResponseDTO> findAll() {
        LOG.info("ENTRY -- ClientService -- findAll");

        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public ClientResponseDTO findById(Long id) {
        LOG.info("ENTRY -- ClientService -- findById -- id={}", id);

        Client client = repository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));

        LOG.info("OK -- ClientService -- findById -- id={}", id);

        return mapper.toDto(client);
    }

    public ClientResponseDTO create(ClientRequestDTO dto) {
        LOG.info("ENTRY -- ClientService -- create");
        Client client = mapper.toEntity(dto);
        Client saved = repository.save(client);
        LOG.info("OK -- ClientService -- create -- id={}", saved.getId());
        return mapper.toDto(saved);
    }

    public ClientResponseDTO update(Long id, ClientRequestDTO dto) {
        LOG.info("ENTRY -- ClientService -- update -- id={}", id);

        Client client = repository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));

        mapper.updateEntity(dto, client);

        Client updated = repository.save(client);

        LOG.info("OK -- ClientService -- update -- id={}", id);

        return mapper.toDto(updated);
    }

    public void delete(Long id) {

        LOG.info("ENTRY -- ClientService -- delete -- id={}", id);

        if (!repository.existsById(id)) {
            LOG.warn("Cliente no encontrado -- ClientService -- delete -- id={}", id);
            throw new ClientNotFoundException(id);
        }

        if (apiKeyRepository.existsByClientId(id)) {
            LOG.warn("Cliente con API Keys asociadas -- ClientService -- delete -- id={}", id);
            throw new ClientHasApiKeysException(id);
        }

        repository.deleteById(id);

        LOG.info("OK -- ClientService -- delete -- id={}", id);
    }
}