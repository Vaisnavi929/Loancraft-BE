package com.narvee.request.dto;

import java.time.LocalDateTime;

public interface FetchLowCheckDepositDTO {

	public Long getId();

	public Double getReceivableAmount();
  
	public Double getRequestedAmount();

	public String getApprovedByName();

	public String getRequestedByName();

	public Double getRequestedAmount();

	public String getRemarks();

	public String getFullName();

	public Boolean getIsApproved();

	public Long getLoanId();

	public String getCreateddate();

	public String getUpdateddate();

}
