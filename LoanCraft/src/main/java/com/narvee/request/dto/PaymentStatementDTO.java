package com.narvee.request.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatementDTO {
	private Long paymentId;
    private LocalDate dueDate;
    private double dueAmount;
    private Double amountPaid;
    private String amountStatus;
    private String firstName;
    private String mobileNumber;
    private String applicationReferenceId;
    private String paymentType;
    private String loanAccountNumber; 

    // Getters and Setters
}
