package com.narvee.Service;

import java.time.LocalDate;

import com.narvee.response.ReceipetResponse;

public interface ReceiptService {

	public ReceipetResponse getReceiptInfo(Long disbursmentId);

	public ReceipetResponse getReceiptInfoByReceiptDate(Long dId, LocalDate localDate);

}
