package com.apigateway.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "api")
public class Api extends General {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "x_api_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "target_url", nullable = false)
    private String targetUrl;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "code", nullable = false, unique = true, length = 100)
    private String code;

    public Api() {}

    public Api(Long id, String targetUrl, String name, String path) {
        this.id = id;
        this.targetUrl = targetUrl;
        this.name = name;
        this.path = path;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    @Override
    public String toString() {
        return "Api{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", targetUrl='" + targetUrl + '\'' +
                ", path='" + path + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}