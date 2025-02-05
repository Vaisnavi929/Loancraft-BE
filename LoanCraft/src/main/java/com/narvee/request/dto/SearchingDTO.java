package com.narvee.request.dto;

import java.time.LocalDate;

public interface SearchingDTO {

	public String getEmail();

	public String getFirstName();

	public String getLastName();

	public String getMobileNo();

	public LocalDate getDob();

	public String getFathersOrSpousesName();

	public String getMiddleName();

	public String getAccountNumber();
	
	public String getHighestQualification();
	
	public String getAdhaarNumber();

}
