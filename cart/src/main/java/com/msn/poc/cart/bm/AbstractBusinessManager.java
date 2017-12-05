package com.msn.poc.cart.bm;

import java.io.IOException;
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
import com.msn.poc.cart.entity.Cart;
import com.msn.poc.cart.entity.Seller;
import com.msn.poc.cart.model.Product;
import com.msn.poc.cart.repository.ProductRepository;
import com.msn.poc.cart.repository.SellerRepository;

public abstract class AbstractBusinessManager {
	protected static final String TYPE_CART = "CART";
	protected static final String TYPE_WISHLIST = "WISHLIST";
	protected final String NOT_LOGGED_IN_USER="Unknown";
	
	public AbstractBusinessManager(){
		//
	}
	
	protected void removeFromRedisCart(String sessionKey,String productId, RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper){
		List<Product> products = getProductsFromRedis(sessionKey, redisTemplate, objectMapper);
		if(products!=null){
			Product identifiedProduct=null;
			for(Product product:products){
				if(productId.equals(product.getProductId())){
					identifiedProduct = product;
					break;
				}
			}
			if(identifiedProduct!=null){
				products.remove(identifiedProduct);
				saveToRedis(sessionKey, redisTemplate, objectMapper, products);
			}
			
		}
		
	}
	
	protected void addToRedisCart(String sessionKey,Product product, RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
		List<Product> products = getProductsFromRedis(sessionKey, redisTemplate, objectMapper);
		if(products == null){
			products = new ArrayList<Product>();
		}
		product.setProductId(String.valueOf(UUID.randomUUID()));

		products.add(product);
		saveToRedis(sessionKey, redisTemplate, objectMapper, products);
	}

	protected void saveToRedis(String sessionKey, RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper,
			List<Product> products) {
		try {
			redisTemplate.opsForValue().set(sessionKey, objectMapper.writeValueAsString(products));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	protected List<Product> getProductsFromRedis(String sessionKey, RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
		List<Product> products = null;
		try {
			if (null != redisTemplate.opsForValue().get(sessionKey)) {
				TypeReference<List<Product>> mapType = new TypeReference<List<Product>>() {
				};
				products = objectMapper.readValue(redisTemplate.opsForValue().get(sessionKey), mapType);
			}			
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return products;
	}
	
	protected Cart getCart(Product product,SellerRepository sellerRepository,ProductRepository productRepository) {
		Seller seller=sellerRepository.findOne(product.getSellerId());
		com.msn.poc.cart.entity.Product productEntity=new com.msn.poc.cart.entity.Product();
		productEntity.setAdditionTime(new Timestamp(new Date().getTime()));
		productEntity.setCategory(product.getCategory());
		productEntity.setCurrency(product.getCurrency());
		productEntity.setManufacturer(product.getManufacturer());
		productEntity.setPrice(product.getPrice());
		productEntity.setProductName(product.getName());
		productEntity.setSeller(seller);
		productEntity.setSellerProductId(product.getSellerProductId());
		productEntity.setSellerUserId(product.getSellerUserId());
		productEntity.setVendor(product.getVendor());
		productEntity = productRepository.save(productEntity);
		Set<com.msn.poc.cart.entity.Product> products=new HashSet<com.msn.poc.cart.entity.Product>();
		products.add(productEntity);
		seller.setProducts(products);
		Cart cart=new Cart();
		cart.setProduct(productEntity);
		productEntity.setCart(cart);
		return cart;
	}

}
