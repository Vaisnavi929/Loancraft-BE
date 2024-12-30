package com.narvee.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationUpdateDto {
	
	private Long loanId;

	private String status;
	
	private String employmentType;
	
	private Double salary;
	
	private Long yearsinEmployment;
	
	private String companyName;
	
	private String companyAddres;
	
	private Long pincode;
	
	private Long customerId;
	
}
