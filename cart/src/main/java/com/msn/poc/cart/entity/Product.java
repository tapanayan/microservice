package com.msn.poc.cart.entity;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="product")
public class Product {
	@Id
	@GeneratedValue( strategy= GenerationType.AUTO )
	@Column(name="PRODUCT_ID")
	private String productId;
	
	@Column(name="PRODUCT_NAME")
	private String productName;
	
	@Column(name="MANUFACTURER")
	private String manufacturer;
	
	@Column(name="CATEGORY")
	private String category;
	
	@Column(name="SELLER_PRODUCT_ID")
	private String sellerProductId;
	
	@Column(name="PRICE")
	private Double price;
	
	@Column(name="SELLER_USER_ID")
	private String sellerUserId;
	
//	@Column(name="SELLER_ID")
//	private String sellerId;
	
	@Column(name="VENDOR")
	private String vendor;
	
	@Column(name="ADDITION_TIME")
	private Timestamp additionTime;
	
	@Column(name="CURRENCY")
	private String currency;
	
	@ManyToOne
	@JoinColumn(name="SELLER_ID")
	private Seller seller;
	
	@OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
	private Cart cart;

	
	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSellerProductId() {
		return sellerProductId;
	}

	public void setSellerProductId(String sellerProductId) {
		this.sellerProductId = sellerProductId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getSellerUserId() {
		return sellerUserId;
	}

	public void setSellerUserId(String sellerUserId) {
		this.sellerUserId = sellerUserId;
	}

//	public String getSellerId() {
//		return sellerId;
//	}
//
//	public void setSellerId(String sellerId) {
//		this.sellerId = sellerId;
//	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Timestamp getAdditionTime() {
		return additionTime;
	}

	public void setAdditionTime(Timestamp additionTime) {
		this.additionTime = additionTime;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	
	public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}
	
	

}
