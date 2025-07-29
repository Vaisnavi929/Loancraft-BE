package com.narvee.request.dto;

import java.time.LocalDateTime;
import java.util.Date;

public interface DisbursmentResponseDTO {

	public Long getdisbursmentId();

	public String getdisbRefNo();

	public Date getdate();

	public String getFullName();

	public String getaccountNo();

	public String getmobileNumber();

	public String getisfc();

	public Double getdisbursedAmount();

	public String getReferenceid();

	public String getremarks();

	public LocalDateTime getcreateddate();

	public LocalDateTime getupdateddate();

	public String getbranch();

	public Boolean getIsRequestedFileUpload();

	public Boolean getIsFileUpload();

	public String getLoanAccountNumber();

}
