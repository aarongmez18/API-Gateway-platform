package com.apigateway.users.model;

public record User(
        Long id,
        String name,
        String email
) {
}
