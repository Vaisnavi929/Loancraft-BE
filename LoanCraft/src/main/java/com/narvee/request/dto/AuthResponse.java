package com.narvee.request.dto;

import lombok.Data;

@Data
public class AuthResponse {
	
	private String firstName;
	private String lastName;
	private String mobileNo;
	private String email;
	private String token;

}
