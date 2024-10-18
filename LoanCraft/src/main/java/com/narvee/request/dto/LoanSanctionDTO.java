package com.narvee.request.dto;

import java.util.Date;
import java.util.List;

import com.narvee.entity.LoanApplication;
import com.narvee.entity.Payment;

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
	    private Date emiStartDate;
	    private Date emiEndDate;
	    private Date emiDueDate;
	    private String currency;
	    private String paymentFrequency;
	    private Double creditLimit;
	    private Double cashLimit;
	    private Double loanProcessingCharges;
	    private Double documentationCharges;
	    private Double penaltyForLatePayment;
	    private String status;

	    private Long loanId;
	    
	    private Long payamentId;
}
