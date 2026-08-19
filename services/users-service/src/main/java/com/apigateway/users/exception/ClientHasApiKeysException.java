package com.apigateway.users.exception;

public class ClientHasApiKeysException extends RuntimeException {

    public ClientHasApiKeysException(Long id) {
        super("No se puede eliminar el cliente con id " + id +
                " porque tiene API Keys asociadas");
    }
}