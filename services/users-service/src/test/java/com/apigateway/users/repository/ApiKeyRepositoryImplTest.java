package com.apigateway.users.repository;

import com.apigateway.users.model.ApiKey;
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
class ApiKeyRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<ApiKey> typedQuery;

    private ApiKeyRepositoryImpl repository;

    @BeforeEach
    void setUp() throws Exception {

        repository = new ApiKeyRepositoryImpl();

        var field = ApiKeyRepositoryImpl.class
                .getDeclaredField("entityManager");

        field.setAccessible(true);
        field.set(repository, entityManager);
    }

    @Test
    void findAll_debeDevolverTodasLasApiKeys() {

        ApiKey apiKey1 = mock(ApiKey.class);
        ApiKey apiKey2 = mock(ApiKey.class);

        List<ApiKey> apiKeys = List.of(apiKey1, apiKey2);

        when(entityManager.createQuery(
                "SELECT a FROM ApiKey a",
                ApiKey.class
        )).thenReturn(typedQuery);

        when(typedQuery.getResultList())
                .thenReturn(apiKeys);

        List<ApiKey> resultado = repository.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertSame(apiKeys, resultado);

        verify(entityManager).createQuery(
                "SELECT a FROM ApiKey a",
                ApiKey.class
        );

        verify(typedQuery).getResultList();
    }

    @Test
    void findById_debeDevolverApiKeyCuandoExiste() {

        Long id = 1L;
        ApiKey apiKey = mock(ApiKey.class);

        when(entityManager.find(ApiKey.class, id))
                .thenReturn(apiKey);

        Optional<ApiKey> resultado = repository.findById(id);

        assertTrue(resultado.isPresent());
        assertSame(apiKey, resultado.get());

        verify(entityManager).find(ApiKey.class, id);
    }

    @Test
    void findById_debeDevolverEmptyCuandoNoExiste() {

        Long id = 99L;

        when(entityManager.find(ApiKey.class, id))
                .thenReturn(null);

        Optional<ApiKey> resultado = repository.findById(id);

        assertTrue(resultado.isEmpty());

        verify(entityManager).find(ApiKey.class, id);
    }

    @Test
    void findByClientId_debeDevolverApiKeysDelCliente() {

        Long clientId = 10L;

        ApiKey apiKey1 = mock(ApiKey.class);
        ApiKey apiKey2 = mock(ApiKey.class);

        List<ApiKey> apiKeys = List.of(apiKey1, apiKey2);

        when(entityManager.createQuery(
                "SELECT a FROM ApiKey a WHERE a.client.id = :clientId",
                ApiKey.class
        )).thenReturn(typedQuery);

        when(typedQuery.setParameter("clientId", clientId))
                .thenReturn(typedQuery);

        when(typedQuery.getResultList())
                .thenReturn(apiKeys);

        List<ApiKey> resultado =
                repository.findByClientId(clientId);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertSame(apiKeys, resultado);

        verify(entityManager).createQuery(
                "SELECT a FROM ApiKey a WHERE a.client.id = :clientId",
                ApiKey.class
        );

        verify(typedQuery)
                .setParameter("clientId", clientId);

        verify(typedQuery)
                .getResultList();
    }

    @Test
    void save_debeHacerPersistCuandoEsNueva() {

        ApiKey apiKey = mock(ApiKey.class);

        when(apiKey.getId()).thenReturn(null);

        ApiKey resultado = repository.save(apiKey);

        assertSame(apiKey, resultado);

        verify(entityManager).persist(apiKey);
        verify(entityManager, never()).merge(any(ApiKey.class));
    }

    @Test
    void save_debeHacerMergeCuandoYaExiste() {

        ApiKey apiKey = mock(ApiKey.class);
        ApiKey merged = mock(ApiKey.class);

        when(apiKey.getId()).thenReturn(1L);

        when(entityManager.merge(apiKey))
                .thenReturn(merged);

        ApiKey resultado = repository.save(apiKey);

        assertSame(merged, resultado);

        verify(entityManager).merge(apiKey);
        verify(entityManager, never()).persist(any(ApiKey.class));
    }

    @Test
    void deleteById_debeEliminarCuandoExiste() {

        Long id = 1L;
        ApiKey apiKey = mock(ApiKey.class);

        when(entityManager.find(ApiKey.class, id))
                .thenReturn(apiKey);

        repository.deleteById(id);

        verify(entityManager).find(ApiKey.class, id);
        verify(entityManager).remove(apiKey);
    }

    @Test
    void deleteById_noDebeEliminarCuandoNoExiste() {

        Long id = 99L;

        when(entityManager.find(ApiKey.class, id))
                .thenReturn(null);

        repository.deleteById(id);

        verify(entityManager).find(ApiKey.class, id);
        verify(entityManager, never()).remove(any());
    }

    @Test
    void existsById_debeDevolverTrueCuandoExiste() {

        Long id = 1L;
        ApiKey apiKey = mock(ApiKey.class);

        when(entityManager.find(ApiKey.class, id))
                .thenReturn(apiKey);

        boolean resultado = repository.existsById(id);

        assertTrue(resultado);

        verify(entityManager).find(ApiKey.class, id);
    }

    @Test
    void existsById_debeDevolverFalseCuandoNoExiste() {

        Long id = 99L;

        when(entityManager.find(ApiKey.class, id))
                .thenReturn(null);

        boolean resultado = repository.existsById(id);

        assertFalse(resultado);

        verify(entityManager).find(ApiKey.class, id);
    }
}