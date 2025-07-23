package com.narvee.request.dto;

import lombok.Data;

@Data
public class AuthResponse {
	
	private String customerId;
	private String firstName;
	private String lastName;
	private String mobileNumber;
	private String email;
	private String token;
	private String occupation;
	private String location;
	private String loanRequired;

}
