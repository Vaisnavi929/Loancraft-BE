package com.narvee.request.dto;

import java.time.LocalDateTime;
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
	private LocalDateTime startDate;

}
