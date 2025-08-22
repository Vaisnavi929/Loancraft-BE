package com.narvee.request.dto;

import java.time.LocalDate;

public interface FetchLowCheckDepositDTO {

	public Long getId();

	public Double getReceivableAmount();
  
	public Double getRequestedAmount();

	public String getApprovedByName();

	public String getRequestedByName();

	public String getRemarks();

	public String getFullName();

	public Boolean getIsApproved();

	public Long getLoanId();

	public String getCreateddate();

	public String getUpdateddate();
	
	public String getStatus();
	
	public String getChequeNumber();
	
	public LocalDate getPostedDate();
	
	public Long getRepaymentId();
    
}
