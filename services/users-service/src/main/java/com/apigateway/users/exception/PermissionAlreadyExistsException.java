package com.apigateway.users.exception;

public class PermissionAlreadyExistsException extends RuntimeException {

    public PermissionAlreadyExistsException(Long clientId, String apiCode) {
        super("El cliente " + clientId + " ya tiene permiso para la API " + apiCode);
    }
}