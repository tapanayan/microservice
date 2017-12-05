package com.msn.poc.cart.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckoutRequest {
	@JsonProperty("sessionToken")
	private String sessionToken;
	
	@JsonProperty("saveCard")
	private boolean saveCard;
	
	@JsonProperty("cardNumber")
	private String cardNumber;
	
	@JsonProperty("nameOnCard")
	private String nameOnCard;
	
	@JsonProperty("category")
	private String category;
	
	@JsonProperty("bankName")
	private String bankName;
	
	@JsonProperty("bankIfsc")
	private String bankIfsc;
	
	@JsonProperty("email")
	private String email;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public boolean isSaveCard() {
		return saveCard;
	}

	public void setSaveCard(boolean saveCard) {
		this.saveCard = saveCard;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getNameOnCard() {
		return nameOnCard;
	}

	public void setNameOnCard(String nameOnCard) {
		this.nameOnCard = nameOnCard;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankIfsc() {
		return bankIfsc;
	}

	public void setBankIfsc(String bankIfsc) {
		this.bankIfsc = bankIfsc;
	}

}
