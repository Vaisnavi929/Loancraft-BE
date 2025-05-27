package com.narvee.request.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Column;

public interface ApplicationDTO {

	public Long getLoanId();

	public String getFirstName();

	public String getLastName();

	public String getEmail();

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
