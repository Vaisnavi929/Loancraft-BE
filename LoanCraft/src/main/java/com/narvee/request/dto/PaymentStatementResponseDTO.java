package com.narvee.request.dto;

import java.time.LocalDate;

public interface PaymentStatementResponseDTO {
	  public Long getPaymentId();
	  public  LocalDate getDueDate();    
	  public  double getDueAmount();	    
	  public Double getAmountPaid();	    
	  public  String getAmountStatus();	    
	  public  String getFirstName(); 
	  public String getMobileNumber();
	  public String getPaymentType();
	  public String getApplicationReferenceId();
	  public String getLoanAccountNumber(); 
	  
	  
	}
