package com.msn.poc.cart.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties // no prefix, find root level values.
public class GlobalProperties {

	private static final Class<GlobalProperties> clasz = GlobalProperties.class;
	private static final Logger log = LoggerFactory.getLogger(clasz);

	private String redishost;
	private String redisport;
	private String redispassword;

	public String getRedishost() {
		return redishost;
	}

	public void setRedishost(String redishost) {
		log.info("redishost::"+redishost);
		this.redishost = redishost;
	}

	public String getRedisport() {
		return redisport;
	}

	public void setRedisport(String redisport) {
		log.info("redisport::"+redisport);
		this.redisport = redisport;
	}

	public String getRedispassword() {
		return redispassword;
	}

	public void setRedispassword(String redispassword) {
		log.info("redispassword::"+redispassword);
		this.redispassword = redispassword;
	}
}
