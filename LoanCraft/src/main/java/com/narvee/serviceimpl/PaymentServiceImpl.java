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
import com.narvee.entity.Ledger;
import com.narvee.entity.LoanSanction;
import com.narvee.entity.Payment;
import com.narvee.repository.LedgerRepository;
import com.narvee.repository.LoanSanctionRepository;
import com.narvee.repository.PaymentRepository;
import com.narvee.request.dto.PaymentDTO;
import com.narvee.request.dto.SortingDTO;

import jakarta.transaction.Transactional;
@Service
public class PaymentServiceImpl implements PaymentService{
	
	private static final Logger logger=LoggerFactory.getLogger(PaymentServiceImpl.class);

	@Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private LedgerRepository ledgerRepository;

    @Autowired
    private LoanSanctionRepository loanSanctionRepository;

    @Autowired
    private ModelMapper modelMapper;

	@Override
	@Transactional
	public Payment savePayment(PaymentDTO paymentDTO) {
		logger.info("!!! inside class : PaymentServiceImpl, !! method : savePayment");
		Payment payment = modelMapper.map(paymentDTO, Payment.class);

		LoanSanction loanSanction = loanSanctionRepository.findById(paymentDTO.getLoanSactionId())
				.orElseThrow(() -> new RuntimeException("LoanSanction not found"));
		payment.setLoanSanction(loanSanction);
		Payment savedPayment = paymentRepository.save(payment);
		Ledger ledger = new Ledger();
		ledger.setPayment(savedPayment);
		ledger.setDate(savedPayment.getPaymentDate());
		ledger.setLoanAmount(loanSanction.getAmountSanctioned());
		ledger.setPrincipleAmount(loanSanction.getAmountDisbursed());
		ledger.setAmountPaid(savedPayment.getAmount());
		ledger.setBalance(loanSanction.getAmountDisbursed() - savedPayment.getAmount());
		ledger.setPaid(savedPayment.getAmount());
		ledgerRepository.save(ledger);
		
		loanSanction.setAmountDisbursed(loanSanction.getAmountDisbursed() - savedPayment.getAmount());
		loanSanctionRepository.save(loanSanction);

		return savedPayment;
	}
	
	@Override
	public Payment updatePayment(PaymentDTO paymentDto) {
		logger.info("!!! inside class : PaymentServiceImpl, !! method : updatePayment ");
		Payment payment=modelMapper.map(paymentDto, Payment.class);
		return paymentRepository.save(payment);
	}

	@Override
	public Payment getById(Long payamentId) {
		logger.info("!!! inside class : PaymentServiceImpl, !! method : getById");
		return paymentRepository.findById(payamentId).get();
	}

	@Override
	public Page<PaymentDTO> getAllPayments(SortingDTO sortingDTO) {
		logger.info("Inside PaymentServiceImpl: getAllPayments");

		String sortField = sortingDTO.getSortField();
		String sortOrder = sortingDTO.getSortOrder();
		String keyword = sortingDTO.getKeyword();
		Integer pageNumber = sortingDTO.getPageNumber();
		Integer pageSize = sortingDTO.getPageSize();


		// Map sorting field to entity's field
		if (sortField.equalsIgnoreCase("Amount")) {
			sortField = "amount";
		} else if (sortField.equalsIgnoreCase("LoanSactionId")) {
			sortField = "loan_saction_id";		
		
		} else if (sortField.equalsIgnoreCase("VoucherNumber")) {
			sortField = "vocher_number";
		} else if (sortField.equalsIgnoreCase("BranchName")) {
			sortField = "branch_name";
		} else if (sortField.equalsIgnoreCase("Perticulars")) {
			sortField = "perticulars";
		} else if (sortField.equalsIgnoreCase("Description")) {
			sortField = "description";
		} else if (sortField.equalsIgnoreCase("BankDetails")) {
			sortField = "bank_details";
		} else {
			sortField = "payment_date"; // Default field for sorting
		}
		// Set sort direction (ascending or descending)
		Sort.Direction sortDirection = Sort.Direction.ASC;
		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
		}
		// Create Pageable object with sorting and pagination
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(sortDirection, sortField));

		Page<Payment> paymentsPage;
		// If keyword is provided, search by keyword; otherwise, fetch all
		if (keyword == null || keyword.isEmpty()) {
			paymentsPage = paymentRepository.findAllPayment(pageable);
		} else {
			paymentsPage = paymentRepository.findPaymentByKeyword(keyword, pageable);
		}
		// Map Payment entities to PaymentDTOs
		return paymentsPage.map(payment -> modelMapper.map(payment, PaymentDTO.class));
	}

	@Override
	public Boolean deleteById(Long payamentId) {
		logger.info("!!! inside class : PaymentServiceImpl , !! method : deleteById ");
		 paymentRepository.deleteById(payamentId);
		 return true;
	}

	
}
