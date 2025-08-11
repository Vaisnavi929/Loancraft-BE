package com.narvee.request.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatementDTO {
	private Long repaymentId;
	private LocalDate dueDate;
	private LocalDate lastPaidDate;
	private double dueAmount;
	private Double amountPaid;
	private String amountStatus;
	private String fullName;
	private String mobileNumber;
	private String applicationReferenceId;
	private String paymentType;
	private String loanAccountNumber;
	private double interest;
	private double principalAmount;
	private double amountSanctioned;
	private long penaltyUncoveredDays;

}
