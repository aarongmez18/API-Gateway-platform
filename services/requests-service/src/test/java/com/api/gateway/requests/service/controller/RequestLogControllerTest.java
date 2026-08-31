package com.api.gateway.requests.service.controller;

import com.api.gateway.requests.service.dto.request.RequestLogRequestDTO;
import com.api.gateway.requests.service.dto.response.RequestLogResponseDTO;
import com.api.gateway.requests.service.logic.RequestLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestLogControllerTest {

    @Mock
    private RequestLogService service;

    private RequestLogController controller;

    @BeforeEach
    void setUp() { controller = new RequestLogController(service); }

    @Test
    void create_debeRegistrarPeticion() {
        RequestLogRequestDTO request = mock(RequestLogRequestDTO.class);

        controller.create(request);

        verify(service).create(request);
        verifyNoMoreInteractions(service);
    }

    @Test
    void find_debeDevolverPeticionesFiltradas() {
        Long clientId = 56L;
        String apiCode = "apis-management";
        Integer statusCode = 200;
        int page = 0;
        int size = 50;

        RequestLogResponseDTO request1 = mock(RequestLogResponseDTO.class);
        RequestLogResponseDTO request2 = mock(RequestLogResponseDTO.class);
        Page<RequestLogResponseDTO> requests = new PageImpl<>(List.of(request1, request2));

        when(service.find(clientId, apiCode, statusCode, page, size)).thenReturn(requests);

        Page<RequestLogResponseDTO> result = controller.find(clientId, apiCode, statusCode, page, size);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertSame(requests, result);

        verify(service).find(clientId, apiCode, statusCode, page, size);
        verifyNoMoreInteractions(service);
    }

    @Test
    void find_debePermitirFiltrosNulos() {
        int page = 0;
        int size = 50;
        Page<RequestLogResponseDTO> requests = Page.empty();

        when(service.find(null, null, null, page, size)).thenReturn(requests);

        Page<RequestLogResponseDTO> result = controller.find(null, null, null, page, size);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(service).find(null, null, null, page, size);
        verifyNoMoreInteractions(service);
    }
}