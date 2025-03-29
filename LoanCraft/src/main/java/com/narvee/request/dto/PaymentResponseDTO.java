package com.narvee.request.dto;

import java.time.LocalDate;

public interface PaymentResponseDTO {
	
    public Long getPaymentId();
    public Double getDueAmount();
    public LocalDate getDueDate();
    public String getAmountPaid();
    public String getAmountStatus();
    public String getPaymentType();
//    public Long getDisbursmentId();
   
}





