package com.api.gateway.proxy.service;

import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RedisLabService {

    private final ReactiveStringRedisTemplate redisTemplate;

    public RedisLabService(ReactiveStringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Mono<Boolean> save(String key, String value) {
        return redisTemplate.opsForValue().set(key, value);
    }

    public Mono<String> get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Mono<Long> increment(String key) { return redisTemplate.opsForValue().increment(key); }

    public Mono<Boolean> expire(String key, long seconds) {return redisTemplate.expire(key, java.time.Duration.ofSeconds(seconds));}

    public Mono<Long> ttl(String key) { return redisTemplate.getExpire(key).map(java.time.Duration::getSeconds);}
}