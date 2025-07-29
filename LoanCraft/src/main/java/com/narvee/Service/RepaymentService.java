
package com.narvee.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.narvee.entity.Disbursment;
import com.narvee.entity.LmsRepayment;
import com.narvee.request.dto.MemberReportDTO;
import com.narvee.request.dto.PaymentDTO;
import com.narvee.request.dto.PaymentResponseDTO;
import com.narvee.request.dto.PaymentStatementDTO;
import com.narvee.request.dto.PaymentStatementResponseDTO;

public interface RepaymentService {

	public LmsRepayment saveRepayment(PaymentDTO paymentDTO);

	public LmsRepayment updateRepayment(PaymentDTO paymentDTO);

	public LmsRepayment getRepaymentById(Long paymentId);

	public Page<PaymentResponseDTO> getAllRepaymentList(SortingRequestDto sortingRequestDto);

	public Boolean deleteById(Long paymentId);

	public void generateRepayment(Disbursment disbursment);

	public List<PaymentStatementDTO> getRepaymentStatement(Long disbursmentId);

	public Page<PaymentStatementResponseDTO> getMonthlyDueList(SortingRequestDto sortingRequestDto);

	public void updateRepaymentStatusScheduled();

	public MemberReportDTO getAllRepaymentTrasactionsBydisbursmentid(Long disbursmentId);

	public void generateRepayment(Long disbursmentId);

	void generate();

}
