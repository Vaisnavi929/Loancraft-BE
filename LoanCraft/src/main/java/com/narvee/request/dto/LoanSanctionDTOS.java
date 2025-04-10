package com.narvee.request.dto;

public interface LoanSanctionDTOS {

	public Double getAmountSanctioned();

	public Double getRateOfInterest();

	public String getInterestType();

	public int getNumberOfEmis();

	public Double getEmiAmount();

	public String getrepaymentMode();

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
	

}
