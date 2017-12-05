package com.msn.poc.cart.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msn.poc.cart.bm.CartBusinessManager;
import com.msn.poc.cart.bm.CheckoutBusinessManager;
import com.msn.poc.cart.feignclient.UserFeignClient;
import com.msn.poc.cart.model.AddToCartRequest;
import com.msn.poc.cart.model.AddToWishListRequest;
import com.msn.poc.cart.model.CheckoutRequest;
import com.msn.poc.cart.model.CurrencyChangeRequest;
import com.msn.poc.cart.model.Notification;
import com.msn.poc.cart.model.Product;
import com.msn.poc.cart.model.ResponseObject;
import com.msn.poc.cart.repository.CartRepository;
import com.msn.poc.cart.repository.InvoiceRepository;
import com.msn.poc.cart.repository.PaymentRepository;
import com.msn.poc.cart.repository.ProductRepository;
import com.msn.poc.cart.repository.SellerRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/v1/cart/")
@Api(tags = { "cart" })
public class CartController extends AbstractRestHandler {

	private static final String SUCCESS = "SUCCESS";
	private static final Class<CartController> clasz = CartController.class;
	private static final Logger log = LoggerFactory.getLogger(clasz);

	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	CartRepository cartRepository;
	@Autowired
	SellerRepository sellerRepository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	PaymentRepository paymentRepo;
	@Autowired
	InvoiceRepository invoiceRepo;
	@Autowired 
	RedisTemplate<String, String> redisTemplate; 

	
	@Autowired
	UserFeignClient userFeignClient;

	@CrossOrigin(origins = "http://10.245.231.99:4200")
	@RequestMapping(value = "getall", method = RequestMethod.POST,consumes=MediaType.TEXT_PLAIN_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Get all the products from the cart", notes = "Get all the products from the cart")
	public List<Product> getAll(HttpServletRequest request, HttpServletResponse response,@RequestBody(required = true) String sessionToken) {
		return new CartBusinessManager().getAllProducts(sessionToken,cartRepository,userFeignClient,redisTemplate,objectMapper);
	}
	
	@CrossOrigin(origins = "http://10.245.231.99:4200")
	@RequestMapping(value="add",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.TEXT_PLAIN_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Add a product to cart", notes = "Add a product to cart.If usersession is not supplied then a session key will be returned in the format 'SUCCESS-{sessionKey}' which should be used in future for other transactions.")
	public String addToCart(HttpServletRequest request, HttpServletResponse response,@RequestBody(required = true)AddToCartRequest addToCartRequest){
		System.out.println("Inside Add to cart.");
		String session = addToCartRequest.getSessionToken();
		if(session==null){
			log.debug("User is without a sessionKey");
		}else{
			log.debug("User have a sessionKey");
		}
		return SUCCESS+"-"+new CartBusinessManager().addToCart(addToCartRequest,cartRepository,sellerRepository,
				productRepository,userFeignClient,false,redisTemplate,objectMapper);
		
	}
	
	@CrossOrigin(origins = "http://10.245.231.99:4200")
	@RequestMapping(value="remove/{productId}",method=RequestMethod.POST,consumes=MediaType.TEXT_PLAIN_VALUE,produces=MediaType.TEXT_PLAIN_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Removes a product from cart", notes = "Removes a product from cart")
	public String removeFromCart(HttpServletRequest request, HttpServletResponse response,@RequestBody(required = true) @PathVariable String productId,@RequestBody(required=true) String sessionToken){
		new CartBusinessManager().removeFromCart(productId,sessionToken,productRepository,cartRepository,
				userFeignClient,redisTemplate,objectMapper);
		return SUCCESS;
		
	}
	
	@CrossOrigin(origins = "http://10.245.231.99:4200")
	@RequestMapping(value = "reprice", method = RequestMethod.POST,consumes=MediaType.TEXT_PLAIN_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Reprices all the products in the cart", notes = "Reprices all the products in the cart and returns the updated products.")
	public Set<Product> reprice(HttpServletRequest request, HttpServletResponse response,@RequestBody(required = true) String sessionToken) {
		Set<Product> data = new HashSet<Product>();
		Product product=new Product();
		product.setName("Dummy");
		product.setPrice(100);
		data.add(product);
		return data;
	}
	
	
	
	@CrossOrigin(origins = "http://10.245.231.99:4200")
	@RequestMapping(value = "wishlist/getAll", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Returns all products of the wishlist of the provided user", notes = "Returns all wishlist of the provided user.")
	public List<Product> getAllWishList(@RequestBody(required = true) String sessionToken) {
		return new CartBusinessManager().getAllWishlistProducts(sessionToken,cartRepository,userFeignClient);
	}
	
	@CrossOrigin(origins = "http://10.245.231.99:4200")
	@RequestMapping(value = "wishlist/addProduct", method = RequestMethod.POST,produces=MediaType.TEXT_PLAIN_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Adds a product to a wishlist", notes = "Adds a product to a wishlist and returns SUCCESS/FAILURE status.")
	public String addToWishlist(HttpServletRequest request, HttpServletResponse response,@RequestBody(required = true)AddToWishListRequest addToWishListRequest) {
		System.out.println("Inside Add to wishlist.");
		new CartBusinessManager().addToWishlist(addToWishListRequest.getSessionToken(), addToWishListRequest.getProduct(), 
				cartRepository, sellerRepository, productRepository, userFeignClient);
		return SUCCESS;
	}
	
	@CrossOrigin(origins = "http://10.245.231.99:4200")
	@RequestMapping(value = "wishlist/removeProduct/{productId}", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Removes a product from a wishlist", notes = "Removes a product from a wishlist and returns SUCCESS/FAILURE status.")
	public ResponseObject removeFromWishlist(
			@RequestBody(required = true)@PathVariable String productId,
			@RequestBody(required=true) String sessionToken) {
		System.out.println("Inside remove from wishlist.");
		return new CartBusinessManager().removeFromWishlist(productId,sessionToken,productRepository,userFeignClient);
	}
	
	
	@CrossOrigin(origins = "http://10.245.231.99:4200")
	@RequestMapping(value = "wishlist/moveToCart/{productId}", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Moves product from a wishlist to cart", notes = "Moves products from a wishlist to cart and returns SUCCESS/FAILURE status.")
	public ResponseObject moveToCart(
			@RequestBody(required = true)@PathVariable String productId,
			@RequestBody(required = true)String sessionToken) {
		System.out.println("Inside moveToCart.");
		return new CartBusinessManager().moveToCart(productId,sessionToken,productRepository,userFeignClient);
	}
	
	@CrossOrigin(origins = "http://10.245.231.99:4200")
	@RequestMapping(value = "moveToWishlist/{productId}", method = RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Moves product from a wishlist to cart", notes = "Moves products from a cart to wishlist and returns SUCCESS/FAILURE status.")
	public ResponseObject moveToWishlist(
			@RequestBody(required = true)@PathVariable String productId,
			@RequestBody(required = true)String sessionToken) {
		System.out.println("Inside moveToWishlist.");
		return new CartBusinessManager().moveToWishlist(productId,sessionToken,productRepository,userFeignClient,cartRepository);
	}
	
	@CrossOrigin(origins = "http://10.245.231.99:4200")
	@RequestMapping(value = "getNotifications/{userId}", method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "This operation will return the list of notifications to be displayed to the user.", notes = "This operation will return the list of notifications to be displayed to the user.")
	public List<Notification> getNotifications(HttpServletRequest request, HttpServletResponse response,
			@RequestBody(required = true)@PathVariable String userId) {
		System.out.println("Inside getNotifications.");
		List<Notification> notifications=new ArrayList<Notification>();
		Notification notification=new Notification();
		notification.setMessage("Your cart is 2 days old.");
		notifications.add(notification);
		return notifications;
	}
	
	@RequestMapping(value = "changeCurrency", method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Changes currency", notes = "Changes currency and returns SUCCESS/FAILURE status.")
	public ResponseObject changeCurrency(
			@RequestBody(required = true)CurrencyChangeRequest currencyChangeRequest) {
		System.out.println("Inside changeCurrency.");
		return new CartBusinessManager().changeCurrency(currencyChangeRequest,cartRepository,userFeignClient);
	}
	
	@RequestMapping(value = "checkout", method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Cart checkout", notes = "Cart checkout and returns SUCCESS/FAILURE status.")
	public ResponseObject checkout(
			@RequestBody(required = true)CheckoutRequest checkoutRequest) {
		System.out.println("Inside checkout.");
		return new CheckoutBusinessManager().checkout(checkoutRequest,cartRepository,productRepository,userFeignClient,
				paymentRepo,invoiceRepo,redisTemplate,objectMapper,sellerRepository);
	}
}
