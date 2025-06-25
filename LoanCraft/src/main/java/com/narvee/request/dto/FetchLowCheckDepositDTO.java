package com.narvee.request.dto;

public interface FetchLowCheckDepositDTO {

	
	public Long getId();
	public Double getRecievableAmount();

	public String getApprovedByName();

	public String getRequestedByName();
    public String getRemarks();
	public String getFullName();
    public Boolean getIsApproved();
	public Long getLoanId();

}
