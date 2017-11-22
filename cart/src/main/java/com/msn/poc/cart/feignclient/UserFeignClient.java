package com.msn.poc.cart.feignclient;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.msn.poc.cart.model.UserDetail;


@FeignClient("user")
public interface UserFeignClient {
	@RequestMapping(value="/v1/user/getToken",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.TEXT_PLAIN_VALUE)	
	public String generateToken(UserDetail userDetails);
	@RequestMapping(value="/v1/user/getDetails",method=RequestMethod.POST,consumes=MediaType.TEXT_PLAIN_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public UserDetail decodeToken(String token);

}
