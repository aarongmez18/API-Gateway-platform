package com.api.gateway.requests.service.logic;

import com.api.gateway.requests.service.dto.request.RequestLogRequestDTO;
import com.api.gateway.requests.service.dto.response.RequestDashboardResponseDTO;
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

import java.time.OffsetDateTime;
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
    void dashboard_debeDevolverMetricasAgregadas() {
        when(repository.countAll()).thenReturn(120L);
        when(repository.countBetween(any(OffsetDateTime.class), any(OffsetDateTime.class))).thenReturn(30L);
        when(repository.countErrorsBetween(any(OffsetDateTime.class), any(OffsetDateTime.class))).thenReturn(4L);
        when(repository.averageDurationBetween(any(OffsetDateTime.class), any(OffsetDateTime.class))).thenReturn(82.6);
        when(repository.countByHour(any(OffsetDateTime.class), any(OffsetDateTime.class))).thenReturn(List.of(new Object[]{9, 5L}, new Object[]{12, 8L}));
        when(repository.topApis(any(OffsetDateTime.class), any(OffsetDateTime.class), eq(5))).thenReturn(List.<Object[]>of(new Object[]{"apis-management", 18L}));
        when(repository.topClients(any(OffsetDateTime.class), any(OffsetDateTime.class), eq(5))).thenReturn(List.<Object[]>of(new Object[]{56L, "Mobile App", 14L}));

        RequestDashboardResponseDTO result = service.dashboard();

        assertEquals(120L, result.totalRequests());
        assertEquals(30L, result.todayRequests());
        assertEquals(4L, result.errors());
        assertEquals(83L, result.averageResponseTimeMs());

        assertEquals(24, result.requestsByHour().size());
        assertEquals(5L, result.requestsByHour().get(9).count());
        assertEquals(8L, result.requestsByHour().get(12).count());
        assertEquals(0L, result.requestsByHour().get(10).count());

        assertEquals(24, result.errorsByHour().size());

        assertEquals("apis-management", result.topApis().get(0).apiCode());
        assertEquals(18L, result.topApis().get(0).count());

        assertEquals(56L, result.topClients().get(0).clientId());
        assertEquals("Mobile App", result.topClients().get(0).clientName());
        assertEquals(14L, result.topClients().get(0).count());
    }

    @Test
    void find_debeConvertirApiCodeVacioEnNull() {
        when(repository.findFiltered(isNull(), isNull(), isNull(), any())).thenReturn(Page.empty());

        Page<RequestLogResponseDTO> result = service.find(null, "   ", null, 0, 50);

        assertTrue(result.isEmpty());
        verify(repository).findFiltered(isNull(), isNull(), isNull(), any());
    }
}