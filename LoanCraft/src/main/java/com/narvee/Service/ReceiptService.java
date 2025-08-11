package com.narvee.Service;

import java.time.LocalDate;

import com.narvee.controller.PayRequest;
import com.narvee.response.ReceiptResponse;

public interface ReceiptService {

	public ReceiptResponse getReceiptInfo(Long disbursmentId);

	public ReceiptResponse getReceiptInfoByReceiptDate(Long dId, LocalDate localDate);

	public ReceiptResponse payLoan(PayRequest payRequest);


}
