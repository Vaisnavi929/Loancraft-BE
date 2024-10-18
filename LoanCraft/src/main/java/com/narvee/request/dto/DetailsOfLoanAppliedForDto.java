package com.narvee.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailsOfLoanAppliedForDto {
	
	private String purposeOfLoan;
	private Double loanAmountRequired;
	private Long tenure;
	private String repaymentMode;
	private String earlierLoanInBeacon;
	private Long customerId;

}
