package com.narvee.request.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface CctResponsesDTO {

	public Long getcrmId();
	public Long getcCtid();
	public LocalDateTime getcreateDdate();
	public LocalDateTime getupdatedDate();
	public LocalDate getendTime();
	public LocalDate getstartTime();
	public String getduration();
	public String getfeedBack();
	public double date();
    public String getstatus();
}
