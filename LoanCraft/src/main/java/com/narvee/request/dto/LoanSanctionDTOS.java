package com.narvee.request.dto;

import java.util.Date;

public interface LoanSanctionDTOS {
	 

	 public Double getAmountSanctioned();
	 public Double getRateOfInterest();
	 public String getInterestType();
	 public int getNumberOfEmis();
	 public Double getEmiAmount();
	 public String getrepaymentMode();
	 public String getStatus();


}
