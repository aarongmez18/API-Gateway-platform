package com.apigateway.users.logic;

import com.apigateway.users.dto.request.ApiKeyRequestDTO;
import com.apigateway.users.dto.response.ApiKeyResponseDTO;
import com.apigateway.users.exception.ApiKeyNotFoundException;
import com.apigateway.users.exception.ClientNotFoundException;
import com.apigateway.users.mapper.ApiKeyMapper;
import com.apigateway.users.model.ApiKey;
import com.apigateway.users.model.Client;
import com.apigateway.users.repository.repositoryInterfaces.ApiKeyRepository;
import com.apigateway.users.repository.repositoryInterfaces.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiKeyServiceTest {

    @Mock
    private ApiKeyRepository apiKeyRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ApiKeyMapper mapper;

    private ApiKeyService service;

    @BeforeEach
    void setUp() {
        service = new ApiKeyService(
                apiKeyRepository,
                clientRepository,
                mapper
        );
    }

    @Test
    void findAll_debeDevolverTodasLasApiKeys() {

        ApiKey apiKey1 = mock(ApiKey.class);
        ApiKey apiKey2 = mock(ApiKey.class);

        ApiKeyResponseDTO dto1 =
                mock(ApiKeyResponseDTO.class);

        ApiKeyResponseDTO dto2 =
                mock(ApiKeyResponseDTO.class);

        when(apiKeyRepository.findAll())
                .thenReturn(List.of(apiKey1, apiKey2));

        when(mapper.toDto(apiKey1))
                .thenReturn(dto1);

        when(mapper.toDto(apiKey2))
                .thenReturn(dto2);

        List<ApiKeyResponseDTO> resultado =
                service.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        assertSame(dto1, resultado.get(0));
        assertSame(dto2, resultado.get(1));

        verify(apiKeyRepository).findAll();
        verify(mapper).toDto(apiKey1);
        verify(mapper).toDto(apiKey2);
    }

    @Test
    void findById_debeDevolverApiKeyCuandoExiste() {

        Long id = 1L;

        ApiKey apiKey = mock(ApiKey.class);
        ApiKeyResponseDTO response =
                mock(ApiKeyResponseDTO.class);

        when(apiKeyRepository.findById(id))
                .thenReturn(Optional.of(apiKey));

        when(mapper.toDto(apiKey))
                .thenReturn(response);

        ApiKeyResponseDTO resultado =
                service.findById(id);

        assertNotNull(resultado);
        assertSame(response, resultado);

        verify(apiKeyRepository).findById(id);
        verify(mapper).toDto(apiKey);
    }

    @Test
    void findById_debeLanzarExcepcionCuandoNoExiste() {

        Long id = 99L;

        when(apiKeyRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ApiKeyNotFoundException.class,
                () -> service.findById(id)
        );

        verify(apiKeyRepository).findById(id);
        verifyNoInteractions(mapper);
    }

    @Test
    void findByClientId_debeDevolverApiKeysDelCliente() {

        Long clientId = 10L;

        ApiKey apiKey1 = mock(ApiKey.class);
        ApiKey apiKey2 = mock(ApiKey.class);

        ApiKeyResponseDTO dto1 =
                mock(ApiKeyResponseDTO.class);

        ApiKeyResponseDTO dto2 =
                mock(ApiKeyResponseDTO.class);

        when(clientRepository.existsById(clientId))
                .thenReturn(true);

        when(apiKeyRepository.findByClientId(clientId))
                .thenReturn(List.of(apiKey1, apiKey2));

        when(mapper.toDto(apiKey1))
                .thenReturn(dto1);

        when(mapper.toDto(apiKey2))
                .thenReturn(dto2);

        List<ApiKeyResponseDTO> resultado =
                service.findByClientId(clientId);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(clientRepository).existsById(clientId);
        verify(apiKeyRepository).findByClientId(clientId);
        verify(mapper).toDto(apiKey1);
        verify(mapper).toDto(apiKey2);
    }

    @Test
    void findByClientId_debeLanzarExcepcionCuandoClienteNoExiste() {

        Long clientId = 99L;

        when(clientRepository.existsById(clientId))
                .thenReturn(false);

        assertThrows(
                ClientNotFoundException.class,
                () -> service.findByClientId(clientId)
        );

        verify(clientRepository).existsById(clientId);

        verify(apiKeyRepository, never())
                .findByClientId(anyLong());
    }

    @Test
    void create_debeCrearApiKeyConHash() {

        Long clientId = 1L;

        ApiKeyRequestDTO request =
                mock(ApiKeyRequestDTO.class);

        Client client = mock(Client.class);

        ApiKeyResponseDTO response =
                mock(ApiKeyResponseDTO.class);

        when(request.clientId())
                .thenReturn(clientId);

        when(request.active())
                .thenReturn(true);

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(client));

        when(apiKeyRepository.save(any(ApiKey.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(mapper.toDto(any(ApiKey.class)))
                .thenReturn(response);

        ApiKeyResponseDTO resultado =
                service.create(request);

        assertNotNull(resultado);
        assertSame(response, resultado);

        ArgumentCaptor<ApiKey> captor =
                ArgumentCaptor.forClass(ApiKey.class);

        verify(apiKeyRepository).save(captor.capture());

        ApiKey apiKeyGuardada = captor.getValue();

        assertSame(client, apiKeyGuardada.getClient());

        assertNotNull(apiKeyGuardada.getKeyHash());

        // SHA-256 en hexadecimal = 64 caracteres
        assertEquals(
                64,
                apiKeyGuardada.getKeyHash().length()
        );

        assertEquals(
                true,
                apiKeyGuardada.getActive()
        );
    }

    @Test
    void create_debeLanzarExcepcionCuandoClienteNoExiste() {

        Long clientId = 99L;

        ApiKeyRequestDTO request =
                mock(ApiKeyRequestDTO.class);

        when(request.clientId())
                .thenReturn(clientId);

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.empty());

        assertThrows(
                ClientNotFoundException.class,
                () -> service.create(request)
        );

        verify(clientRepository).findById(clientId);

        verify(apiKeyRepository, never())
                .save(any());
    }

    @Test
    void update_debeActualizarApiKeyCuandoExiste() {

        Long id = 1L;
        Long clientId = 10L;

        ApiKeyRequestDTO request =
                mock(ApiKeyRequestDTO.class);

        ApiKey apiKey = mock(ApiKey.class);
        ApiKey updated = mock(ApiKey.class);
        Client client = mock(Client.class);

        ApiKeyResponseDTO response =
                mock(ApiKeyResponseDTO.class);

        when(request.clientId())
                .thenReturn(clientId);

        when(request.active())
                .thenReturn(true);

        when(apiKeyRepository.findById(id))
                .thenReturn(Optional.of(apiKey));

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(client));

        when(apiKeyRepository.save(apiKey))
                .thenReturn(updated);

        when(mapper.toDto(updated))
                .thenReturn(response);

        ApiKeyResponseDTO resultado =
                service.update(id, request);

        assertNotNull(resultado);
        assertSame(response, resultado);

        verify(apiKeyRepository).findById(id);
        verify(clientRepository).findById(clientId);

        verify(apiKey).setClient(client);
        verify(apiKey).setActive(true);

        verify(apiKeyRepository).save(apiKey);
        verify(mapper).toDto(updated);
    }

    @Test
    void update_debeLanzarExcepcionCuandoApiKeyNoExiste() {

        Long id = 99L;

        ApiKeyRequestDTO request =
                mock(ApiKeyRequestDTO.class);

        when(apiKeyRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ApiKeyNotFoundException.class,
                () -> service.update(id, request)
        );

        verify(apiKeyRepository).findById(id);

        verify(clientRepository, never())
                .findById(anyLong());

        verify(apiKeyRepository, never())
                .save(any());
    }

    @Test
    void update_debeLanzarExcepcionCuandoClienteNoExiste() {

        Long id = 1L;
        Long clientId = 99L;

        ApiKeyRequestDTO request =
                mock(ApiKeyRequestDTO.class);

        ApiKey apiKey = mock(ApiKey.class);

        when(request.clientId())
                .thenReturn(clientId);

        when(apiKeyRepository.findById(id))
                .thenReturn(Optional.of(apiKey));

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.empty());

        assertThrows(
                ClientNotFoundException.class,
                () -> service.update(id, request)
        );

        verify(apiKeyRepository).findById(id);
        verify(clientRepository).findById(clientId);

        verify(apiKeyRepository, never())
                .save(any());
    }

    @Test
    void delete_debeEliminarApiKeyCuandoExiste() {

        Long id = 1L;

        when(apiKeyRepository.existsById(id))
                .thenReturn(true);

        service.delete(id);

        verify(apiKeyRepository).existsById(id);
        verify(apiKeyRepository).deleteById(id);
    }

    @Test
    void delete_debeLanzarExcepcionCuandoNoExiste() {

        Long id = 99L;

        when(apiKeyRepository.existsById(id))
                .thenReturn(false);

        assertThrows(
                ApiKeyNotFoundException.class,
                () -> service.delete(id)
        );

        verify(apiKeyRepository).existsById(id);

        verify(apiKeyRepository, never())
                .deleteById(anyLong());
    }
}