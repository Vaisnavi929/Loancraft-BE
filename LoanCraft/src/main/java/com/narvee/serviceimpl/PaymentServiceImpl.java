
package com.narvee.serviceimpl;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.narvee.Service.PaymentService;
import com.narvee.Service.SortingRequestDto;
import com.narvee.entity.Disbursment;
import com.narvee.entity.LmsPayment;
import com.narvee.repository.DisbursmentRepository;
import com.narvee.repository.PaymentRepository;
import com.narvee.request.dto.PaymentDTO;
import com.narvee.request.dto.PaymentResponseDTO;
import com.narvee.request.dto.PaymentStatementDTO;
import com.narvee.request.dto.PaymentStatementResponseDTO;

import jakarta.transaction.Transactional;

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
		else if (sortField.equalsIgnoreCase("paymentType"))
		     sortField = "payment_type";
		else if (sortField.equalsIgnoreCase("checkBouns"))
		     sortField = "check_bouns";
		else if (sortField.equalsIgnoreCase("intrest"))
		     sortField = "intrest";
		else if (sortField.equalsIgnoreCase("paymentmode"))
		     sortField = "paymentmode";
		else if (sortField.equalsIgnoreCase("remarks"))
		     sortField = "remarks";
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

    public void generatePayments(Disbursment disbursment) {
        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        int currentYear = currentDate.getYear();
        
        List<LmsPayment> payments = new ArrayList<>();
        int noOfEmis = disbursment.getLoanSanction().getNumberOfEmis();
        for (int i = 1; i <= noOfEmis; i++) { 
            LocalDate dueDate = disbursment.getDate().plusMonths(i);
            String amountStatus;
         
            if (dueDate.getMonth() == currentMonth && dueDate.getYear() == currentYear) {
                amountStatus = "pending";
            } else if (dueDate.isBefore(currentDate)) {
                amountStatus = "Due";
            } else {
                amountStatus = "upcoming";
            }
            Double amountPaid = 0.0;
            LmsPayment payment = new LmsPayment();
            payment.setDueAmount(disbursment.getDisbursedAmount() / noOfEmis); 
            payment.setDueDate(dueDate);
            payment.setAmountPaid(amountPaid);
            payment.setAmountStatus(amountStatus);
            payment.setDisbursment(disbursment);
            
            payments.add(payment);
        }
        
        paymentRepository.saveAll(payments);
    }
    public List<PaymentStatementDTO> getPaymentStatement(Long disbursmentId) {
        List<PaymentStatementResponseDTO> payments = paymentRepository.findByDisbursmentId(disbursmentId);
        List<PaymentStatementDTO> statements = new ArrayList<>();

        for (PaymentStatementResponseDTO payment : payments) {
            PaymentStatementDTO statement = new PaymentStatementDTO();
            statement.setDueDate(payment.getDueDate());
            statement.setDueAmount(payment.getDueAmount());
            statement.setAmountPaid(payment.getAmountPaid());
            statement.setAmountStatus(payment.getAmountStatus());
            statement.setFirstName(payment.getFirstName());
            statement.setMobileNumber(payment.getMobileNumber());
            statement.setPaymentId(payment.getPaymentId());
            statement.setPaymentType(payment.getPaymentType());    
            statement.setApplicationReferenceId(payment.getApplicationReferenceId());
            statements.add(statement)  ;
        }

        return statements;
    }

	@Override
	public Page<PaymentStatementResponseDTO> getMonthlyDueList(SortingRequestDto sortingRequestDto){
		logger.info("!!! inside class :PaymentServiceImpl ,method :getMonthlyDueList() ");
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
			else if (sortField.equalsIgnoreCase("paymentType"))
			     sortField = "payment_Type";
			else if (sortField.equalsIgnoreCase("mobileNumber"))
			     sortField = "mobilenumber";
			else if (sortField.equalsIgnoreCase("firstName"))
			     sortField = "firstname";
			else if (sortField.equalsIgnoreCase("loanAccountNumber"))
			     sortField = "loanAccountNumber";
			Sort.Direction sortDirection = Sort.Direction.ASC;
			if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
				sortDirection = Sort.Direction.DESC;
			}

			
			
			Sort sort = Sort.by(sortDirection, sortField);
			Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
			if (keyword.equalsIgnoreCase("empty")) {
				return paymentRepository.getMonthlyDueList(pageable);
				
				
				
			} else {
				return paymentRepository.getMonthlyDueListWithKeyword(pageable, keyword);

	}
	
	}
	
	 @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updatePaymentStatusScheduled() {
        paymentRepository.updatePaymentStatus();
    }
}




