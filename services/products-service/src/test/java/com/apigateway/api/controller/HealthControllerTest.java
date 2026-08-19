package com.apigateway.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HealthControllerTest {

    private HealthController controller;

    @BeforeEach
    void setUp() {
        controller = new HealthController();
    }

    @Test
    void health_debeDevolverEstadoUp() {

        Map<String, Object> response = controller.health();

        assertNotNull(response);

        assertEquals("UP", response.get("status"));
        assertEquals("api-service", response.get("service"));

        assertNotNull(response.get("timestamp"));
    }
}