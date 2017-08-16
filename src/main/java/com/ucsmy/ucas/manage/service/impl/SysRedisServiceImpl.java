package com.ucsmy.ucas.manage.service.impl;

import com.ucsmy.ucas.manage.service.SysCacheService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
@Service
public class SysRedisServiceImpl implements SysCacheService {
    @Resource(name = "stringRedisTemplate")
    private RedisTemplate<Object, Object> stringRedisTemplate;

    @Resource(name = "keyStringRedisTemplate")
    private ValueOperations<String, Object> keyStringOpsForValue;

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> stringOpsForValue;

    @Override
    public void set(String key, Object value, long invalid) {
        if (value instanceof String)
            stringOpsForValue.set(key, (String)value, invalid, TimeUnit.SECONDS);
        else
            keyStringOpsForValue.set(key, value, invalid, TimeUnit.SECONDS);
        stringRedisTemplate.opsForHash().put("crl", "time", "1782");
        stringRedisTemplate.opsForHash().put("crl", "fileStr", "1782dfdf");
    }

    @Override
    public void set(String key, Object value) {
        if (value instanceof String) {
            stringOpsForValue.set(key, (String) value);
        } else {
            keyStringOpsForValue.set(key, value);
        }
    }

    @Override
    public Object get(String key) {
        return keyStringOpsForValue.get(key);
    }

    @Override
    public String getString(String key) {
        return stringOpsForValue.get(key);
    }

    @Override
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    @Override
    public boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    @Override
    public long getExpire(String key) {
        return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public Long increment(String key, long delta) {
        return stringOpsForValue.increment(key, delta);
    }

    @Override
    public Long increment(String key, long delta, long timeout, TimeUnit unit) {
        Long value = increment(key, delta);
        // 检查过期时间
        long expire = getExpire(key);
        if (expire < 0) {
            // 添加过期时间
            stringRedisTemplate.expire(key, timeout, unit);
        }
        return value;
    }
}
