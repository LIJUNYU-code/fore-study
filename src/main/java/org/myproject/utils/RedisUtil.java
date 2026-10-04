package org.myproject.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;

    // TODO: 封装常用的Redis操作方法

    public void set(String key, String value) {
        // TODO: redisTemplate.opsForValue().set(key, value)
        throw new UnsupportedOperationException("TODO");
    }

    public void setEx(String key, String value, long timeout, TimeUnit unit) {
        // TODO: redisTemplate.opsForValue().set(key, value, timeout, unit)
        throw new UnsupportedOperationException("TODO");
    }

    public String get(String key) {
        // TODO: return redisTemplate.opsForValue().get(key)
        throw new UnsupportedOperationException("TODO");
    }

    public Boolean delete(String key) {
        // TODO: return redisTemplate.delete(key)
        throw new UnsupportedOperationException("TODO");
    }

    public Long decrement(String key) {
        // TODO: return redisTemplate.opsForValue().decrement(key)
        // 用于秒杀预减库存
        throw new UnsupportedOperationException("TODO");
    }

    public Long increment(String key) {
        // TODO: return redisTemplate.opsForValue().increment(key)
        throw new UnsupportedOperationException("TODO");
    }

    public Boolean hasKey(String key) {
        // TODO: return redisTemplate.hasKey(key)
        throw new UnsupportedOperationException("TODO");
    }

    public Boolean setIfAbsent(String key, String value, long timeout, TimeUnit unit) {
        // TODO: return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit)
        // 用于分布式锁
        throw new UnsupportedOperationException("TODO");
    }
}