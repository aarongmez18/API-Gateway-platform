package com.apigateway.users.repository;

import com.apigateway.users.model.ApiKey;
import com.apigateway.users.repository.repositoryInterfaces.ApiKeyRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ApiKeyRepositoryImpl implements ApiKeyRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ApiKey> findAll() {
        return entityManager
                .createQuery("SELECT a FROM ApiKey a", ApiKey.class)
                .getResultList();
    }

    @Override
    public Optional<ApiKey> findById(Long id) {
        return Optional.ofNullable(
                entityManager.find(ApiKey.class, id)
        );
    }

    @Override
    public List<ApiKey> findByClientId(Long clientId) {
        return entityManager
                .createQuery(
                        "SELECT a FROM ApiKey a WHERE a.client.id = :clientId",
                        ApiKey.class
                )
                .setParameter("clientId", clientId)
                .getResultList();
    }

    @Override
    @Transactional
    public ApiKey save(ApiKey apiKey) {

        if (apiKey.getId() == null) {
            entityManager.persist(apiKey);
            return apiKey;
        }

        return entityManager.merge(apiKey);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        ApiKey apiKey = entityManager.find(ApiKey.class, id);

        if (apiKey != null) {
            entityManager.remove(apiKey);
        }
    }

    @Override
    public boolean existsByClientId(Long clientId) {

        Long count = entityManager
                .createQuery(
                        "SELECT COUNT(a) FROM ApiKey a WHERE a.client.id = :clientId",
                        Long.class
                )
                .setParameter("clientId", clientId)
                .getSingleResult();

        return count > 0;
    }

    @Override
    public boolean existsById(Long id) {
        return entityManager.find(ApiKey.class, id) != null;
    }
}