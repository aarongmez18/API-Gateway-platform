package com.apigateway.users.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "client_api_permission",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_permission_client_api_code",
                        columnNames = {"x_client_id", "api_code"}
                )
        }
)
public class ClientApiPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "x_permission_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "x_client_id", nullable = false)
    private Client client;

    @Column(name = "api_code", nullable = false, length = 100)
    private String apiCode;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public ClientApiPermission() {}

    public ClientApiPermission(Client client, String apiCode) {
        this.client = client;
        this.apiCode = apiCode;
    }

    public Long getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getApiCode() {
        return apiCode;
    }

    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}