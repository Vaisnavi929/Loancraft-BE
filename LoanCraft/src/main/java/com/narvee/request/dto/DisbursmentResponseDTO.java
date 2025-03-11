package com.narvee.request.dto;

import java.time.LocalDateTime;
import java.util.Date;

public interface DisbursmentResponseDTO {
	
	public Long getdisbursmentId();

	public Date getdate();

	public String getaccountNo();

	public String getisfc();

	public Double getdisbursedAmount();

	public String getremarks();
    public LocalDateTime getcreateddate();
    public LocalDateTime getupdateddate();
	public String getbranch();
    

}
