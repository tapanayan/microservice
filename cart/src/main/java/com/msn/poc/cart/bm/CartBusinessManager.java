package com.msn.poc.cart.bm;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msn.poc.cart.entity.Cart;
import com.msn.poc.cart.entity.Seller;
import com.msn.poc.cart.feignclient.UserFeignClient;
import com.msn.poc.cart.model.AddToCartRequest;
import com.msn.poc.cart.model.Product;
import com.msn.poc.cart.model.UserDetail;
import com.msn.poc.cart.repository.CartRepository;
import com.msn.poc.cart.repository.ProductRepository;
import com.msn.poc.cart.repository.SellerRepository;
import com.msn.poc.cart.utility.Utility;

public class CartBusinessManager {
	private final String userServiceUrl="http://localhost:50121/v1/user/";
	private final String decodingPath="getDetails";
	private final String encodingPath="getToken";
	private final String NOT_LOGGED_IN_USER="Unknown";
	
	public String addToCart(AddToCartRequest addToCartRequest,CartRepository cartRepository,SellerRepository sellerRepository,ProductRepository productRepository,
			UserFeignClient userFeignClient){
		String sessionKey=addToCartRequest.getSessionToken();
		UserDetail detail=null;
		if(sessionKey==null){
			detail=new UserDetail();
			detail.setUserName(NOT_LOGGED_IN_USER);
			detail.setUserRole("Viewer");
//			String userDetail=Utility.objectToJSON(detail);
//			try {
//				sessionKey = Utility.makePostCall(userDetail, userServiceUrl+encodingPath,"application/json","text/plain");		
//				sessionKey = userFeignClient.generateToken(detail);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			sessionKey = userFeignClient.generateToken(detail);
		}else{
//			detail = getDecodedUserDetail(sessionKey);
			detail = userFeignClient.decodeToken(sessionKey);
		}
		Cart cart=getCart(addToCartRequest.getProduct(),sellerRepository,productRepository);
		cart.setToken(sessionKey);
		if(!NOT_LOGGED_IN_USER.equals(detail.getUserName())){
			cart.setUserId(detail.getUserName());
		}
		cartRepository.save(cart);
		return sessionKey;
	}
	private UserDetail getDecodedUserDetail(String sessionKey) {
		UserDetail detail = null;
		try {
			String userInfo = Utility.makePostCall(sessionKey, userServiceUrl+decodingPath, "text/plain", "application/json");
			detail=new ObjectMapper().readValue(userInfo, UserDetail.class);				
		} catch (IOException e) {
			e.printStackTrace();
		}
		return detail;
	}
	private Cart getCart(Product product,SellerRepository sellerRepository,ProductRepository productRepository) {
		if(sellerRepository==null){
			System.out.println("Seller repository is null");
		}
		if(product == null){
			System.out.println("Product is null");
		}
		Seller seller=sellerRepository.findOne(product.getSellerId());
		com.msn.poc.cart.entity.Product productEntity=new com.msn.poc.cart.entity.Product();
		productEntity.setAdditionTime(new Timestamp(new Date().getTime()));
		productEntity.setCategory(product.getCategory());
		productEntity.setCurrency(product.getCurrency());
		productEntity.setManufacturer(product.getManufacturer());
		productEntity.setPrice(product.getPrice());
//		productEntity.setProductId(String.valueOf(System.currentTimeMillis()));
//		productEntity.setProductId("xyz");
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
	
	
	public Set<Product> getAllProducts(String sessionToken, CartRepository cartRepository) {
		Set<com.msn.poc.cart.entity.Product> products=new HashSet<com.msn.poc.cart.entity.Product>();
		List<Cart> carts = cartRepository.findByToken(sessionToken);
		for(Cart cart:carts){
			products.add(cart.getProduct());			
		}
		Set<Product> productModels = getConvertedToModels(products);
		return productModels;
	}
	
	private Set<Product> getConvertedToModels(Set<com.msn.poc.cart.entity.Product> products) {
		Set<Product> productModels=new HashSet<Product>();
		for(com.msn.poc.cart.entity.Product product:products){
			Product prod=new Product();
			prod.setCategory(product.getCategory());
			prod.setCurrency(product.getCurrency());
			prod.setManufacturer(product.getManufacturer());
			prod.setName(product.getProductName());
			prod.setPrice(product.getPrice());
			prod.setProductId(product.getProductId());
			prod.setSellerId(product.getSeller().getSellerId());
			prod.setSellerProductId(product.getSellerProductId());
			prod.setSellerUserId(product.getSellerUserId());
			prod.setVendor(product.getVendor());
			productModels.add(prod);
		}
		return productModels;
	}

}
