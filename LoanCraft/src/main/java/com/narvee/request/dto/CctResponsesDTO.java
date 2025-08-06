package com.narvee.request.dto;

import java.time.LocalDateTime;

public interface CctResponsesDTO {

	public Long getcrmId();
	public Long getcCtid();
	public LocalDateTime getcreateDdate();
	public LocalDateTime getupdatedDate();
	public LocalDateTime getendTime();
	public LocalDateTime getstartTime();
	public String getduration();
	public String getfeedBack();
    public String getstatus();
}
