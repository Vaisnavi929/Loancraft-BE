package com.narvee.request.dto;

import java.util.Date;

public interface LoanSanctionDTOS {
	 public Double getlLoanRequested();
	 public Double getAmountSanctioned();
	 public Double getAmountDisbursed();
	 public Double getRateOfInterest();
	 public String getInterestType();
	 public int getNumberOfEmis();
	 public Double getEmiAmount();
	 public String getrepaymentMode();
	 public Date getEmiStartDate();
	 public Date getEmiEndDate();
	 public Date getEmiDueDate();
	 public String getCurrency();
	 public String getPaymentFrequency();
	 public Double getCreditLimit();
	 public Double getCashLimit();
	 public Double getLoanProcessingCharges();
	 public Double getDocumentationCharges();
	 public Double getPenaltyForLatePayment();
	 public String getStatus();

}
