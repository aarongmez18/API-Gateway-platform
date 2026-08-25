package com.apigateway.api.repository;

import com.apigateway.api.model.Api;
import com.apigateway.api.repository.repositoryInterfaces.ApiRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ApiRepositoryImpl implements ApiRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Api> findAll() {
        return entityManager
                .createQuery("SELECT a FROM Api a", Api.class)
                .getResultList();
    }

    @Override
    public Optional<Api> findById(Long id) {
        return Optional.ofNullable(
                entityManager.find(Api.class, id)
        );
    }

    @Override
    @Transactional
    public Api save(Api api) {

        if (api.getId() == null) {
            entityManager.persist(api);
            return api;
        }

        return entityManager.merge(api);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        Api api = entityManager.find(Api.class, id);

        if (api != null) {
            entityManager.remove(api);
        }
    }

    @Override
    public boolean existsByCode(String code) {
        Long count = entityManager.createQuery("SELECT COUNT(a) FROM Api a WHERE a.code = :code", Long.class).setParameter("code", code).getSingleResult();
        return count > 0;
    }

    @Override
    public boolean existsById(Long id) {
        return entityManager.find(Api.class, id) != null;
    }
}