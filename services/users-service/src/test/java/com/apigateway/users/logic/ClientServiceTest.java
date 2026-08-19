package com.apigateway.users.logic;

import com.apigateway.users.dto.request.ClientRequestDTO;
import com.apigateway.users.dto.response.ClientResponseDTO;
import com.apigateway.users.exception.ClientNotFoundException;
import com.apigateway.users.mapper.ClientMapper;
import com.apigateway.users.model.Client;
import com.apigateway.users.repository.repositoryInterfaces.ClientRepository;
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
class ClientServiceTest {

    @Mock
    private ClientRepository repository;

    @Mock
    private ClientMapper mapper;

    private ClientService service;

    @BeforeEach
    void setUp() {
        service = new ClientService(repository, mapper);
    }

    @Test
    void findAll_debeDevolverTodosLosClientes() {

        Client client1 = mock(Client.class);
        Client client2 = mock(Client.class);

        ClientResponseDTO dto1 = mock(ClientResponseDTO.class);
        ClientResponseDTO dto2 = mock(ClientResponseDTO.class);

        when(repository.findAll())
                .thenReturn(List.of(client1, client2));

        when(mapper.toDto(client1))
                .thenReturn(dto1);

        when(mapper.toDto(client2))
                .thenReturn(dto2);

        List<ClientResponseDTO> resultado = service.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertSame(dto1, resultado.get(0));
        assertSame(dto2, resultado.get(1));

        verify(repository).findAll();
        verify(mapper).toDto(client1);
        verify(mapper).toDto(client2);
    }

    @Test
    void findById_debeDevolverClienteCuandoExiste() {

        Long id = 1L;

        Client client = mock(Client.class);
        ClientResponseDTO response = mock(ClientResponseDTO.class);

        when(repository.findById(id))
                .thenReturn(Optional.of(client));

        when(mapper.toDto(client))
                .thenReturn(response);

        ClientResponseDTO resultado = service.findById(id);

        assertNotNull(resultado);
        assertSame(response, resultado);

        verify(repository).findById(id);
        verify(mapper).toDto(client);
    }

    @Test
    void findById_debeLanzarExcepcionCuandoNoExiste() {

        Long id = 99L;

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ClientNotFoundException.class,
                () -> service.findById(id)
        );

        verify(repository).findById(id);
        verifyNoInteractions(mapper);
    }

    @Test
    void create_debeCrearCliente() {

        ClientRequestDTO request = mock(ClientRequestDTO.class);

        Client client = mock(Client.class);
        Client saved = mock(Client.class);

        ClientResponseDTO response = mock(ClientResponseDTO.class);

        when(mapper.toEntity(request))
                .thenReturn(client);

        when(repository.save(client))
                .thenReturn(saved);

        when(mapper.toDto(saved))
                .thenReturn(response);

        ClientResponseDTO resultado = service.create(request);

        assertNotNull(resultado);
        assertSame(response, resultado);

        verify(mapper).toEntity(request);
        verify(repository).save(client);
        verify(mapper).toDto(saved);
    }

    @Test
    void update_debeActualizarClienteCuandoExiste() {

        Long id = 1L;

        ClientRequestDTO request = mock(ClientRequestDTO.class);
        Client client = mock(Client.class);
        Client updated = mock(Client.class);

        ClientResponseDTO response = mock(ClientResponseDTO.class);

        when(repository.findById(id))
                .thenReturn(Optional.of(client));

        when(repository.save(client))
                .thenReturn(updated);

        when(mapper.toDto(updated))
                .thenReturn(response);

        ClientResponseDTO resultado =
                service.update(id, request);

        assertNotNull(resultado);
        assertSame(response, resultado);

        verify(repository).findById(id);
        verify(mapper).updateEntity(request, client);
        verify(repository).save(client);
        verify(mapper).toDto(updated);
    }

    @Test
    void update_debeLanzarExcepcionCuandoNoExiste() {

        Long id = 99L;

        ClientRequestDTO request =
                mock(ClientRequestDTO.class);

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                ClientNotFoundException.class,
                () -> service.update(id, request)
        );

        verify(repository).findById(id);
        verify(repository, never()).save(any());
        verify(mapper, never())
                .updateEntity(any(), any());
    }

    @Test
    void delete_debeEliminarClienteCuandoExiste() {

        Long id = 1L;

        when(repository.existsById(id))
                .thenReturn(true);

        service.delete(id);

        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void delete_debeLanzarExcepcionCuandoNoExiste() {

        Long id = 99L;

        when(repository.existsById(id))
                .thenReturn(false);

        assertThrows(
                ClientNotFoundException.class,
                () -> service.delete(id)
        );

        verify(repository).existsById(id);
        verify(repository, never()).deleteById(anyLong());
    }
}