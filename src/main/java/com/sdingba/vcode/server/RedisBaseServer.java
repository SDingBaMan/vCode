package com.sdingba.vcode.server;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisBaseServer {
    // 过期时间为60秒
    private static final long EXPIRE_MINUTES = 90;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    public void addValue(String key, String value) {
        valueOperations.set(key, value);
        redisTemplate.expire(key, EXPIRE_MINUTES, TimeUnit.SECONDS);
    }

    public String getValue(String key) {
        return valueOperations.get(key);
    }

    /**
     * 设置超时
     * 
     * @param key
     * @param timeout
     * @param unit
     * @author lianggz
     */
    public void expire(String key, final long timeout, final TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }
}