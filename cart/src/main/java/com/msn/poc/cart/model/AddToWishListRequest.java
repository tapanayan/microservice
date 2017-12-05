package com.msn.poc.cart.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddToWishListRequest {
//	@JsonProperty("wishlistId")
//	private String wishlistId;
	@JsonProperty("product")
	private Product product;
	@JsonProperty("sessionToken")
	private String sessionToken;
public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	//	public String getWishlistId() {
//		return wishlistId;
//	}
//	public void setWishlistId(String wishlistId) {
//		this.wishlistId = wishlistId;
//	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	

}
