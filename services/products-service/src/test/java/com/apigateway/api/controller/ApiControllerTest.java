package com.apigateway.api.controller;

import com.apigateway.api.dto.ApiRequestDTO;
import com.apigateway.api.dto.ApiResponseDTO;
import com.apigateway.api.logic.ApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiControllerTest {

    @Mock
    private ApiService service;

    private ApiController controller;

    @BeforeEach
    void setUp() {
        controller = new ApiController(service);
    }

    @Test
    void findAll_debeDevolverTodasLasApis() {

        ApiResponseDTO api1 = mock(ApiResponseDTO.class);
        ApiResponseDTO api2 = mock(ApiResponseDTO.class);

        List<ApiResponseDTO> apis = List.of(api1, api2);

        when(service.findAll()).thenReturn(apis);

        List<ApiResponseDTO> resultado = controller.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertSame(apis, resultado);

        verify(service, times(1)).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    void findById_debeDevolverApi() {

        Long id = 1L;

        ApiResponseDTO response = mock(ApiResponseDTO.class);

        when(service.findById(id)).thenReturn(response);

        ApiResponseDTO resultado = controller.findById(id);

        assertNotNull(resultado);
        assertSame(response, resultado);

        verify(service, times(1)).findById(id);
        verifyNoMoreInteractions(service);
    }

    @Test
    void create_debeCrearApi() {

        ApiRequestDTO request = mock(ApiRequestDTO.class);
        ApiResponseDTO response = mock(ApiResponseDTO.class);

        when(service.create(request)).thenReturn(response);

        ApiResponseDTO resultado = controller.create(request);

        assertNotNull(resultado);
        assertSame(response, resultado);

        verify(service, times(1)).create(request);
        verifyNoMoreInteractions(service);
    }

    @Test
    void update_debeActualizarApi() {

        Long id = 1L;

        ApiRequestDTO request = mock(ApiRequestDTO.class);
        ApiResponseDTO response = mock(ApiResponseDTO.class);

        when(service.update(id, request)).thenReturn(response);

        ApiResponseDTO resultado = controller.update(id, request);

        assertNotNull(resultado);
        assertSame(response, resultado);

        verify(service, times(1)).update(id, request);
        verifyNoMoreInteractions(service);
    }

    @Test
    void delete_debeEliminarApi() {

        Long id = 1L;

        doNothing().when(service).delete(id);

        controller.delete(id);

        verify(service, times(1)).delete(id);
        verifyNoMoreInteractions(service);
    }
}