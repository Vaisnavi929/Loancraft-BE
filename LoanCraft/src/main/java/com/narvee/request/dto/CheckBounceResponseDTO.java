package com.narvee.request.dto;

import java.time.LocalDate;

public interface CheckBounceResponseDTO {
	
public String getChequeNumber();
    
public LocalDate getBounceDate();
    
public Double getAmount();
    
public String getReason(); 

public String getRemarks();
    
public Double getBounceCharges();
public String getStatus();
public Long getDisbursmentId();

}
