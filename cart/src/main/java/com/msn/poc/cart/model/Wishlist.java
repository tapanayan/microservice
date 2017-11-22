package com.msn.poc.cart.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Wishlist {
	@JsonProperty("name")
	private String name;
	@JsonProperty("userId")
	private String userId;
	@JsonProperty("wishlistId")
	private String wishlistId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getWishlistId() {
		return wishlistId;
	}
	public void setWishlistId(String wishlistId) {
		this.wishlistId = wishlistId;
	}
	
}
