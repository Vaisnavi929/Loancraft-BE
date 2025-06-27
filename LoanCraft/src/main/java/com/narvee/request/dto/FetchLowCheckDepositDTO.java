package com.narvee.request.dto;

import java.time.LocalDateTime;

public interface FetchLowCheckDepositDTO {

	public Long getId();

	public Double getRecievableAmount();

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
