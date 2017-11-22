package com.msn.poc.cart.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDetail {
	@JsonProperty("userName")
	private String userName;
	@JsonProperty("userRole")
	private String userRole;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	

}
