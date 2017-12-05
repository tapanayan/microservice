package com.msn.poc.cart.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrencyChangeRequest {
	@JsonProperty("toCurrency")
	private String toCurrency;
	
	@JsonProperty("sessionToken")
	private String sessionToken;

	public String getToCurrency() {
		return toCurrency;
	}

	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	
	

}
