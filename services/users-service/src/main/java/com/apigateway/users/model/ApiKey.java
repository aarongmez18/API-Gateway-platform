package com.apigateway.users.model;

import jakarta.persistence.*;

@Entity
@Table(name = "api_key")
public class ApiKey extends General {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "x_api_key_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "x_client_id",
            nullable = false
    )
    private Client client;

    @Column(name = "key_hash", nullable = false, length = 255)
    private String keyHash;

    public ApiKey() {}

    public ApiKey(Long id, Client client, String keyHash) {
        this.id = id;
        this.client = client;
        this.keyHash = keyHash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getKeyHash() {
        return keyHash;
    }

    public void setKeyHash(String keyHash) {
        this.keyHash = keyHash;
    }

    @Override
    public String toString() {
        return "ApiKey{" +
                "id=" + id +
                ", clientId=" + (client != null ? client.getId() : null) +
                ", keyHash='" + keyHash + '\'' +
                '}';
    }
}