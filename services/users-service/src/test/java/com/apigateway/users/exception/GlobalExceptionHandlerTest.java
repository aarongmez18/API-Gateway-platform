package com.apigateway.users.exception;

import com.apigateway.users.dto.response.ErrorResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleClientNotFound_debeDevolver404() {

        ClientNotFoundException exception =
                new ClientNotFoundException(99L);

        ResponseEntity<ErrorResponseDTO> response =
                handler.handleClientNotFound(exception);

        assertEquals(
                HttpStatus.NOT_FOUND,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(
                HttpStatus.NOT_FOUND.value(),
                response.getBody().status()
        );

        assertEquals(
                "Cliente no encontrado con id: 99",
                response.getBody().message()
        );

        assertNotNull(
                response.getBody().timestamp()
        );
    }

    @Test
    void handleApiKeyNotFound_debeDevolver404() {

        ApiKeyNotFoundException exception =
                new ApiKeyNotFoundException(50L);

        ResponseEntity<ErrorResponseDTO> response =
                handler.handleApiKeyNotFound(exception);

        assertEquals(
                HttpStatus.NOT_FOUND,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(
                HttpStatus.NOT_FOUND.value(),
                response.getBody().status()
        );

        assertEquals(
                "API Key no encontrada con id: 50",
                response.getBody().message()
        );

        assertNotNull(
                response.getBody().timestamp()
        );
    }

    @Test
    void handleException_debeDevolver500() {

        Exception exception =
                new RuntimeException("Error inesperado");

        ResponseEntity<ErrorResponseDTO> response =
                handler.handleException(exception);

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                response.getBody().status()
        );

        assertEquals(
                "Error interno del servidor",
                response.getBody().message()
        );

        assertNotNull(
                response.getBody().timestamp()
        );
    }
}