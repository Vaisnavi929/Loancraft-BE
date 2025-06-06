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

	    private LocalDate paymentDate;

	    private String paymentMode; // e.g., CASH, UPI, BANK_TRANSFER

	    private String referenceNumber; // Transaction ID, cheque number, etc.

	    private String remarks;

}
