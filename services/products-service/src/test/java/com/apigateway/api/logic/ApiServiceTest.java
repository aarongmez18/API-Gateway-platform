package com.apigateway.api.logic;

import com.apigateway.api.dto.ApiRequestDTO;
import com.apigateway.api.dto.ApiResponseDTO;
import com.apigateway.api.exception.ApiNotFoundException;
import com.apigateway.api.mapper.ApiMapper;
import com.apigateway.api.model.Api;
import com.apigateway.api.repository.repositoryInterfaces.ApiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiServiceTest {

    @Mock
    private ApiRepository repository;

    @Mock
    private ApiMapper mapper;

    private ApiService service;

    @BeforeEach
    void setUp() {
        service = new ApiService(repository, mapper);
    }

    @Test
    void findAll_debeDevolverTodasLasApis() {

        Api api1 = mock(Api.class);
        Api api2 = mock(Api.class);

        ApiResponseDTO dto1 = mock(ApiResponseDTO.class);
        ApiResponseDTO dto2 = mock(ApiResponseDTO.class);

        when(repository.findAll()).thenReturn(List.of(api1, api2));
        when(mapper.toDto(api1)).thenReturn(dto1);
        when(mapper.toDto(api2)).thenReturn(dto2);

        List<ApiResponseDTO> resultado = service.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertSame(dto1, resultado.get(0));
        assertSame(dto2, resultado.get(1));

        verify(repository).findAll();
        verify(mapper).toDto(api1);
        verify(mapper).toDto(api2);
    }

    @Test
    void findById_debeDevolverApiCuandoExiste() {

        Long id = 1L;

        Api api = mock(Api.class);
        ApiResponseDTO response = mock(ApiResponseDTO.class);

        when(repository.findById(id)).thenReturn(Optional.of(api));
        when(mapper.toDto(api)).thenReturn(response);

        ApiResponseDTO resultado = service.findById(id);

        assertSame(response, resultado);

        verify(repository).findById(id);
        verify(mapper).toDto(api);
    }

    @Test
    void findById_debeLanzarExcepcionCuandoNoExiste() {

        Long id = 99L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                ApiNotFoundException.class,
                () -> service.findById(id)
        );

        verify(repository).findById(id);
        verifyNoInteractions(mapper);
    }

    @Test
    void update_debeActualizarApiCuandoExiste() {

        Long id = 1L;

        ApiRequestDTO request = mock(ApiRequestDTO.class);
        Api api = mock(Api.class);
        Api updated = mock(Api.class);
        ApiResponseDTO response = mock(ApiResponseDTO.class);

        when(repository.findById(id)).thenReturn(Optional.of(api));
        when(repository.save(api)).thenReturn(updated);
        when(mapper.toDto(updated)).thenReturn(response);

        ApiResponseDTO resultado = service.update(id, request);

        assertSame(response, resultado);

        verify(repository).findById(id);
        verify(mapper).updateEntity(request, api);
        verify(repository).save(api);
        verify(mapper).toDto(updated);
    }

    @Test
    void update_debeLanzarExcepcionCuandoNoExiste() {

        Long id = 99L;
        ApiRequestDTO request = mock(ApiRequestDTO.class);

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(
                ApiNotFoundException.class,
                () -> service.update(id, request)
        );

        verify(repository).findById(id);
        verify(repository, never()).save(any());
    }

    @Test
    void delete_debeEliminarApiCuandoExiste() {

        Long id = 1L;

        when(repository.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void delete_debeLanzarExcepcionCuandoNoExiste() {

        Long id = 99L;

        when(repository.existsById(id)).thenReturn(false);

        assertThrows(
                ApiNotFoundException.class,
                () -> service.delete(id)
        );

        verify(repository).existsById(id);
        verify(repository, never()).deleteById(anyLong());
    }
}