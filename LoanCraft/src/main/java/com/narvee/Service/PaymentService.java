package com.narvee.Service;

import java.util.List;

import com.narvee.entity.Payment;
import com.narvee.request.dto.PaymentDTO;

public interface PaymentService {
	
	public Payment savePayment(PaymentDTO paymentDTO);
	public Payment updatePayment(PaymentDTO paymentDto);
	public Payment getById(Long payamentId);
	public List<Payment> getAll();
    public Boolean deleteById(Long payamentId);
	
}
