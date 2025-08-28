package com.narvee.request.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface RepaymentTransactionsDTO {

	public Long getId();

	public Double getAmountPaid();

	public LocalDateTime getCreateddate();

	public String getDescription();

	public Double getInterestComponent();

	public Double getOtherCharges();

	public LocalDate getPaymentDate();

	public String getPaymentMode();

	public Double getPenaltyComponent();

	public Double getPrincipalComponent();

	public Long getDisbursmentId(); // d.disbursment_id

	public Double getRemainingBalance();

	public Long getRepaymentId();

	public LocalDateTime getUpdateddate();
}
