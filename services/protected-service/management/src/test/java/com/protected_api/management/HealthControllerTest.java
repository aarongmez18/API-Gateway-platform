package com.protected_api.management;

import com.protected_api.management.controller.HealthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        assertEquals("protected-service", response.get("service"));

        assertNotNull(response.get("timestamp"));
    }
}