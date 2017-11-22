package com.msn.poc.user.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRegistrationDetails {
	@JsonProperty("firstName")
	private String firstName;
	@JsonProperty("lastName")
	private String lastName;
	@JsonProperty("middleName")
	private String middleName;
	@JsonProperty("dateOfBirth")
	private String dateOfBirth;

}
