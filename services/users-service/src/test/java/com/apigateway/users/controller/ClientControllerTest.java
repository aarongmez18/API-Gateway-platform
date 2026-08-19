package com.apigateway.users.controller;

import com.apigateway.users.dto.request.ClientRequestDTO;
import com.apigateway.users.dto.response.ClientResponseDTO;
import com.apigateway.users.logic.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    private ClientService service;

    private ClientController controller;

    @BeforeEach
    void setUp() {
        controller = new ClientController(service);
    }

    @Test
    void findAll_debeDevolverTodosLosClientes() {

        ClientResponseDTO client1 = mock(ClientResponseDTO.class);
        ClientResponseDTO client2 = mock(ClientResponseDTO.class);

        List<ClientResponseDTO> clients = List.of(client1, client2);

        when(service.findAll()).thenReturn(clients);

        List<ClientResponseDTO> resultado = controller.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertSame(clients, resultado);

        verify(service).findAll();
        verifyNoMoreInteractions(service);
    }

    @Test
    void findById_debeDevolverCliente() {

        Long id = 1L;

        ClientResponseDTO response = mock(ClientResponseDTO.class);

        when(service.findById(id)).thenReturn(response);

        ClientResponseDTO resultado = controller.findById(id);

        assertNotNull(resultado);
        assertSame(response, resultado);

        verify(service).findById(id);
        verifyNoMoreInteractions(service);
    }

    @Test
    void create_debeCrearCliente() {

        ClientRequestDTO request = mock(ClientRequestDTO.class);
        ClientResponseDTO response = mock(ClientResponseDTO.class);

        when(service.create(request)).thenReturn(response);

        ClientResponseDTO resultado = controller.create(request);

        assertNotNull(resultado);
        assertSame(response, resultado);

        verify(service).create(request);
        verifyNoMoreInteractions(service);
    }

    @Test
    void update_debeActualizarCliente() {

        Long id = 1L;

        ClientRequestDTO request = mock(ClientRequestDTO.class);
        ClientResponseDTO response = mock(ClientResponseDTO.class);

        when(service.update(id, request)).thenReturn(response);

        ClientResponseDTO resultado = controller.update(id, request);

        assertNotNull(resultado);
        assertSame(response, resultado);

        verify(service).update(id, request);
        verifyNoMoreInteractions(service);
    }

    @Test
    void delete_debeEliminarCliente() {

        Long id = 1L;

        doNothing().when(service).delete(id);

        controller.delete(id);

        verify(service).delete(id);
        verifyNoMoreInteractions(service);
    }
}