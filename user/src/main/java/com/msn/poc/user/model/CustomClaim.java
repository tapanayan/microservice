package com.msn.poc.user.model;

import java.util.Date;

public class CustomClaim  {
    private String role;            //user role
    private String userId;          //logged in user ID
    private Date   exp;//expiration
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getExp() {
		return exp;
	}
	public void setExp(Date exp) {
		this.exp = exp;
	}

   

}
