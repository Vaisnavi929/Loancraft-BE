package com.narvee.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanSanctionDTO {
	
	private Long loanSactionId;
	private Double loanRequested;
	private Double amountSanctioned;
	private Double amountDisbursed;
	private Double rateOfInterest;
	private String interestType;
	private int numberOfEmis;
	private Double emiAmount;
	private String repaymentMode;
	private String status;
	private Long addedBy;
	private Long updatedBy;
	private Double loanProcessingCharges;
	private Double documentationCharges;
	private Long loanId;

	
}
