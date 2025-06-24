package com.narvee.request.dto;

import java.util.Date;

public interface LoanSanctionDTOS {

	public Double getAmountSanctioned();

	public Double getRateOfInterest();

	public String getInterestType();

	public int getNumberOfEmis();

	public Double getEmiAmount();

	public String getRepaymentMode();

	public String getStatus();

	public String getFirstName();

	public String getLastName();

	public String getReferenceid();

	public String getSanctionid();

	public String getcreateddate();

	public String getUpdateddate();
	
	public String getDocumentationCharges();
	
	public String getLoanProcessingCharges();
	
	public String getLoanAccountNumber();
	
	public Boolean getIsRequestedFileUpload();

	public Boolean getIsFileUpload();
	public Long getLoanId();
	public String getEmiStartDate();
	public String getEmiEndDate();
	
	

}
