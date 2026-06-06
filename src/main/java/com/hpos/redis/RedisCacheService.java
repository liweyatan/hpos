package com.hpos.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheService {

    private static final Logger log = LoggerFactory.getLogger(RedisCacheService.class);

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    public boolean tryLock(String key, long ttlSeconds) {
        if (redisTemplate == null) return true;
        try {
            Boolean locked = redisTemplate.opsForValue().setIfAbsent(key, "1", ttlSeconds, TimeUnit.SECONDS);
            return Boolean.TRUE.equals(locked);
        } catch (Exception e) {
            log.warn("Redis 锁获取异常，放行（回退到数据库）: {}", e.getMessage());
            return true;
        }
    }

    public void unlock(String key) {
        if (redisTemplate == null) return;
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.warn("Redis 解锁异常: {}", e.getMessage());
        }
    }
}
