package com.narvee.request.dto;

import java.time.LocalDateTime;
import java.util.Date;



public interface ApplicationResponseDto {
//	  public Long getloanId();
	  public  LocalDateTime getUpdateddate(); 
	  public Long getLoanId();
	  public String getReferenceId();
	  public LocalDateTime getCreateddate();
	  public String getFirstName();
	  public  String getLastName();
	  public String getEmail();
	  public  String getStatus();
	  public  String getMobileNumber();
	  public String getRepaymentMode();
	  public Double getLoanAmount();

}
