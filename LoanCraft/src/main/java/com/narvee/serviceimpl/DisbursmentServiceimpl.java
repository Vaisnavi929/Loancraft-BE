package com.narvee.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.narvee.Service.DisbursmentService;
import com.narvee.Service.PaymentService;
import com.narvee.Service.SortingRequestDto;
import com.narvee.Service.VerificationService;
import com.narvee.entity.Disbursment;
import com.narvee.entity.LoanSanction;
import com.narvee.repository.DisbursmentRepository;
import com.narvee.repository.LoanSanctionRepository;
import com.narvee.request.dto.DisbursmentDTO;
import com.narvee.request.dto.DisbursmentIdDTO;
import com.narvee.request.dto.DisbursmentResponseDTO;

@Service
public class DisbursmentServiceimpl implements DisbursmentService {

	private static final Logger logger = LoggerFactory.getLogger(DisbursmentServiceimpl.class);
	@Autowired
	ModelMapper mapper;
	@Autowired
	private DisbursmentRepository disbursmentRepository;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private LoanSanctionRepository loanSanctionRepository;

	@Autowired
	private VerificationService verificationService;

	@Override
	@Transactional
	public Disbursment saveDisbursment(DisbursmentDTO disbursmentDTO) {
		logger.info("!!! inside class: DisbursmentServiceImpl, method: saveDisbursment()");
		Disbursment disbursment = mapper.map(disbursmentDTO, Disbursment.class);
		LoanSanction loanSanction = loanSanctionRepository.findById(disbursmentDTO.getLoanSactionId())
				.orElseThrow(() -> new RuntimeException("Loan Application not found"));
		loanSanction.setStatus(disbursmentDTO.getStatus());
		disbursment.setLoanSanction(loanSanction);
		Disbursment savedDisbursment = disbursmentRepository.save(disbursment);
		if (disbursmentDTO.getStatus().equalsIgnoreCase("disbursed")) {
			verificationService.updateDisbursed(loanSanction.getApplications().getLoanId(), true);
		}
		paymentService.generatePayments(savedDisbursment);
		return savedDisbursment;

	}

	@Override
	public Disbursment updateDisbursment(DisbursmentDTO disbursmentDTO) {
		logger.info("!!! inside class:DisbursmentServiceimpl,method :updateDisbursment() ");
		Disbursment disbursment = mapper.map(disbursmentDTO, Disbursment.class);
		LoanSanction loanSanction = loanSanctionRepository.findById(disbursmentDTO.getLoanSactionId())
				.orElseThrow(() -> new RuntimeException("Loan Application not found"));
		disbursment.setLoanSanction(loanSanction);
		Disbursment savedDisbursment = disbursmentRepository.save(disbursment);
		paymentService.generatePayments(savedDisbursment);
		return savedDisbursment;
	}

	@Override
	public Disbursment getById(Long disbursmentId) {
		logger.info("!!! inside class:DisbursmentServiceimpl,method :getById() ");
		return disbursmentRepository.findById(disbursmentId).get();
	}

	@Override
	public List<Disbursment> getAll() {
		logger.info("!!! inside class:DisbursmentServiceimpl,method :getAll() ");
		return disbursmentRepository.findAll();
	}

	@Override
	public Boolean deleteById(Long disbursmentId) {
		logger.info("!!! inside class:DisbursmentServiceimpl,method :deleteById() ");
		disbursmentRepository.deleteById(disbursmentId);
		return true;
	}

	@Override
	public Page<DisbursmentResponseDTO> getAllDisbursmentWithPagination(SortingRequestDto sortingRequestDto) {
		logger.info("!!! inside class:DisbursmentServiceimpl,method :getAllDisbursmentWithPagination() ");
		String sortField = sortingRequestDto.getSortField();
		String sortOrder = sortingRequestDto.getSortOrder();
		String keyword = sortingRequestDto.getKeyword();
		Integer pageNumber = sortingRequestDto.getPageNumber();
		Integer pageSize = sortingRequestDto.getPageSize();
		if (sortField.equalsIgnoreCase("accountNo"))
			sortField = "account_no";
		else if (sortField.equalsIgnoreCase("isfc"))
			sortField = "isfc";
		else if (sortField.equalsIgnoreCase("date"))
			sortField = "date";
		else if (sortField.equalsIgnoreCase("disbursedAmount"))
			sortField = "disbursed_amount";
		else if (sortField.equalsIgnoreCase("remarks"))
			sortField = "remarks";
		else if (sortField.equalsIgnoreCase("branch"))
			sortField = "branch";
		else if (sortField.equalsIgnoreCase("mobileNumber"))
			sortField = "mobilenumber";
		else if (sortField.equalsIgnoreCase("firstName"))
			sortField = "firstname";
		else if (sortField.equalsIgnoreCase("referenceid"))
			sortField = "referenceid";
		else if (sortField.equalsIgnoreCase("createddate"))
			sortField = "createddate";
		else
			sortField = "updateddate";

		Sort.Direction sortDirection = Sort.Direction.ASC;
		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
		}

		Sort sort = Sort.by(sortDirection, sortField);
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
		if (keyword.equalsIgnoreCase("empty")) {
			return disbursmentRepository.getAllDisbursmentWithPagination(pageable);

		} else {
			return disbursmentRepository.getAllDisbursmentWithkeyword(pageable, keyword);

		}

	}

	@Override
	public List<DisbursmentIdDTO> getCustomerDetailsWithDisbursmentId() {
		logger.info("!!! inside class:DisbursmentServiceimpl,method :getCustomerDetailsWithDisbursmentId() ");
		return disbursmentRepository.getCustomerDetailsWithDisbursmentId();
	}
}
