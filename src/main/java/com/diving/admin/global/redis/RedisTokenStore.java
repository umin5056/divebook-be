package com.diving.admin.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisTokenStore {

    private static final String REFRESH_PREFIX = "refresh:";
    private static final String BLACKLIST_PREFIX = "blacklist:";

    private final StringRedisTemplate redisTemplate;

    public void saveRefreshToken(String instructorId, String token, long ttlMs) {
        redisTemplate.opsForValue().set(REFRESH_PREFIX + instructorId, token, ttlMs, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String instructorId) {
        return redisTemplate.opsForValue().get(REFRESH_PREFIX + instructorId);
    }

    public void deleteRefreshToken(String instructorId) {
        redisTemplate.delete(REFRESH_PREFIX + instructorId);
    }

    public void blacklistAccessToken(String token, long remainingMs) {
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, "1", remainingMs, TimeUnit.MILLISECONDS);
    }

    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
    }
}
