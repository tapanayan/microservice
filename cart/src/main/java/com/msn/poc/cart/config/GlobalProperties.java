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

}
