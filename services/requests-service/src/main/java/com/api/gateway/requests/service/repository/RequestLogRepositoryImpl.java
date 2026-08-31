package com.api.gateway.requests.service.repository;

import com.api.gateway.requests.service.model.RequestLog;
import com.api.gateway.requests.service.repository.repositoryInterfaces.RequestLogRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RequestLogRepositoryImpl implements RequestLogRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public RequestLog save(RequestLog requestLog) {
        entityManager.persist(requestLog);
        return requestLog;
    }

    @Override
    public Page<RequestLog> findFiltered(Long clientId, String apiCode, Integer statusCode, Pageable pageable) {
        String where = """
                WHERE (:clientId IS NULL OR r.clientId = :clientId)
                  AND (:apiCode IS NULL OR r.apiCode = :apiCode)
                  AND (:statusCode IS NULL OR r.statusCode = :statusCode)
                """;

        TypedQuery<RequestLog> query = entityManager.createQuery("SELECT r FROM RequestLog r " + where + " ORDER BY r.requestedAt DESC", RequestLog.class);
        setParameters(query, clientId, apiCode, statusCode);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        TypedQuery<Long> countQuery = entityManager.createQuery("SELECT COUNT(r) FROM RequestLog r " + where, Long.class);
        setParameters(countQuery, clientId, apiCode, statusCode);

        List<RequestLog> results = query.getResultList();
        long total = countQuery.getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    private void setParameters(TypedQuery<?> query, Long clientId, String apiCode, Integer statusCode) {
        query.setParameter("clientId", clientId);
        query.setParameter("apiCode", apiCode);
        query.setParameter("statusCode", statusCode);
    }
}