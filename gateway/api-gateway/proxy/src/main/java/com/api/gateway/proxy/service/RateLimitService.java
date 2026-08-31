package com.api.gateway.proxy.service;

import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@Service
public class RateLimitService {

    private static final long COUNTER_TTL_SECONDS = 120;

    private static final RedisScript<Long> INCREMENT_SCRIPT = RedisScript.of("""
            local current = redis.call('INCR', KEYS[1])
            if current == 1 then
                redis.call('EXPIRE', KEYS[1], ARGV[1])
            end
            return current
            """, Long.class);

    private final ReactiveStringRedisTemplate redisTemplate;

    public RateLimitService(ReactiveStringRedisTemplate redisTemplate) { this.redisTemplate = redisTemplate; }

    public Mono<RateLimitResult> consume(Long clientId, int limit) {
        long epochSecond = Instant.now().getEpochSecond();
        long minuteWindow = epochSecond / 60;
        long retryAfterSeconds = 60 - (epochSecond % 60);
        String key = "rate-limit:client:" + clientId + ":" + minuteWindow;

        return redisTemplate.execute(INCREMENT_SCRIPT, List.of(key), String.valueOf(COUNTER_TTL_SECONDS))
                .next()
                .map(current -> new RateLimitResult(current <= limit, current, limit, Math.max(0, limit - current), retryAfterSeconds));
    }

    public record RateLimitResult(boolean allowed, long current, int limit, long remaining, long retryAfterSeconds) {}
}