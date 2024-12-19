package com.narvee.request.dto;

import java.util.Date;

public interface LoanApplicationDTOS {
	
	public String getStatus();
	public String getFullname();
	public String getMail_id();
	public String getMobile_number();
	public String getApplication_reference_id();
	public Date getDate_of_birth();

}
