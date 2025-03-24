package com.narvee.Service;

import org.springframework.data.domain.Page;

import com.narvee.entity.Disbursment;
import com.narvee.entity.LmsPayment;
import com.narvee.request.dto.PaymentDTO;
import com.narvee.request.dto.PaymentResponseDTO;

public interface PaymentService {
	
	public LmsPayment savePayment(PaymentDTO paymentDTO);
	public LmsPayment updatePayment(PaymentDTO paymentDTO);
	public LmsPayment getPaymentById(Long paymentId);
	public Page<PaymentResponseDTO> getAllPaymentList(SortingRequestDto sortingRequestDto);
	public Boolean deleteById(Long paymentId); 	
	 public void generatePayments(Disbursment disbursment);

}

