package com.api.gateway.requests.service.repository;

import com.api.gateway.requests.service.model.RequestLog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestLogRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<RequestLog> resultQuery;

    @Mock
    private TypedQuery<Long> countQuery;

    private RequestLogRepositoryImpl repository;

    @BeforeEach
    void setUp() throws Exception {
        repository = new RequestLogRepositoryImpl();

        var field = RequestLogRepositoryImpl.class.getDeclaredField("entityManager");
        field.setAccessible(true);
        field.set(repository, entityManager);
    }

    @Test
    void save_debePersistirPeticion() {
        RequestLog requestLog = mock(RequestLog.class);

        RequestLog result = repository.save(requestLog);

        assertSame(requestLog, result);

        verify(entityManager).persist(requestLog);
        verifyNoMoreInteractions(entityManager);
    }

    @Test
    void findFiltered_debeDevolverPeticionesPaginadas() {
        Long clientId = 56L;
        String apiCode = "apis-management";
        Integer statusCode = 200;
        Pageable pageable = PageRequest.of(0, 50);

        RequestLog request1 = mock(RequestLog.class);
        RequestLog request2 = mock(RequestLog.class);
        List<RequestLog> requests = List.of(request1, request2);

        String where = """
                WHERE (:clientId IS NULL OR r.clientId = :clientId)
                  AND (:apiCode IS NULL OR r.apiCode = :apiCode)
                  AND (:statusCode IS NULL OR r.statusCode = :statusCode)
                """;

        when(entityManager.createQuery("SELECT r FROM RequestLog r " + where + " ORDER BY r.requestedAt DESC", RequestLog.class)).thenReturn(resultQuery);
        when(entityManager.createQuery("SELECT COUNT(r) FROM RequestLog r " + where, Long.class)).thenReturn(countQuery);
        when(resultQuery.setParameter(anyString(), any())).thenReturn(resultQuery);
        when(resultQuery.setFirstResult(anyInt())).thenReturn(resultQuery);
        when(resultQuery.setMaxResults(anyInt())).thenReturn(resultQuery);
        when(resultQuery.getResultList()).thenReturn(requests);
        when(countQuery.setParameter(anyString(), any())).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(2L);

        Page<RequestLog> result = repository.findFiltered(clientId, apiCode, statusCode, pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertSame(request1, result.getContent().get(0));
        assertSame(request2, result.getContent().get(1));

        verify(resultQuery).setParameter("clientId", clientId);
        verify(resultQuery).setParameter("apiCode", apiCode);
        verify(resultQuery).setParameter("statusCode", statusCode);
        verify(resultQuery).setFirstResult(0);
        verify(resultQuery).setMaxResults(50);

        verify(countQuery).setParameter("clientId", clientId);
        verify(countQuery).setParameter("apiCode", apiCode);
        verify(countQuery).setParameter("statusCode", statusCode);
        verify(countQuery).getSingleResult();
    }

    @Test
    void findFiltered_debeAplicarOffsetDePaginacion() {
        Pageable pageable = PageRequest.of(2, 25);

        String where = """
                WHERE (:clientId IS NULL OR r.clientId = :clientId)
                  AND (:apiCode IS NULL OR r.apiCode = :apiCode)
                  AND (:statusCode IS NULL OR r.statusCode = :statusCode)
                """;

        when(entityManager.createQuery("SELECT r FROM RequestLog r " + where + " ORDER BY r.requestedAt DESC", RequestLog.class)).thenReturn(resultQuery);
        when(entityManager.createQuery("SELECT COUNT(r) FROM RequestLog r " + where, Long.class)).thenReturn(countQuery);
        when(resultQuery.setParameter(anyString(), any())).thenReturn(resultQuery);
        when(resultQuery.setFirstResult(anyInt())).thenReturn(resultQuery);
        when(resultQuery.setMaxResults(anyInt())).thenReturn(resultQuery);
        when(resultQuery.getResultList()).thenReturn(List.of());
        when(countQuery.setParameter(anyString(), any())).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(100L);

        Page<RequestLog> result = repository.findFiltered(null, null, null, pageable);

        assertNotNull(result);
        assertEquals(100, result.getTotalElements());
        assertEquals(4, result.getTotalPages());

        verify(resultQuery).setFirstResult(50);
        verify(resultQuery).setMaxResults(25);
    }
}