package com.narvee.request.dto;

public interface FetchLowCheckDepositDTO {

	public Double getRecievableMount();

	public String getApprovedByName();

	public String getRequestedByName();

	public String getFullName();

	public Long getLoanId();

}
