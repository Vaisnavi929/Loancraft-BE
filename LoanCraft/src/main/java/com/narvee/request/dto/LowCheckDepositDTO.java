package com.narvee.request.dto;

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
	private Double recievableMount;
	private Double requestedAmount;
}
