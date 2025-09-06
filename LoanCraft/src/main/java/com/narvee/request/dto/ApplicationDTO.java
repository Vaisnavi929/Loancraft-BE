package com.narvee.request.dto;

import java.time.LocalDate;

public interface ApplicationDTO {

	public Long getLoanId();

	public String getEmail();

	public String getFullName();

	public String getStatus();

	public String getMobileNumber();

	public LocalDate getCreateddate();

	public LocalDate getUpdateddate();

	public String getRepaymentmode();

	public Double getLoanamount();

	public String getReferenceid();

	public Boolean getPhysicalVerification();

	public Boolean getIsRequestedFileUpload();

	public Boolean getIsFileUpload();

	public String getRemarks();

}
