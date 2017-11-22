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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msn.poc.cart.bm.CartBusinessManager;
import com.msn.poc.cart.feignclient.UserFeignClient;
import com.msn.poc.cart.model.AddToCartRequest;
import com.msn.poc.cart.model.AddToWishListRequest;
import com.msn.poc.cart.model.Notification;
import com.msn.poc.cart.model.Product;
import com.msn.poc.cart.model.Wishlist;
import com.msn.poc.cart.repository.CartRepository;
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
	UserFeignClient userFeignClient;

	@RequestMapping(value = "getall", method = RequestMethod.POST,consumes=MediaType.TEXT_PLAIN_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Get all the products from the cart", notes = "Get all the products from the cart")
	public Set<Product> getAll(HttpServletRequest request, HttpServletResponse response,@RequestBody(required = true) String sessionToken) {
		Set<Product> data = new CartBusinessManager().getAllProducts(sessionToken,cartRepository);
		return data;
	}
	
	@RequestMapping(value="add",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.TEXT_PLAIN_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Add a product to cart", notes = "Add a product to cart.If usersession is not supplied then a session key will be returned in the format 'SUCCESS-{sessionKey}' which should be used in future for other transactions.")
	public String addToCart(HttpServletRequest request, HttpServletResponse response,@RequestBody(required = true)AddToCartRequest addToCartRequest){
		String session = addToCartRequest.getSessionToken();
		if(session==null){
			log.debug("User is without a sessionKey");
		}else{
			log.debug("User have a sessionKey");
		}
		return SUCCESS+"-"+new CartBusinessManager().addToCart(addToCartRequest,cartRepository,sellerRepository,productRepository,userFeignClient);
		
	}
	
	@RequestMapping(value="remove/{productId}",method=RequestMethod.POST,consumes=MediaType.TEXT_PLAIN_VALUE,produces=MediaType.TEXT_PLAIN_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Removes a product from cart", notes = "Removes a product from cart")
	public String removeFromCart(HttpServletRequest request, HttpServletResponse response,@RequestBody @PathVariable String productId,@RequestBody(required=true) String sessionToken){
		System.out.println("Inside removeFromCart.ID=>"+productId);
		return SUCCESS;
		
	}
	
	@RequestMapping(value = "reprice", method = RequestMethod.POST,consumes=MediaType.TEXT_PLAIN_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Reprices all the products in the cart", notes = "Reprices all the products in the cart and returns the updated products.")
	public Set<Product> reprice(HttpServletRequest request, HttpServletResponse response,@RequestBody(required = true) String sessionToken) {
		Set<Product> data = new HashSet<Product>();
		Product product=new Product();
		product.setName("Dummy");
		product.setProductId("DummyId");
		product.setPrice(100);
		data.add(product);
		return data;
	}
	
	@RequestMapping(value = "wishlist/create/{name}/{userId}", method = RequestMethod.GET,produces=MediaType.TEXT_PLAIN_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Creates wishlist", notes = "Creates a wishlist for a logged in user and returns the wishlist id.")
	public String createWishlist(HttpServletRequest request, HttpServletResponse response,@RequestBody(required = true)@PathVariable String name,@RequestBody(required = true)@PathVariable String userId) {
		System.out.println("Inside create wishlist.");
		return name;
	}
	
	@RequestMapping(value = "wishlist/getAll/{userId}", method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Returns all wishlist of the provided user", notes = "Returns all wishlist of the provided user.")
	public Set<Wishlist> getAllWishList(HttpServletRequest request, HttpServletResponse response,@RequestBody(required = true)@PathVariable String userId) {
		System.out.println("Inside getAllWishList wishlist.");
		Set<Wishlist> data = new HashSet<Wishlist>();
		Wishlist wishlist=new Wishlist();
		wishlist.setName("Dummy Wishlist");
		wishlist.setWishlistId("Dummy");
		wishlist.setUserId(userId);
		data.add(wishlist);
		return data;
	}
	
	@RequestMapping(value = "wishlist/addProduct", method = RequestMethod.POST,produces=MediaType.TEXT_PLAIN_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Adds a product to a wishlist", notes = "Adds a product to a wishlist and returns SUCCESS/FAILURE status.")
	public String addToWishlist(HttpServletRequest request, HttpServletResponse response,@RequestBody(required = true)AddToWishListRequest addToWishListRequest) {
		System.out.println("Inside Add to wishlist.");
		return SUCCESS;
	}
	
	@RequestMapping(value = "wishlist/removeProduct/{wishlistId}/{productId}/{userId}", method = RequestMethod.GET,produces=MediaType.TEXT_PLAIN_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Removes a product from a wishlist", notes = "Removes a product from a wishlist and returns SUCCESS/FAILURE status.")
	public String removeFromWishlist(HttpServletRequest request, HttpServletResponse response,
			@RequestBody(required = true)@PathVariable String wishlistId,
			@RequestBody(required = true)@PathVariable String productId,
			@RequestBody(required = true)@PathVariable String userId) {
		System.out.println("Inside remove from wishlist.");
		System.out.println("Remove product "+productId+" from wishlist "+wishlistId);
		return SUCCESS;
	}
	
	@RequestMapping(value = "wishlist/getAll/{wishlistId}/{userId}", method = RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Retrieves products from a wishlist", notes = "Retrieves products from a wishlist.")
	public List<Product> retrieveFromWishlist(HttpServletRequest request, HttpServletResponse response,
			@RequestBody(required = true)@PathVariable String wishlistId,
			@RequestBody(required = true)@PathVariable String userId) {
		System.out.println("Inside retrieveFromWishlist.");
		System.out.println("Retrieve products from wishlist "+wishlistId);
		List<Product> data = new ArrayList<Product>();
		Product product=new Product();
		product.setName("Dummy");
		product.setProductId("DummyId");
		product.setPrice(100);
		data.add(product);
		return data;
	}
	
	@RequestMapping(value = "wishlist/moveToCart/{wishlistId}/{userId}", method = RequestMethod.GET,produces=MediaType.TEXT_PLAIN_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "Moves products from a wishlist to cart", notes = "Moves products from a wishlist to cart and returns SUCCESS/FAILURE status.")
	public String moveToCart(HttpServletRequest request, HttpServletResponse response,
			@RequestBody(required = true)@PathVariable String wishlistId,
			@RequestBody(required = true)@PathVariable String userId) {
		System.out.println("Inside moveToCart.");
		System.out.println("Move products from wishlist "+wishlistId+" to cart");
		return SUCCESS;
	}
	
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
}
