package com.apigateway.users.repository.repositoryInterfaces;

import com.apigateway.users.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {

    List<Client> findAll();

    Optional<Client> findById(Long id);

    Client save(Client client);

    void deleteById(Long id);

    boolean existsById(Long id);
}