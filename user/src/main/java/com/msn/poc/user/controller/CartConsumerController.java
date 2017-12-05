package com.msn.poc.user.controller;

import java.util.Set;

import org.omg.PortableInterceptor.SUCCESSFUL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.msn.poc.user.bm.JWTTokenUtility;
import com.msn.poc.user.bm.UserBusinessManager;
import com.msn.poc.user.exception.DataFormatException;
import com.msn.poc.user.exception.TokenException;
import com.msn.poc.user.feignclient.CartFeignClient;
import com.msn.poc.user.model.ResponseObject;
import com.msn.poc.user.model.UserAuthenticationRequest;
import com.msn.poc.user.model.UserDetail;
import com.msn.poc.user.model.UserRegistrationDetails;
import com.msn.poc.user.repository.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/v1/user/")
@Api(tags = { "cartconsumer" })
public class CartConsumerController extends AbstractRestHandler {
	private static final Logger log = LoggerFactory.getLogger(CartConsumerController.class);
	
	@Autowired
	CartFeignClient cartFeignClient;
	
	@Autowired
	UserRepository userRepository;
	
//	@RequestMapping(value = "getcart", method = RequestMethod.GET)
//	@ResponseStatus(HttpStatus.OK)
//	@ApiOperation(value = "Consumes the cart service via feign client", notes = "Service to service call without hardcoding or directly refering the ip port of the called service")
//	public Set<String> getCart() {
//		Set<String> data = new HashSet<String>();
//		data.add("12345");
//		return cartFeignClient.getAll();
//	}
	
	@CrossOrigin(origins = "http://10.245.231.99:4200")
	@RequestMapping(value="getToken",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.TEXT_PLAIN_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value="Returns a JWT token",notes="Returns a JWT token for the provided user and it should be used for subsequent rest calls.")
	public String generateToken(@RequestBody(required=true) UserDetail userDetails) throws TokenException{
		JWTTokenUtility utility=new JWTTokenUtility();
		try {
			return utility.generateToken(userDetails);
		} catch (TokenException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@CrossOrigin(origins = "http://10.245.231.99:4200")
	@RequestMapping(value="getDetails",method=RequestMethod.POST,consumes=MediaType.TEXT_PLAIN_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value="Returns the decoded token",notes="Returns the decoded token")
	public UserDetail decodeToken(@RequestBody(required=true) String token) throws TokenException{
		JWTTokenUtility utility=new JWTTokenUtility();
		try {
			return utility.getUserDetail(token);
		} catch (TokenException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@CrossOrigin(origins = "http://10.245.231.99:4200")
	@RequestMapping(value="registerUser",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.TEXT_PLAIN_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value="Registers an user having a unique user name",notes="Registers an user having a unique user name.")
	public String registerUser(@RequestBody(required=true) UserRegistrationDetails userRegistrationDetails) {
		if(new UserBusinessManager().registerUser(userRegistrationDetails, userRepository)){
			return "SUCCESSFUL";
		}
		
		return "FAILED";
	}
	
	@CrossOrigin(origins = "http://10.245.231.99:4200")
	@RequestMapping(value = "authenticateUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Authenticate a user having a unique ID and password", notes = "Authenticate a user having a unique ID and password.In return object status will be SUCCESSFUL/FAILURE")
	public ResponseObject authenticateUser(@RequestBody(required = true) UserAuthenticationRequest userAuthenticationRequest) {
		ResponseObject response=new ResponseObject();
		if (new UserBusinessManager().authenticateUser(userAuthenticationRequest.getUserId(), userAuthenticationRequest.getPassword(), userRepository)) {
			UserDetail userDetails=new UserDetail();
			userDetails.setUserName(userAuthenticationRequest.getUserId());
			userDetails.setUserRole("Customer");
			response.setStatus("SUCCESSFUL");
			try {
				response.setText(new JWTTokenUtility().generateToken(userDetails));
			} catch (TokenException e) {
				response.setStatus("FAILURE");
				response.setText("Token generation failed.");
			}
		}
		else {
			response.setStatus("FAILURE");
			response.setText("Invalid User Id or Password.");
		}
		return response;
	}


}
