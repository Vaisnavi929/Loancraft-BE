package com.narvee.request.dto;



import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
	
	
    private Long paymentId;
    private Double dueAmount;
    private LocalDate dueDate;
    private Double amountPaid;
    private String amountStatus;
    private Long disbursmentId; 
	private String paymentType;
	private Double intrest ;
	private String paymentmode;
	private String remarks;
	private String checkBouns;
}



