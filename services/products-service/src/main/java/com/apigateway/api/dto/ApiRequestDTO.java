package com.apigateway.api.dto;

import jakarta.validation.constraints.NotBlank;

public class ApiRequestDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String targetUrl;

    @NotBlank
    private String path;

    private Boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}