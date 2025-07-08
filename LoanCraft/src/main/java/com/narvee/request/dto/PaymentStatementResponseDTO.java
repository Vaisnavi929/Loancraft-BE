package com.narvee.request.dto;

import java.time.LocalDate;

public interface PaymentStatementResponseDTO {
	public Long getRepaymentId();

	public LocalDate getDueDate();

	public double getDueAmount();

	public Double getAmountPaid();

	public String getAmountStatus();

	public String getFullName();

	public String getMobileNumber();

	public String getApplicationReferenceId();

	public String getLoanAccountNumber();

	public Double getInterest();

	public Double getPrincipalAmount();

	public Double getAmountSanctioned();

	public Double getOtherCharges();

}
