package com.apigateway.products.model;

public record Product(
        Long id,
        String name,
        Double price
) {
}
