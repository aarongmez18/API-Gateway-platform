package com.apigateway.users.repository;

import com.apigateway.users.model.Client;
import com.apigateway.users.repository.repositoryInterfaces.ClientRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClientRepositoryImpl implements ClientRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Client> findAll() {
        return entityManager
                .createQuery("SELECT c FROM Client c", Client.class)
                .getResultList();
    }

    @Override
    public Optional<Client> findById(Long id) {
        return Optional.ofNullable(
                entityManager.find(Client.class, id)
        );
    }

    @Override
    @Transactional
    public Client save(Client client) {

        if (client.getId() == null) {
            entityManager.persist(client);
            return client;
        }

        return entityManager.merge(client);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        Client client = entityManager.find(Client.class, id);

        if (client != null) {
            entityManager.remove(client);
        }
    }

    @Override
    public boolean existsById(Long id) {
        return entityManager.find(Client.class, id) != null;
    }
}