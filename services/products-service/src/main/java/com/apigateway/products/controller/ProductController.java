package com.apigateway.products.controller;

import com.apigateway.products.model.Product;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final List<Product> products = List.of(
            new Product(1L, "Portatil", 1200.00),
            new Product(2L, "Monitor", 350.00),
            new Product(3L, "Teclado", 80.00)
    );

    @GetMapping
    public List<Product> findAll() {
        return products;
    }

    @GetMapping("/{id}")
    public Product findById(@PathVariable Long id) {
        return products.stream()
                .filter(product -> product.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + id));
    }
}
