package com.apigateway.users.repository;

import com.apigateway.users.model.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
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
class ClientRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Client> typedQuery;

    private ClientRepositoryImpl repository;

    @BeforeEach
    void setUp() throws Exception {

        repository = new ClientRepositoryImpl();

        var field = ClientRepositoryImpl.class
                .getDeclaredField("entityManager");

        field.setAccessible(true);
        field.set(repository, entityManager);
    }

    @Test
    void findAll_debeDevolverTodosLosClientes() {

        Client client1 = mock(Client.class);
        Client client2 = mock(Client.class);

        List<Client> clients = List.of(client1, client2);

        when(entityManager.createQuery(
                "SELECT c FROM Client c",
                Client.class
        )).thenReturn(typedQuery);

        when(typedQuery.getResultList())
                .thenReturn(clients);

        List<Client> resultado = repository.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertSame(clients, resultado);

        verify(entityManager).createQuery(
                "SELECT c FROM Client c",
                Client.class
        );

        verify(typedQuery).getResultList();
    }

    @Test
    void findById_debeDevolverClienteCuandoExiste() {

        Long id = 1L;
        Client client = mock(Client.class);

        when(entityManager.find(Client.class, id))
                .thenReturn(client);

        Optional<Client> resultado = repository.findById(id);

        assertTrue(resultado.isPresent());
        assertSame(client, resultado.get());

        verify(entityManager).find(Client.class, id);
    }

    @Test
    void findById_debeDevolverEmptyCuandoNoExiste() {

        Long id = 99L;

        when(entityManager.find(Client.class, id))
                .thenReturn(null);

        Optional<Client> resultado = repository.findById(id);

        assertTrue(resultado.isEmpty());

        verify(entityManager).find(Client.class, id);
    }

    @Test
    void save_debeHacerPersistCuandoEsNuevo() {

        Client client = mock(Client.class);

        when(client.getId()).thenReturn(null);

        Client resultado = repository.save(client);

        assertSame(client, resultado);

        verify(entityManager).persist(client);
        verify(entityManager, never()).merge(any(Client.class));
    }

    @Test
    void save_debeHacerMergeCuandoYaExiste() {

        Client client = mock(Client.class);
        Client merged = mock(Client.class);

        when(client.getId()).thenReturn(1L);
        when(entityManager.merge(client))
                .thenReturn(merged);

        Client resultado = repository.save(client);

        assertSame(merged, resultado);

        verify(entityManager).merge(client);
        verify(entityManager, never()).persist(any(Client.class));
    }

    @Test
    void deleteById_debeEliminarCuandoExiste() {

        Long id = 1L;
        Client client = mock(Client.class);

        when(entityManager.find(Client.class, id))
                .thenReturn(client);

        repository.deleteById(id);

        verify(entityManager).find(Client.class, id);
        verify(entityManager).remove(client);
    }

    @Test
    void deleteById_noDebeEliminarCuandoNoExiste() {

        Long id = 99L;

        when(entityManager.find(Client.class, id))
                .thenReturn(null);

        repository.deleteById(id);

        verify(entityManager).find(Client.class, id);
        verify(entityManager, never()).remove(any());
    }

    @Test
    void existsById_debeDevolverTrueCuandoExiste() {

        Long id = 1L;
        Client client = mock(Client.class);

        when(entityManager.find(Client.class, id))
                .thenReturn(client);

        boolean resultado = repository.existsById(id);

        assertTrue(resultado);

        verify(entityManager).find(Client.class, id);
    }

    @Test
    void existsById_debeDevolverFalseCuandoNoExiste() {

        Long id = 99L;

        when(entityManager.find(Client.class, id))
                .thenReturn(null);

        boolean resultado = repository.existsById(id);

        assertFalse(resultado);

        verify(entityManager).find(Client.class, id);
    }
}