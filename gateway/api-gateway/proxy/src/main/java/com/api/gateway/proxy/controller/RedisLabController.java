package com.api.gateway.proxy.controller;

import com.api.gateway.proxy.service.RedisLabService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/redis-lab")
public class RedisLabController {

    private final RedisLabService service;

    public RedisLabController(RedisLabService service) {
        this.service = service;
    }

    @PostMapping("/{key}")
    public Mono<Boolean> save(@PathVariable String key, @RequestParam String value) {
        return service.save(key, value);
    }

    @GetMapping("/{key}")
    public Mono<String> get(@PathVariable String key) {
        return service.get(key);
    }

    @PostMapping("/counter/{key}")
    public Mono<Long> increment(@PathVariable String key) {
        return service.increment(key);
    }

    @PostMapping("/expire/{key}")
    public Mono<Boolean> expire(@PathVariable String key, @RequestParam long seconds) {
        return service.expire(key, seconds);
    }

    @GetMapping("/ttl/{key}")
    public Mono<Long> ttl(@PathVariable String key) {
        return service.ttl(key);
    }
}