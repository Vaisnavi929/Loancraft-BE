package com.narvee.request.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckDepositDTO {
	    private Long id;
	    private String chequeNumber;
	    private LocalDate postingDate;
	    private Double amount;
	    private String reason;
	    private Double recievableAmount;
	    private String remarks;
	    private String status;
	    private Long disbursmentId;
		private Long repaymentId;

}