package com.msn.poc.cart.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RedisConfig {
	
	private static final Class<RedisConfig> clasz = RedisConfig.class;
	private static final Logger log = LoggerFactory.getLogger(clasz);
	
	@Autowired
	private GlobalProperties globalProperties;
	
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jd = new JedisConnectionFactory();
		jd.setUsePool(true);
		jd.setHostName(globalProperties.getRedishost());
		jd.setPort(Integer.parseInt(globalProperties.getRedisport()));
		jd.setPassword(globalProperties.getRedispassword());
		log.info("#### factory::"+jd);
		return jd;
	}
	
	@Bean
	RedisTemplate<String,String> stringRedisTemplate() {
		RedisTemplate<String,String> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		redisTemplate.setDefaultSerializer(new StringRedisSerializer());
		log.info("#### redisTemplate::"+redisTemplate);
		return redisTemplate;
	}
	
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
