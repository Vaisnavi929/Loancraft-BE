package com.narvee.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDTOS {

	private String customerId;
	private String email;
	private String accountNumber;
	private String fullName;
	private String mobileNumber;
	private String loanRequired;
	private String location;


}
