package com.msn.poc.cart.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="seller")
public class Seller {
	@Id
	@Column(name="SELLER_ID")
	private String sellerId;
	
	@Column(name="FIRST_NAME")
	private String firstName;
	
	@Column(name="LAST_NAME")
	private String lastName;
	
	@Column(name="MIDDLE_NAME")
	private String middleName;
	
	@Column(name="CONTACT_NO")
	private String contactNo;
	
	@Column(name="CITY")
	private String city;
	
	@Column(name="STREET_ONE")
	private String streetOne;
	
	@Column(name="STREET_TWO")
	private String streetTwo;
	
	@Column(name="COUNTRY")
	private String country;
	
	@Column(name="PIN")
	private String pin;
	
	@Column(name="STATE")
	private String state;
	
	@OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
	private Set<Product> products;
	
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreetOne() {
		return streetOne;
	}
	public void setStreetOne(String streetOne) {
		this.streetOne = streetOne;
	}
	public String getStreetTwo() {
		return streetTwo;
	}
	public void setStreetTwo(String streetTwo) {
		this.streetTwo = streetTwo;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
	public Set<Product> getProducts() {
		return products;
	}
	
	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	

}
