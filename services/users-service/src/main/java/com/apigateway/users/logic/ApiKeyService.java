package com.apigateway.users.logic;

import com.apigateway.users.dto.request.ApiKeyRequestDTO;
import com.apigateway.users.dto.response.ApiKeyResponseDTO;
import com.apigateway.users.exception.ApiKeyNotFoundException;
import com.apigateway.users.exception.ClientNotFoundException;
import com.apigateway.users.model.ApiKey;
import com.apigateway.users.model.Client;
import com.apigateway.users.repository.repositoryInterfaces.ApiKeyRepository;
import com.apigateway.users.repository.repositoryInterfaces.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

@Service
public class ApiKeyService {

    private static final Logger LOG = LoggerFactory.getLogger(ApiKeyService.class);

    private final ApiKeyRepository apiKeyRepository;
    private final ClientRepository clientRepository;
    private final ApiKeyMapper mapper;

    public ApiKeyService(
            ApiKeyRepository apiKeyRepository,
            ClientRepository clientRepository,
            ApiKeyMapper mapper) {

        this.apiKeyRepository = apiKeyRepository;
        this.clientRepository = clientRepository;
        this.mapper = mapper;
    }

    public List<ApiKeyResponseDTO> findAll() {

        LOG.info("ENTRY -- ApiKeyService -- findAll");

        return apiKeyRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public ApiKeyResponseDTO findById(Long id) {
        LOG.info("ENTRY -- ApiKeyService -- findById -- id={}", id);
        ApiKey apiKey = apiKeyRepository.findById(id).orElseThrow(() -> new ApiKeyNotFoundException(id));
        LOG.info("OK -- ApiKeyService -- findById -- id={}", id);
        return mapper.toDto(apiKey);
    }

    public List<ApiKeyResponseDTO> findByClientId(Long clientId) {

        LOG.info("ENTRY -- ApiKeyService -- findByClientId -- clientId={}", clientId);

        if (!clientRepository.existsById(clientId)) {
            LOG.warn("Cliente no encontrado -- ApiKeyService -- findByClientId -- clientId={}", clientId);
            throw new ClientNotFoundException(clientId);
        }

        List<ApiKeyResponseDTO> apiKeys = apiKeyRepository
                .findByClientId(clientId)
                .stream()
                .map(mapper::toDto)
                .toList();

        LOG.info(
                "OK -- ApiKeyService -- findByClientId -- clientId={} -- total={}",
                clientId,
                apiKeys.size()
        );

        return apiKeys;
    }

    public ApiKeyResponseDTO create(ApiKeyRequestDTO dto) {

        LOG.info(
                "ENTRY -- ApiKeyService -- create -- clientId={}",
                dto.clientId()
        );

        Client client = clientRepository.findById(dto.clientId())
                .orElseThrow(() ->
                        new ClientNotFoundException(dto.clientId())
                );

        String rawApiKey = generateApiKey();
        String keyHash = hashApiKey(rawApiKey);

        ApiKey apiKey = new ApiKey();

        apiKey.setClient(client);
        apiKey.setKeyHash(keyHash);

        if (dto.active() != null) {
            apiKey.setActive(dto.active());
        }

        ApiKey saved = apiKeyRepository.save(apiKey);

        LOG.info(
                "OK -- ApiKeyService -- create -- id={} -- clientId={}",
                saved.getId(),
                dto.clientId()
        );

        return mapper.toDto(saved);
    }

    public ApiKeyResponseDTO update(
            Long id,
            ApiKeyRequestDTO dto) {

        LOG.info("ENTRY -- ApiKeyService -- update -- id={}", id);
        ApiKey apiKey = apiKeyRepository.findById(id).orElseThrow(() -> new ApiKeyNotFoundException(id));
        Client client = clientRepository.findById(dto.clientId()).orElseThrow(() -> new ClientNotFoundException(dto.clientId()));

        apiKey.setClient(client);

        if (dto.active() != null) {
            apiKey.setActive(dto.active());
        }

        ApiKey updated = apiKeyRepository.save(apiKey);
        LOG.info("OK -- ApiKeyService -- update -- id={}", id);

        return mapper.toDto(updated);
    }

    public void delete(Long id) {
        LOG.info("ENTRY -- ApiKeyService -- delete -- id={}", id);

        if (!apiKeyRepository.existsById(id)) {
            LOG.warn("API Key no encontrada -- ApiKeyService -- delete -- id={}", id);
            throw new ApiKeyNotFoundException(id);
        }

        apiKeyRepository.deleteById(id);
        LOG.info("OK -- ApiKeyService -- delete -- id={}", id);
    }

    private String generateApiKey() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "");
    }

    private String hashApiKey(String apiKey) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(apiKey.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {
            LOG.error("Error generando hash de API Key", e);
            throw new IllegalStateException("No se pudo generar el hash de la API Key", e);
        }
    }
}