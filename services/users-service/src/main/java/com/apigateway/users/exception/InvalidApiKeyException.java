package com.apigateway.users.exception;

public class InvalidApiKeyException extends RuntimeException {

    public InvalidApiKeyException() {
        super("API Key inválida");
    }
}