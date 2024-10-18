package com.narvee.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncomeDetailsDto {
	
	private Double householdIncome;
	private Double householdExpenditure;
	private Double applicantIncome;
	private Double otherIncome1;
	private Double otherIncome2;
	private Double otherIncome3;
	private Double totalIncome;
	private Double netIncome;

}
