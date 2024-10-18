package com.narvee.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExistingLoanDetailsDto {
	
	private String nameOfInstitution;
	private String purposeOfLoan;
	private Double loanAmount;
	private Double tenureOfLoan;
	private Long monthlyInstallment;
	private Double currentOutstanding;
	private Long balanceTenure; 


}
