package com.msn.poc.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseObject {
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("text")
	private String text;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	

}
