package com.apigateway.users.controller;

import com.apigateway.users.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final List<User> users = List.of(
            new User(1L, "Aaron", "aaron@test.com"),
            new User(2L, "Laura", "laura@test.com"),
            new User(3L, "Carlos", "carlos@test.com")
    );

    @GetMapping
    public List<User> findAll() {
        return users;
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        return users.stream()
                .filter(user -> user.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));
    }
}
