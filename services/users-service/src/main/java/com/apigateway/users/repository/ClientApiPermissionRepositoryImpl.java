package com.apigateway.users.repository;

import com.apigateway.users.model.ClientApiPermission;
import com.apigateway.users.repository.repositoryInterfaces.ClientApiPermissionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClientApiPermissionRepositoryImpl implements ClientApiPermissionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public ClientApiPermission save(ClientApiPermission permission) {
        entityManager.persist(permission);
        return permission;
    }

    @Override
    public List<ClientApiPermission> findByClientId(Long clientId) {
        return entityManager.createQuery("""
            SELECT p
            FROM ClientApiPermission p
            WHERE p.client.id = :clientId
            ORDER BY p.apiCode
            """, ClientApiPermission.class)
                .setParameter("clientId", clientId)
                .getResultList();
    }

    @Override
    public boolean existsByClientIdAndApiCode(Long clientId, String apiCode) {
        Long count = entityManager.createQuery("""
            SELECT COUNT(p)
            FROM ClientApiPermission p
            WHERE p.client.id = :clientId
              AND p.apiCode = :apiCode
            """, Long.class)
                .setParameter("clientId", clientId)
                .setParameter("apiCode", apiCode)
                .getSingleResult();

        return count > 0;
    }

    @Override
    @Transactional
    public void deleteByClientIdAndApiCode(Long clientId, String apiCode) {
        entityManager.createQuery("""
            DELETE FROM ClientApiPermission p
            WHERE p.client.id = :clientId
              AND p.apiCode = :apiCode
            """)
                .setParameter("clientId", clientId)
                .setParameter("apiCode", apiCode)
                .executeUpdate();
    }
}