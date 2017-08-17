package com.sdingba.vcode.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@PropertySource("classpath:redis.properties")
public class RedisConfig {

	@Autowired
	private Environment env;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
		jedisConnectionFactory.setHostName(env.getProperty("vcode.redis.host").trim());
		jedisConnectionFactory.setPort(Integer.parseInt(env.getProperty("vcode.redis.port").trim()));
		jedisConnectionFactory.setPassword(env.getProperty("vcode.redis.password").trim());
		jedisConnectionFactory.setDatabase(Integer.parseInt(env.getProperty("vcode.redis.database").trim()));
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