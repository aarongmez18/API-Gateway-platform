package com.api.gateway.requests.service.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "REQUEST_LOG")
public class RequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CLIENT_ID")
    private Long clientId;

    @Column(name = "CLIENT_NAME", length = 150)
    private String clientName;

    @Column(name = "API_CODE", nullable = false, length = 100)
    private String apiCode;

    @Column(name = "ENDPOINT", nullable = false, length = 1000)
    private String endpoint;

    @Column(name = "METHOD", nullable = false, length = 10)
    private String method;

    @Column(name = "STATUS_CODE", nullable = false)
    private Integer statusCode;

    @Column(name = "DURATION_MS", nullable = false)
    private Long durationMs;

    @Column(name = "REQUESTED_AT", nullable = false)
    private OffsetDateTime requestedAt;

    protected RequestLog() {}

    public RequestLog(Long clientId, String clientName, String apiCode, String endpoint, String method, Integer statusCode, Long durationMs, OffsetDateTime requestedAt) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.apiCode = apiCode;
        this.endpoint = endpoint;
        this.method = method;
        this.statusCode = statusCode;
        this.durationMs = durationMs;
        this.requestedAt = requestedAt;
    }

    public Long getId() { return id; }
    public Long getClientId() { return clientId; }

    public String getClientName() { return clientName; }
    public String getApiCode() { return apiCode; }

    public String getEndpoint() { return endpoint; }
    public String getMethod() { return method; }

    public Integer getStatusCode() { return statusCode; }
    public Long getDurationMs() { return durationMs; }
    public OffsetDateTime getRequestedAt() { return requestedAt; }
}