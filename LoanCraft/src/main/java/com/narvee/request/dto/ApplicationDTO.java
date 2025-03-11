package com.narvee.request.dto;

import java.time.LocalDateTime;

public interface ApplicationDTO {
	
	public Long getloanId();
	public String getfirstName();
	public String getlastName();
	public String getemail();
	public String getstatus();
	public String getmobileNumber();
	public LocalDateTime getcreateddate();
    public LocalDateTime getupdateddate();

}
