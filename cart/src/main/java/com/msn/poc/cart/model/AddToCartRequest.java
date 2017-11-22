package com.msn.poc.cart.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddToCartRequest {
	@JsonProperty("sessionToken")
	private String sessionToken;
	@JsonProperty("product")
	private Product product;
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public String getSessionToken() {
		return sessionToken;
	}
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	

}
