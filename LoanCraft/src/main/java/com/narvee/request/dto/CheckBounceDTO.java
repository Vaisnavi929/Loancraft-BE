package com.narvee.request.dto;

import java.time.LocalDate;

import com.narvee.helpers.BounceReason;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckBounceDTO {
	 private Long id;
	    private String chequeNumber;
	    private LocalDate bounceDate;
	    private Double amount;
	    private BounceReason reason;
	    private String remarks;
	    private Double bounceCharges;
	    private Long repaymentId;

}
