package com.narvee.controller;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayRequest {

	private Long did;

	private Double amount;

	private double otherCharges;

	private LocalDate paymentDate;

	private String paymentMode; // e.g., CASH, UPI, BANK_TRANSFER

	private String transactionId; // Transaction ID, cheque number, etc.

	private String remarks;

	private String description;

	private String otherChargesType;

}
