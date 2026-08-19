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

    public Client() {}

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}