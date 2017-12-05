package com.msn.poc.cart.bm;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.msn.poc.cart.entity.Cart;
import com.msn.poc.cart.entity.Seller;
import com.msn.poc.cart.feignclient.UserFeignClient;
import com.msn.poc.cart.model.AddToCartRequest;
import com.msn.poc.cart.model.CheckoutRequest;
import com.msn.poc.cart.model.CurrencyChangeRequest;
import com.msn.poc.cart.model.Product;
import com.msn.poc.cart.model.ResponseObject;
import com.msn.poc.cart.model.UserDetail;
import com.msn.poc.cart.repository.CartRepository;
import com.msn.poc.cart.repository.ProductRepository;
import com.msn.poc.cart.repository.SellerRepository;
import com.msn.poc.cart.utility.Utility;

public class CartBusinessManager extends AbstractBusinessManager {
	
	private final String CURRENCY_CONVERSION_URL="https://v3.exchangerate-api.com/pair/dedc2639d50cee2bf2765129/";
	
	public String addToCart(AddToCartRequest addToCartRequest,CartRepository cartRepository,SellerRepository sellerRepository,ProductRepository productRepository,
			UserFeignClient userFeignClient,boolean wishlist, RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper){
		String sessionKey = saveCart(addToCartRequest.getSessionToken(), addToCartRequest.getProduct(), 
				cartRepository, sellerRepository, productRepository, userFeignClient, false,redisTemplate,objectMapper);		
		return sessionKey;
	}
	
	public void addToWishlist(String sessionKey, Product product, CartRepository cartRepository,
			SellerRepository sellerRepository, ProductRepository productRepository, UserFeignClient userFeignClient) {
		if (sessionKey != null) {
			UserDetail detail = userFeignClient.decodeToken(sessionKey);
			if (!NOT_LOGGED_IN_USER.equals(detail.getUserName())) {
				saveCart(sessionKey, product, cartRepository, sellerRepository, productRepository, userFeignClient,
						true,null,null);
			}else{
				System.out.println("Not a logged in user.Cannot add to wishlist");
			}
		}else{
			System.out.println("SessionKey is null!Cannot add to wishlist");
		}

	}
	
	private String saveCart(String sessionKey,Product product,CartRepository cartRepository,SellerRepository sellerRepository,ProductRepository productRepository,
			UserFeignClient userFeignClient,boolean wishlist, RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper){
		UserDetail detail=null;
		if(sessionKey==null){
			detail=new UserDetail();
			detail.setUserName(NOT_LOGGED_IN_USER);
			detail.setUserRole("Viewer");
			sessionKey = userFeignClient.generateToken(detail);
		}else{
			detail = userFeignClient.decodeToken(sessionKey);
		}
		
		if(!NOT_LOGGED_IN_USER.equals(detail.getUserName())){
			Cart cart=getCart(product,sellerRepository,productRepository);
			cart.setToken(sessionKey);
			cart.setUserId(detail.getUserName());
			if(wishlist){
				cart.setType(TYPE_WISHLIST);
				System.out.println("Inside save cart to add to wishlist.");
			}else{
				cart.setType(TYPE_CART);
				System.out.println("Inside save cart to add to cart.");
			}
			cartRepository.save(cart);
		}else{
			addToRedisCart(sessionKey, product, redisTemplate, objectMapper);
		}
			
		return sessionKey;
	}
	
	
	
	public List<Product> getAllProducts(String sessionToken, CartRepository cartRepository,UserFeignClient userFeignClient, RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
		UserDetail detail=userFeignClient.decodeToken(sessionToken);
		Set<com.msn.poc.cart.entity.Product> products=new HashSet<com.msn.poc.cart.entity.Product>();
		List<Product> productModels;
		if(NOT_LOGGED_IN_USER.equals(detail.getUserName())){
			//cache implementation
//			carts = cartRepository.findByTokenAndType(sessionToken,TYPE_CART);
			productModels = getProductsFromRedis(sessionToken, redisTemplate, objectMapper);
			if(productModels == null){
				productModels=new ArrayList<Product>();
			}
		}else{
			List<Cart> carts = cartRepository.findByUserIdAndType(detail.getUserName(), TYPE_CART);
			for(Cart cart:carts){
				if (!cart.getProduct().isAlreadySold()) {
					products.add(cart.getProduct());
				}			
			}
			productModels = getConvertedToModels(products);
		}
		
		return productModels;
	}
	
	private List<Product> getConvertedToModels(Set<com.msn.poc.cart.entity.Product> products) {
		List<Product> productModels=new ArrayList<Product>();
		for(com.msn.poc.cart.entity.Product product:products){
			Product prod=new Product();
			prod.setCategory(product.getCategory());
			prod.setCurrency(product.getCurrency());
			prod.setManufacturer(product.getManufacturer());
			prod.setName(product.getProductName());
			prod.setPrice(product.getPrice());
			prod.setSellerId(product.getSeller().getSellerId());
			prod.setSellerProductId(product.getSellerProductId());
			prod.setSellerUserId(product.getSellerUserId());
			prod.setVendor(product.getVendor());
			prod.setProductId(String.valueOf(product.getProductId()));
			productModels.add(prod);
		}
		return productModels;
	}
	public void removeFromCart(String productId, String sessionToken,ProductRepository productRepository, 
			CartRepository cartRepository, UserFeignClient userFeignClient, RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
		UserDetail detail=userFeignClient.decodeToken(sessionToken);		
		if(NOT_LOGGED_IN_USER.equals(detail.getUserName())){
			//cache implementation
			removeFromRedisCart(sessionToken, productId, redisTemplate, objectMapper);
		}else{			
			com.msn.poc.cart.entity.Product product = productRepository.findOne(Integer.valueOf(productId));
			productRepository.delete(Integer.valueOf(productId));
		}
		
	}

	public ResponseObject removeFromWishlist(String productId, String sessionToken, ProductRepository productRepository,
			UserFeignClient userFeignClient) {
		ResponseObject responseObject = new ResponseObject();
		UserDetail detail = userFeignClient.decodeToken(sessionToken);
		if (NOT_LOGGED_IN_USER.equals(detail.getUserName())) {
			responseObject.setStatus("FAILURE");
			responseObject.setFailureReason("User must log in");
		} else {
			com.msn.poc.cart.entity.Product product = productRepository.findOne(Integer.valueOf(productId));
			if (product==null || TYPE_CART.equals(product.getCart().getType())) {
				responseObject.setStatus("FAILURE");
				responseObject.setFailureReason("Product not available in wishlist");
			} else if(product.getCart().getUserId()==null || !product.getCart().getUserId().equals(detail.getUserName())){
				responseObject.setStatus("FAILURE");
				responseObject.setFailureReason("Product not available in wishlist");				
			}else {
				productRepository.delete(Integer.valueOf(productId));
				responseObject.setStatus("SUCCESS");
			}
		}
		return responseObject;

	}

	public List<Product> getAllWishlistProducts(String sessionToken, CartRepository cartRepository,
			UserFeignClient userFeignClient) {
		System.out.println("Inside getAllWishlistProducts");
		List<Product> productModels=new ArrayList<Product>();
		UserDetail detail = userFeignClient.decodeToken(sessionToken);
		System.out.println("Username:"+detail.getUserName());
		if (!NOT_LOGGED_IN_USER.equals(detail.getUserName())) {
			Set<com.msn.poc.cart.entity.Product> products=new HashSet<com.msn.poc.cart.entity.Product>();
			List<Cart> carts = cartRepository.findByUserIdAndType(detail.getUserName(),TYPE_WISHLIST);
			for(Cart cart:carts){
				products.add(cart.getProduct());			
			}
			productModels = getConvertedToModels(products);
		}
		return productModels;
	}

	public ResponseObject moveToCart(String productId, String sessionToken, ProductRepository productRepository,
			UserFeignClient userFeignClient) {
		ResponseObject responseObject = new ResponseObject();
		UserDetail detail = userFeignClient.decodeToken(sessionToken);
		if (NOT_LOGGED_IN_USER.equals(detail.getUserName())) {
			responseObject.setStatus("FAILURE");
			responseObject.setFailureReason("User must log in");
		} else {
			com.msn.poc.cart.entity.Product product = productRepository.findOne(Integer.valueOf(productId));
			if (product==null || TYPE_CART.equals(product.getCart().getType())) {
				responseObject.setStatus("FAILURE");
				responseObject.setFailureReason("Product not available in wishlist");
			} else if(product.getCart().getUserId()==null || !product.getCart().getUserId().equals(detail.getUserName())){
				responseObject.setStatus("FAILURE");
				responseObject.setFailureReason("Product not available in wishlist");				
			}else {
				product.getCart().setType(TYPE_CART);
				productRepository.save(product);
				responseObject.setStatus("SUCCESS");
			}
		}
		return responseObject;
	}

	public ResponseObject moveToWishlist(String productId, String sessionToken, ProductRepository productRepository,
			UserFeignClient userFeignClient,CartRepository cartRepository) {
		ResponseObject responseObject = new ResponseObject();
		UserDetail detail = userFeignClient.decodeToken(sessionToken);
		if (NOT_LOGGED_IN_USER.equals(detail.getUserName())) {
			responseObject.setStatus("FAILURE");
			responseObject.setFailureReason("User must log in");
		} else {
			com.msn.poc.cart.entity.Product product = productRepository.findOne(Integer.valueOf(productId));
			if (product==null || TYPE_WISHLIST.equals(product.getCart().getType())) {
				responseObject.setStatus("FAILURE");
				responseObject.setFailureReason("Product not available in cart");
			} else if(product.getCart().getUserId()!=null && !product.getCart().getUserId().equals(detail.getUserName())){
				responseObject.setStatus("FAILURE");
				responseObject.setFailureReason("Product not available in cart");				
			}else {
				product.getCart().setType(TYPE_WISHLIST);
				product.getCart().setUserId(detail.getUserName());
				productRepository.save(product);
				List<com.msn.poc.cart.entity.Product> products = productRepository.findBySellerProductId(product.getSellerProductId());
				if(products!=null && !products.isEmpty()){
					for(com.msn.poc.cart.entity.Product prod:products){
						if(TYPE_CART.equals(prod.getCart().getType()) && !prod.isAlreadySold()){
							cartRepository.delete(prod.getCart());
						}
					}
				}
				responseObject.setStatus("SUCCESS");
			}
		}
		return responseObject;
	}

	public ResponseObject changeCurrency(CurrencyChangeRequest currencyChangeRequest, CartRepository cartRepository,
			UserFeignClient userFeignClient) {
		UserDetail detail = userFeignClient.decodeToken(currencyChangeRequest.getSessionToken());
		List<Cart> carts;
		if (NOT_LOGGED_IN_USER.equals(detail.getUserName())) {
			carts = cartRepository.findByToken(currencyChangeRequest.getSessionToken());
		} else {
			carts = cartRepository.findByUserId(detail.getUserName());
		}
		ResponseObject object = new ResponseObject();
		if(currencyChangeRequest.getToCurrency()==null){
			object.setStatus("FAILURE");
			object.setFailureReason("No to currency supplied.");
		}
		if(carts==null || carts.size()==0){
			object.setStatus("SUCCESS");
		}else{
			String currentCurrency=carts.get(0).getProduct().getCurrency();
			try {
				String result = Utility.makeSecureGetCall(CURRENCY_CONVERSION_URL+currentCurrency+"/"+currencyChangeRequest.getToCurrency());
				JsonParser jp = new JsonParser();
				JsonElement root = jp.parse(result);
				JsonObject jsonobj = root.getAsJsonObject();

				if ("success".equalsIgnoreCase(jsonobj.get("result").getAsString())) {
					// Accessing object
					String rate = jsonobj.get("rate").getAsString();
					System.out.println(rate);
					for(Cart cart:carts){
						if (!cart.getProduct().isAlreadySold()) {
							cart.getProduct().setPrice(cart.getProduct().getPrice() * Double.valueOf(rate));
							cart.getProduct().setCurrency(currencyChangeRequest.getToCurrency());
							cartRepository.save(cart);
						}
					}
					object.setStatus("SUCCESS");
					object.setFailureReason(rate);
				}else{
					object.setStatus("FAILURE");
					object.setFailureReason(jsonobj.get("error").getAsString());
				}
			} catch (IOException e) {
				e.printStackTrace();
				object.setStatus("FAILURE");
				object.setFailureReason("Service call failed.");
			}
		}
		return object;
	}
	

}
