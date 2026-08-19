package com.apigateway.users.controller;

import com.apigateway.users.dto.request.ApiKeyRequestDTO;
import com.apigateway.users.dto.response.ApiKeyResponseDTO;
import com.apigateway.users.logic.ApiKeyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiKeyControllerTest {

    @Mock
    private ApiKeyService service;

    private ApiKeyController controller;

    @BeforeEach
    void setUp() {
        controller = new ApiKeyController(service);
    }

    @Test
    void findAll_debeDevolverTodasLasApiKeys() {

        ApiKeyResponseDTO apiKey1 = mock(ApiKeyResponseDTO.class);
        ApiKeyResponseDTO apiKey2 = mock(ApiKeyResponseDTO.class);

        List<ApiKeyResponseDTO> apiKeys = List.of(apiKey1, apiKey2);

        when(service.findAll()).thenReturn(apiKeys);

        List<ApiKeyResponseDTO> resultado =
                controller.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertSame(apiKeys, resultado);

        verify(service).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    void findById_debeDevolverApiKey() {

        Long id = 1L;

        ApiKeyResponseDTO response =
                mock(ApiKeyResponseDTO.class);

        when(service.findById(id)).thenReturn(response);

        ApiKeyResponseDTO resultado =
                controller.findById(id);

        assertNotNull(resultado);
        assertSame(response, resultado);

        verify(service).findById(id);
        verifyNoMoreInteractions(service);
    }

    @Test
    void findByClientId_debeDevolverApiKeysDelCliente() {

        Long clientId = 10L;

        ApiKeyResponseDTO apiKey1 =
                mock(ApiKeyResponseDTO.class);

        ApiKeyResponseDTO apiKey2 =
                mock(ApiKeyResponseDTO.class);

        List<ApiKeyResponseDTO> apiKeys =
                List.of(apiKey1, apiKey2);

        when(service.findByClientId(clientId))
                .thenReturn(apiKeys);

        List<ApiKeyResponseDTO> resultado =
                controller.findByClientId(clientId);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertSame(apiKeys, resultado);

        verify(service).findByClientId(clientId);
        verifyNoMoreInteractions(service);
    }

    @Test
    void create_debeCrearApiKey() {

        ApiKeyRequestDTO request =
                mock(ApiKeyRequestDTO.class);

        ApiKeyResponseDTO response =
                mock(ApiKeyResponseDTO.class);

        when(service.create(request))
                .thenReturn(response);

        ApiKeyResponseDTO resultado =
                controller.create(request);

        assertNotNull(resultado);
        assertSame(response, resultado);

        verify(service).create(request);
        verifyNoMoreInteractions(service);
    }

    @Test
    void update_debeActualizarApiKey() {

        Long id = 1L;

        ApiKeyRequestDTO request =
                mock(ApiKeyRequestDTO.class);

        ApiKeyResponseDTO response =
                mock(ApiKeyResponseDTO.class);

        when(service.update(id, request))
                .thenReturn(response);

        ApiKeyResponseDTO resultado =
                controller.update(id, request);

        assertNotNull(resultado);
        assertSame(response, resultado);

        verify(service).update(id, request);
        verifyNoMoreInteractions(service);
    }

    @Test
    void delete_debeEliminarApiKey() {

        Long id = 1L;

        doNothing().when(service).delete(id);

        controller.delete(id);

        verify(service).delete(id);
        verifyNoMoreInteractions(service);
    }
}