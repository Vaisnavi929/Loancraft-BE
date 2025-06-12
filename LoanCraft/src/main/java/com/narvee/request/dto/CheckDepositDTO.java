package com.narvee.request.dto;

import java.time.LocalDate;

import com.narvee.helpers.BounceReason;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckDepositDTO {
	    private Long id;
	    private String chequeNumber;
	    private LocalDate bounceDate;
	    private Double amount;
	    private BounceReason reason;
	    private Double recievableAmount;
	    private String remarks;
	    private Long disbursmentId;

}
