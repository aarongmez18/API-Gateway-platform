package com.apigateway.users.model;

import jakarta.persistence.*;

@Entity
@Table(name = "client")
public class Client extends General {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "x_client_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "rate_limit_per_minute", nullable = false)
    private Integer rateLimitPerMinute = 100;

    public Client() {}

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(Integer rateLimitPerMinute, String name, Long id) {
        this.rateLimitPerMinute = rateLimitPerMinute;
        this.name = name;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRateLimitPerMinute() { return rateLimitPerMinute; }

    public void setRateLimitPerMinute(Integer rateLimitPerMinute) { this.rateLimitPerMinute = rateLimitPerMinute;}

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}