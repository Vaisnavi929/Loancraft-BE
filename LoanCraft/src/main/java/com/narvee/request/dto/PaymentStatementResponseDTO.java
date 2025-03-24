package com.narvee.request.dto;

import java.time.LocalDate;

public interface PaymentStatementResponseDTO {
	  public Long getPaymentId();
	  public  LocalDate getDueDate();    
	  public  double getDueAmount();	    
	  public String getAmountPaid();	    
	  public  String getAmountStatus();	    
	  public  String getFirstName(); 
	  public String getMobileNumber();
	  public String getApplicationReferenceId();
	}
