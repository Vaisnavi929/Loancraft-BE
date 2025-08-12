package com.narvee.request.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LowCheckDepositDTO {

	private Long requestById;
	private Long approvedById;
	private boolean isApproved;
	private Long repaymentId;
	private Double receivableAmount;
	private Double requestedAmount;
	private LocalDate postingDate;
	private String chequeNumber;
	
}
