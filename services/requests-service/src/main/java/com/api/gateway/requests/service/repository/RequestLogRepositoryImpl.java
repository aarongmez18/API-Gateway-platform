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

import java.time.OffsetDateTime;
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

    @Override
    public Long countAll() {
        return entityManager.createQuery("SELECT COUNT(r) FROM RequestLog r", Long.class).getSingleResult();
    }

    @Override
    public Long countBetween(OffsetDateTime from, OffsetDateTime to) {
        return entityManager.createQuery("SELECT COUNT(r) FROM RequestLog r WHERE r.requestedAt >= :from AND r.requestedAt < :to", Long.class).setParameter("from", from).setParameter("to", to).getSingleResult();
    }

    @Override
    public Long countErrorsBetween(OffsetDateTime from, OffsetDateTime to) {
        return entityManager.createQuery("SELECT COUNT(r) FROM RequestLog r WHERE r.requestedAt >= :from AND r.requestedAt < :to AND r.statusCode >= 400", Long.class).setParameter("from", from).setParameter("to", to).getSingleResult();
    }

    @Override
    public Double averageDurationBetween(OffsetDateTime from, OffsetDateTime to) {
        return entityManager.createQuery("SELECT AVG(r.durationMs) FROM RequestLog r WHERE r.requestedAt >= :from AND r.requestedAt < :to", Double.class).setParameter("from", from).setParameter("to", to).getSingleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> countByHour(OffsetDateTime from, OffsetDateTime to) {
        return entityManager.createNativeQuery("""
            SELECT EXTRACT(HOUR FROM REQUESTED_AT)::INTEGER AS HOUR, COUNT(*) AS TOTAL
            FROM REQUEST_LOG
            WHERE REQUESTED_AT >= :from AND REQUESTED_AT < :to
            GROUP BY HOUR
            ORDER BY HOUR
            """).setParameter("from", from).setParameter("to", to).getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> countErrorsByHour(OffsetDateTime from, OffsetDateTime to) {
        return entityManager.createNativeQuery("""
            SELECT EXTRACT(HOUR FROM REQUESTED_AT)::INTEGER AS HOUR, COUNT(*) AS TOTAL
            FROM REQUEST_LOG
            WHERE REQUESTED_AT >= :from AND REQUESTED_AT < :to AND STATUS_CODE >= 400
            GROUP BY HOUR
            ORDER BY HOUR
            """).setParameter("from", from).setParameter("to", to).getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> topApis(OffsetDateTime from, OffsetDateTime to, int limit) {
        return entityManager.createNativeQuery("""
            SELECT API_CODE, COUNT(*) AS TOTAL
            FROM REQUEST_LOG
            WHERE REQUESTED_AT >= :from AND REQUESTED_AT < :to
            GROUP BY API_CODE
            ORDER BY TOTAL DESC
            """).setParameter("from", from).setParameter("to", to).setMaxResults(limit).getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> topClients(OffsetDateTime from, OffsetDateTime to, int limit) {
        return entityManager.createNativeQuery("""
            SELECT CLIENT_ID, CLIENT_NAME, COUNT(*) AS TOTAL
            FROM REQUEST_LOG
            WHERE REQUESTED_AT >= :from AND REQUESTED_AT < :to AND CLIENT_ID IS NOT NULL
            GROUP BY CLIENT_ID, CLIENT_NAME
            ORDER BY TOTAL DESC
            """).setParameter("from", from).setParameter("to", to).setMaxResults(limit).getResultList();
    }
}