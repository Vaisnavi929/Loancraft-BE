package com.narvee.request.dto;

import java.time.LocalDateTime;

public interface ApplicationDTO {

	public Long getLoanId();
	
	public String getEmail();
	
	public String getFullName();

	public String getStatus();

	public String getMobileNumber();

	public LocalDateTime getCreateddate();

	public LocalDateTime getUpdateddate();

	public String getRepaymentmode();

	public Double getLoanamount();

	public String getReferenceid();

	public Boolean  getPhysicalVerification();
	
	public Boolean getIsRequestedFileUpload();

	public Boolean getIsFileUpload();
	
}
