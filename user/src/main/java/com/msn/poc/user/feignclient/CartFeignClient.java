package com.msn.poc.user.feignclient;

import java.util.Set;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("cart")
public interface CartFeignClient {
	@RequestMapping(method = RequestMethod.GET, value = "/v1/cart/getall")
	Set<String> getAll();
}
