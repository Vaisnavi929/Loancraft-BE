package com.narvee.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepaymentScheduleRequest {
	private Double annualInterestRate;
	private Double loanAmount;
	private Integer installmentTenor;

}
