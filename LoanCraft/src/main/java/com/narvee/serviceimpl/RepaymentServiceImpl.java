
package com.narvee.serviceimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

import com.narvee.Service.RepaymentService;
import com.narvee.Service.SortingRequestDto;
import com.narvee.emi.calculator.EmiCalculatorUtill;
import com.narvee.emi.calculator.RepaymentSchedule;
import com.narvee.entity.Disbursment;
import com.narvee.entity.LmsRepayment;
import com.narvee.repository.DisbursmentRepository;
import com.narvee.repository.RepaymentRepository;
import com.narvee.request.dto.EmiCalResponseDTO;
import com.narvee.request.dto.PaymentDTO;
import com.narvee.request.dto.PaymentResponseDTO;
import com.narvee.request.dto.PaymentStatementDTO;
import com.narvee.request.dto.PaymentStatementResponseDTO;
import com.narvee.request.dto.RepaymentScheduleRequest;
import com.narvee.util.DateUtil;

import jakarta.transaction.Transactional;

@Service
public class RepaymentServiceImpl implements RepaymentService {

	public static final Logger logger = LoggerFactory.getLogger(RepaymentServiceImpl.class);

	@Autowired
	private RepaymentRepository repaymentRepository;
	@Autowired
	private DisbursmentRepository disbursmentRepository;

	@Autowired
	ModelMapper mapper;

	@Override
	public LmsRepayment saveRepayment(PaymentDTO paymentDTO) {
		logger.info("!!! inside class :PaymentServiceImpl ,method :savePayment() ");
		LmsRepayment lmsPayment = mapper.map(paymentDTO, LmsRepayment.class);
		Disbursment disbursment = disbursmentRepository.findById(paymentDTO.getDisbursmentId())
				.orElseThrow(() -> new RuntimeException("Loan Application not found"));
		lmsPayment.setDisbursment(disbursment);
		return repaymentRepository.save(lmsPayment);
	}

	@Override

	public LmsRepayment updateRepayment(PaymentDTO paymentDTO) {
		logger.info("!!! inside class :PaymentServiceImpl ,method :updatePayment() ");
		LmsRepayment lmsPayment = mapper.map(paymentDTO, LmsRepayment.class);
		Disbursment disbursment = disbursmentRepository.findById(paymentDTO.getDisbursmentId())
				.orElseThrow(() -> new RuntimeException("Loan Application not found"));
		lmsPayment.setDisbursment(disbursment);
		return repaymentRepository.save(lmsPayment);
	}

	@Override
	public LmsRepayment getRepaymentById(Long paymentId) {
		logger.info("!!! inside class :PaymentServiceImpl ,method :getPaymentById() ");
		return repaymentRepository.findById(paymentId).get();
	}

	@Override
	public Page<PaymentResponseDTO> getAllRepaymentList(SortingRequestDto sortingRequestDto) {
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
			return repaymentRepository.getAllPaymentListWithPagination(pageable);

		} else {
			return repaymentRepository.getAllPaymentListWithKeyword(pageable, keyword);

		}
	}

	@Override
	public Boolean deleteById(Long paymentId) {
		logger.info("!!! inside class :PaymentServiceImpl ,method :deleteById() ");
		repaymentRepository.deleteById(paymentId);
		return true;
	}

	public void generateRepayment(Disbursment disbursment) {
		logger.info("!!! inside class :PaymentServiceImpl ,method :generatePayments() ");
		LocalDate currentDate = LocalDate.now();
		Month currentMonth = currentDate.getMonth();
		int currentYear = currentDate.getYear();

		List<LmsRepayment> payments = new ArrayList<>();
		RepaymentScheduleRequest request = new RepaymentScheduleRequest();
		LocalDateTime emiStartDate = DateUtil.convertToLocalDateTime(disbursment.getLoanSanction().getEmiStartDate());

		request.setStartDate(emiStartDate);
		request.setInstallmentTenor(disbursment.getLoanSanction().getNumberOfEmis());
		request.setAnnualInterestRate(disbursment.getLoanSanction().getRateOfInterest());
		request.setLoanAmount(disbursment.getLoanSanction().getAmountSanctioned());

		EmiCalResponseDTO schedule = null;
		if (disbursment.getLoanSanction().getInterestType().equalsIgnoreCase("Fixed")) {
			logger.info("!!! inside class :PaymentServiceImpl ,Fixed ");
			schedule = EmiCalculatorUtill.calculateRepaymentFixed(request);

		} else {
			logger.info("!!! inside class :PaymentServiceImpl ,floating ");
			schedule = EmiCalculatorUtill.calculateRepaymentfloating(request);

		}

		List<RepaymentSchedule> repaymentSchedule = schedule.getRepaymentSchedules();

		for (RepaymentSchedule repayment : repaymentSchedule) {

			String amountStatus;
			if (repayment.getDueDate().getMonth() == currentMonth && repayment.getDueDate().getYear() == currentYear) {
				amountStatus = "pending";
			} else if (repayment.getDueDate().isBefore(currentDate)) {
				amountStatus = "Due";
			} else {
				amountStatus = "upcoming";
			}

			Double amountPaid = 0.0;
			LmsRepayment payment = new LmsRepayment();
			payment.setDueAmount(disbursment.getLoanSanction().getEmiAmount());
			payment.setDueDate(repayment.getDueDate());
			payment.setAmountPaid(amountPaid);
			payment.setAmountStatus(amountStatus);
			payment.setDisbursment(disbursment);
			payment.setInterest(repayment.getInterest().doubleValue());
			payment.setPrincipalAmount(repayment.getPrincipalRepaid().doubleValue());

			payments.add(payment);

		}

		repaymentRepository.saveAll(payments);
	}

	public List<PaymentStatementDTO> getRepaymentStatement(Long disbursmentId) {
		List<PaymentStatementResponseDTO> payments = repaymentRepository.findByDisbursmentId(disbursmentId);
		List<PaymentStatementDTO> statements = new ArrayList<>();

		for (PaymentStatementResponseDTO payment : payments) {
			PaymentStatementDTO statement = new PaymentStatementDTO();
			statement.setDueDate(payment.getDueDate());
			statement.setDueAmount(payment.getDueAmount());
			statement.setAmountPaid(payment.getAmountPaid());
			statement.setAmountStatus(payment.getAmountStatus());
			statement.setFullName(payment.getFullName());
			statement.setMobileNumber(payment.getMobileNumber());
			statement.setRepaymentId(payment.getRepaymentId());
			statement.setInterest(payment.getInterest());
			statement.setPrincipalAmount(payment.getPrincipalAmount());
			statement.setApplicationReferenceId(payment.getApplicationReferenceId());
			statement.setAmountSanctioned(payment.getAmountSanctioned());
			statement.setOtherCharges(payment.getOtherCharges());
			statements.add(statement);
		}

		return statements;
	}

	@Override
	public Page<PaymentStatementResponseDTO> getMonthlyDueList(SortingRequestDto sortingRequestDto) {
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
		else if (sortField.equalsIgnoreCase("mobileNumber"))
			sortField = "mobilenumber";
		else if (sortField.equalsIgnoreCase("firstName"))
			sortField = "full_name";
		else if (sortField.equalsIgnoreCase("loanAccountNumber"))
			sortField = "loanAccountNumber";
		Sort.Direction sortDirection = Sort.Direction.ASC;
		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
		}

		Sort sort = Sort.by(sortDirection, sortField);
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
		if (keyword.equalsIgnoreCase("empty")) {
			return repaymentRepository.getMonthlyDueList(pageable);

		} else {
			return repaymentRepository.getMonthlyDueListWithKeyword(pageable, keyword);

		}

	}

	@Scheduled(cron = "0 0 0 * * ?")
	@Transactional
	public void updateRepaymentStatusScheduled() {
		logger.info("!!! inside class :PaymentServiceImpl ,method :updateRepaymentStatusScheduled() ");
		repaymentRepository.updatePaymentStatus();
	}

}
