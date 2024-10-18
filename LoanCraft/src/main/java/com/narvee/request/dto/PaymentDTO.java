package com.narvee.request.dto;

import java.util.Date;

import com.narvee.entity.LoanSanction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
	
	private Long payamentId;
	private String vocherNumber;
	private Date paymentDate;
	private String branchName;
	private String perticulars;
	private Double amount;
	private String description;
	private String bankDetails;
	private Long loanSactionId;
	
	
}
