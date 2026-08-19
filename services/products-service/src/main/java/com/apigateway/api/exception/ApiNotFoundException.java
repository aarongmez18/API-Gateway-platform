package com.apigateway.api.exception;

public class ApiNotFoundException extends RuntimeException {

    public ApiNotFoundException(Long id) {
        super("API no encontrada con id: " + id);
    }
}