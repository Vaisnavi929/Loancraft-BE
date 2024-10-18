package com.narvee.request.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LedgerDTO {
	
	private Date date;
	private Double loanAmount;
	private Double principleAmount;
	private Double amountPaid;
	private Double balance;
	private Double paid;
	
	private Long payamentId;

}
