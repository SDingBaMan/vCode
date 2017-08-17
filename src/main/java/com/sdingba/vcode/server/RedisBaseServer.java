package com.sdingba.vcode.server;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RedisBaseServer {

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;

    @Resource(name = "redisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    public void addValue(String key, String value) {
        valueOperations.set(key, value);
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