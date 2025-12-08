package com.narvee.request.dto;

import java.util.Date;

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
	private Double rateOfInterest;
	private String interestType;
	private int numberOfEmis;
	private Double emiAmount;
	private String repaymentMode;
	private Date emiStartDate;
	private Date emiEndDate;
	private Date emiDueDate;
	private String status;
	private Long addedBy;
	private Long updatedBy;
	private String paymentFreqmuency;
	private Double loanProcessingCharges;
	private Double documentationCharges;
	
//	//Abhinay
//	@Pattern(regexp = "^[A-Z0-9]+$", message = "Only capital letters and digits allowed")
//	@Size(min = 8, max = 16, message = "Length must be between 8 and 16")
	private String loanAccountNumber;
	private Double originationFee;
	private Double returnOriginationfee;
	private Long loanId;

	
}
