package com.msn.poc.cart.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Product {

	@JsonProperty("name")
	private String name;
	@JsonProperty("productId")
	private String productId;
	@JsonProperty("price")
	private double price;
	@JsonProperty("category")
	private String category;
	@JsonProperty("manufacturer")
	private String manufacturer;
	@JsonProperty("sellerProductId")
	private String sellerProductId;
	@JsonProperty("sellerId")
	private String sellerId;
	@JsonProperty("vendor")
	private String vendor;
	@JsonProperty("sellerUserId")
	private String sellerUserId;
	@JsonProperty("currency")
	private String currency;
	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getSellerProductId() {
		return sellerProductId;
	}
	public void setSellerProductId(String sellerProductId) {
		this.sellerProductId = sellerProductId;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public String getSellerUserId() {
		return sellerUserId;
	}
	public void setSellerUserId(String sellerUserId) {
		this.sellerUserId = sellerUserId;
	}
	
	

}
