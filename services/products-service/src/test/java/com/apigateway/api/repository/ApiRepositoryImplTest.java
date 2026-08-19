package com.apigateway.api.repository;

import com.apigateway.api.model.Api;
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
class ApiRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Api> typedQuery;

    private ApiRepositoryImpl repository;

    @BeforeEach
    void setUp() throws Exception {

        repository = new ApiRepositoryImpl();

        var field = ApiRepositoryImpl.class
                .getDeclaredField("entityManager");

        field.setAccessible(true);
        field.set(repository, entityManager);
    }

    @Test
    void findAll_debeDevolverTodasLasApis() {

        Api api1 = mock(Api.class);
        Api api2 = mock(Api.class);

        List<Api> apis = List.of(api1, api2);

        when(entityManager.createQuery(
                "SELECT a FROM Api a",
                Api.class
        )).thenReturn(typedQuery);

        when(typedQuery.getResultList())
                .thenReturn(apis);

        List<Api> resultado = repository.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertSame(apis, resultado);

        verify(entityManager).createQuery(
                "SELECT a FROM Api a",
                Api.class
        );

        verify(typedQuery).getResultList();
    }

    @Test
    void findById_debeDevolverApiCuandoExiste() {

        Long id = 1L;
        Api api = mock(Api.class);

        when(entityManager.find(Api.class, id))
                .thenReturn(api);

        Optional<Api> resultado = repository.findById(id);

        assertTrue(resultado.isPresent());
        assertSame(api, resultado.get());

        verify(entityManager).find(Api.class, id);
    }

    @Test
    void findById_debeDevolverEmptyCuandoNoExiste() {

        Long id = 99L;

        when(entityManager.find(Api.class, id))
                .thenReturn(null);

        Optional<Api> resultado = repository.findById(id);

        assertTrue(resultado.isEmpty());

        verify(entityManager).find(Api.class, id);
    }

    @Test
    void save_debeHacerPersistCuandoEsNueva() {

        Api api = mock(Api.class);

        when(api.getId()).thenReturn(null);

        Api resultado = repository.save(api);

        assertSame(api, resultado);

        verify(entityManager).persist(api);
        verify(entityManager, never()).merge(any(Api.class));
    }

    @Test
    void save_debeHacerMergeCuandoYaExiste() {

        Api api = mock(Api.class);
        Api apiActualizada = mock(Api.class);

        when(api.getId()).thenReturn(1L);
        when(entityManager.merge(api))
                .thenReturn(apiActualizada);

        Api resultado = repository.save(api);

        assertSame(apiActualizada, resultado);

        verify(entityManager).merge(api);
        verify(entityManager, never()).persist(any(Api.class));
    }

    @Test
    void deleteById_debeEliminarCuandoExiste() {

        Long id = 1L;
        Api api = mock(Api.class);

        when(entityManager.find(Api.class, id))
                .thenReturn(api);

        repository.deleteById(id);

        verify(entityManager).find(Api.class, id);
        verify(entityManager).remove(api);
    }

    @Test
    void deleteById_noDebeEliminarCuandoNoExiste() {

        Long id = 99L;

        when(entityManager.find(Api.class, id))
                .thenReturn(null);

        repository.deleteById(id);

        verify(entityManager).find(Api.class, id);
        verify(entityManager, never()).remove(any());
    }

    @Test
    void existsById_debeDevolverTrueCuandoExiste() {

        Long id = 1L;
        Api api = mock(Api.class);

        when(entityManager.find(Api.class, id))
                .thenReturn(api);

        boolean resultado = repository.existsById(id);

        assertTrue(resultado);

        verify(entityManager).find(Api.class, id);
    }

    @Test
    void existsById_debeDevolverFalseCuandoNoExiste() {

        Long id = 99L;

        when(entityManager.find(Api.class, id))
                .thenReturn(null);

        boolean resultado = repository.existsById(id);

        assertFalse(resultado);

        verify(entityManager).find(Api.class, id);
    }
}