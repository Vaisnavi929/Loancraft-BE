package com.narvee.request.dto;

public interface FetchLowCheckDepositDTO {

	public Double getRecievableAmount();

	public String getApprovedByName();

	public String getRequestedByName();

	public String getFullName();

	public Long getLoanId();

}
