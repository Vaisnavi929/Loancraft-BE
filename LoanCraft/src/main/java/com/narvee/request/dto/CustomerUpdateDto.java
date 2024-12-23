package com.narvee.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateDto {
	
	private Long customerId;

	private String firstName;

	private String lastName;

	private String mobileNo;

	private String email;
	

}
