package com.msn.poc.cart.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseObject {
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("failureReason")
	private String failureReason;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	
	

}
