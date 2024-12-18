package com.narvee.Service;

import org.springframework.data.domain.Page;

import com.narvee.entity.Payment;
import com.narvee.request.dto.PaymentDTO;
import com.narvee.request.dto.SortingDTO;

public interface PaymentService {

	public Payment savePayment(PaymentDTO paymentDTO);

	public Payment updatePayment(PaymentDTO paymentDto);

	public Payment getById(Long payamentId);

	public Boolean deleteById(Long payamentId);

	Page<PaymentDTO> getAllPayments(SortingDTO sortingDTO);

}
