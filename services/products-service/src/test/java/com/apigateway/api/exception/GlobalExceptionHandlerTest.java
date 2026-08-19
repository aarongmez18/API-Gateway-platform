package com.apigateway.api.exception;

import com.apigateway.api.dto.ErrorResponseDTO;
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
    void handleApiNotFound_debeDevolver404() {

        ApiNotFoundException exception =
                new ApiNotFoundException(99L);

        ResponseEntity<ErrorResponseDTO> response =
                handler.handleApiNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals(
                HttpStatus.NOT_FOUND.value(),
                response.getBody().status()
        );

        assertEquals(
                "API no encontrada con id: 99",
                response.getBody().message()
        );

        assertNotNull(response.getBody().timestamp());
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

        assertNotNull(response.getBody().timestamp());
    }
}