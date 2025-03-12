package com.narvee.serviceimpl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.narvee.Service.PaymentService;
import com.narvee.Service.SortingRequestDto;
import com.narvee.entity.Disbursment;
import com.narvee.entity.LmsPayment;
import com.narvee.repository.DisbursmentRepository;
import com.narvee.repository.PaymentRepository;
import com.narvee.request.dto.PaymentDTO;
import com.narvee.request.dto.PaymentResponseDTO;

@Service
public class PaymentServiceImpl implements PaymentService{
	
	public static final Logger logger=LoggerFactory.getLogger(PaymentServiceImpl.class);
	
	@Autowired
	private PaymentRepository paymentRepository;
	@Autowired
	private DisbursmentRepository disbursmentRepository;
	
	@Autowired
	ModelMapper mapper;

	@Override
	public LmsPayment savePayment(PaymentDTO paymentDTO) {
	logger.info("!!! inside class :PaymentServiceImpl ,method :savePayment() ");
	LmsPayment lmsPayment = mapper.map(paymentDTO, LmsPayment.class);
	Disbursment disbursment = disbursmentRepository.findById(paymentDTO.getDisbursmentId())
			.orElseThrow(() -> new RuntimeException("Loan Application not found"));
	lmsPayment.setDisbursment(disbursment);
		return paymentRepository.save(lmsPayment);
	}

	@Override
	public LmsPayment updatePayment(PaymentDTO paymentDTO) {
		logger.info("!!! inside class :PaymentServiceImpl ,method :updatePayment() ");
		LmsPayment lmsPayment = mapper.map(paymentDTO, LmsPayment.class);
		Disbursment disbursment = disbursmentRepository.findById(paymentDTO.getDisbursmentId())
				.orElseThrow(() -> new RuntimeException("Loan Application not found"));
		lmsPayment.setDisbursment(disbursment);
		return paymentRepository.save(lmsPayment);
	}

	@Override
	public LmsPayment getPaymentById(Long paymentId) {
		logger.info("!!! inside class :PaymentServiceImpl ,method :getPaymentById() ");
		return paymentRepository.findById(paymentId).get();
	}

	@Override
	public Page<PaymentResponseDTO> getAllPaymentList(SortingRequestDto sortingRequestDto) {
		logger.info("!!! inside class :PaymentServiceImpl ,method :getAll() ");
		String sortField = sortingRequestDto.getSortField();
		String sortOrder = sortingRequestDto.getSortOrder();
		String keyword = sortingRequestDto.getKeyword();
		Integer pageNumber = sortingRequestDto.getPageNumber();
		Integer pageSize = sortingRequestDto.getPageSize();
	    if (sortField.equalsIgnoreCase("amountPaid"))
			sortField = "amount_paid";
		else if (sortField.equalsIgnoreCase("dueDate"))
		     sortField = "due_date";   
		else if (sortField.equalsIgnoreCase("amountStatus"))
		     sortField = "amount_status";
		else if (sortField.equalsIgnoreCase("dueAmount"))
		     sortField = "due_amount";
		Sort.Direction sortDirection = Sort.Direction.ASC;
		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
		}

		Sort sort = Sort.by(sortDirection, sortField);
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
		if (keyword.equalsIgnoreCase("empty")) {
			return paymentRepository.getAllPaymentListWithPagination(pageable);
			
			
			
		} else {
			return paymentRepository.getAllPaymentListWithKeyword(pageable, keyword);

		}}
	

	@Override
	public Boolean deleteById(Long paymentId) {
		logger.info("!!! inside class :PaymentServiceImpl ,method :deleteById() ");
		paymentRepository.deleteById(paymentId);
		return true;
	}

}
