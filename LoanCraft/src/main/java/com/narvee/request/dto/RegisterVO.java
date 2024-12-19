package com.narvee.request.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterVO {
	
	private String firstName;
	private String lastname;
	private String mobileNumber;
	private String email;
	private String password;
}
