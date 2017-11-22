package com.msn.poc.cart.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Notification {
	@JsonProperty("message")
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	

}
