package com.narvee.request.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface CctResponsesDTO {

	public Long getcctId();

	public String getcustomerName();

	public String getagentName();

	public LocalDate getdate();

	public String getfeedBack();

	public String getstatus();

	public Long getloanAccountNumber();

}
