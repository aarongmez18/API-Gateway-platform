package com.apigateway.users.exception;

public class ApiKeyNotFoundException extends RuntimeException {
    public ApiKeyNotFoundException(Long id) {
        super("API Key no encontrada con id: " + id);
    }
}