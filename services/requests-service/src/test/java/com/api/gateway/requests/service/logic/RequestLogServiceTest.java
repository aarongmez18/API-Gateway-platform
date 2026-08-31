package com.api.gateway.requests.service.logic;

import com.api.gateway.requests.service.dto.request.RequestLogRequestDTO;
import com.api.gateway.requests.service.dto.response.RequestLogResponseDTO;
import com.api.gateway.requests.service.mapper.RequestLogMapper;
import com.api.gateway.requests.service.model.RequestLog;
import com.api.gateway.requests.service.repository.repositoryInterfaces.RequestLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestLogServiceTest {

    @Mock
    private RequestLogRepository repository;

    @Mock
    private RequestLogMapper mapper;

    private RequestLogService service;

    @BeforeEach
    void setUp() { service = new RequestLogService(repository, mapper); }

    @Test
    void create_debeGuardarPeticion() {
        RequestLogRequestDTO dto = mock(RequestLogRequestDTO.class);
        RequestLog requestLog = mock(RequestLog.class);
        RequestLog saved = mock(RequestLog.class);

        when(mapper.toEntity(dto)).thenReturn(requestLog);
        when(repository.save(requestLog)).thenReturn(saved);

        service.create(dto);

        verify(mapper).toEntity(dto);
        verify(repository).save(requestLog);
    }

    @Test
    void find_debeDevolverPeticionesFiltradas() {
        RequestLog requestLog = mock(RequestLog.class);
        RequestLogResponseDTO response = mock(RequestLogResponseDTO.class);
        Page<RequestLog> page = new PageImpl<>(List.of(requestLog));

        when(repository.findFiltered(eq(56L), eq("apis-management"), eq(200), any())).thenReturn(page);
        when(mapper.toDto(requestLog)).thenReturn(response);

        Page<RequestLogResponseDTO> result = service.find(56L, "apis-management", 200, 0, 50);

        assertEquals(1, result.getTotalElements());
        assertSame(response, result.getContent().get(0));
        verify(repository).findFiltered(eq(56L), eq("apis-management"), eq(200), any());
        verify(mapper).toDto(requestLog);
    }

    @Test
    void find_debeConvertirApiCodeVacioEnNull() {
        when(repository.findFiltered(isNull(), isNull(), isNull(), any())).thenReturn(Page.empty());

        Page<RequestLogResponseDTO> result = service.find(null, "   ", null, 0, 50);

        assertTrue(result.isEmpty());
        verify(repository).findFiltered(isNull(), isNull(), isNull(), any());
    }
}