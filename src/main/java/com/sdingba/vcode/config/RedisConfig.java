package com.sdingba.vcode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;

@Configuration
@PropertySource("classpath:vcode.properties")
public class RedisConfig {

    @Resource
    private VcodeConfig vcodeConfig;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(vcodeConfig.getHost().trim());
        jedisConnectionFactory.setPort(Integer.parseInt(vcodeConfig.getPort().trim()));
        jedisConnectionFactory.setPassword(vcodeConfig.getPassword().trim());
		jedisConnectionFactory.setDatabase(Integer.parseInt(vcodeConfig.getDatabase().trim()));
		jedisConnectionFactory.setUsePool(true);
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}