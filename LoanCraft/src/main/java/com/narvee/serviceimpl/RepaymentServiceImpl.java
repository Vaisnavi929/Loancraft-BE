
package com.narvee.serviceimpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import com.narvee.Service.ReceiptService;
import com.narvee.Service.RepaymentService;
import com.narvee.Service.SortingRequestDto;
import com.narvee.emi.calculator.EmiCalculatorUtill;
import com.narvee.emi.calculator.RepaymentSchedule;
import com.narvee.entity.Disbursment;
import com.narvee.entity.LmsRepayment;
import com.narvee.repository.DisbursmentRepository;
import com.narvee.repository.RepaymentRepository;
import com.narvee.request.dto.EmiCalResponseDTO;
import com.narvee.request.dto.MemberReportDTO;
import com.narvee.request.dto.PaymentDTO;
import com.narvee.request.dto.PaymentResponseDTO;
import com.narvee.request.dto.PaymentStatementDTO;
import com.narvee.request.dto.PaymentStatementResponseDTO;
import com.narvee.request.dto.RepaymentScheduleRequest;
import com.narvee.response.ReceiptResponse;
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
	private ReceiptService receiptService;

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
			payment.setInterest(repayment.getInterest());
			payment.setPrincipalAmount(repayment.getPrincipalRepaid());

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
		else if (sortField.equalsIgnoreCase("paymentType"))
			sortField = "payment_Type";
		else if (sortField.equalsIgnoreCase("mobileNumber"))
			sortField = "mobilenumber";
		else if (sortField.equalsIgnoreCase("fullName"))
			sortField = "full_name";
		else if(sortField.equalsIgnoreCase("applicationReferenceId"))
			sortField = "applicationReferenceId";
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

	@Override
	public MemberReportDTO getAllRepaymentTrasactionsBydisbursmentid(Long disbursmentId) {
		logger.info("!!! inside class :PaymentServiceImpl ,method :getAllRepaymentTrasactionsBydisbursmentid() ");

		List<Map<String, Object>> memberReports = new ArrayList<>();
		MemberReportDTO memberReportDTO = new MemberReportDTO();

		List<Object[]> reports = repaymentRepository.getAllRepaymentTrasactionsBydisbursmentid(disbursmentId);

		double totalInterestPaid = 0.0;
		double totalPrincipalPaid = 0.0;
		double totalInterestDue = 0.0;
		double totalPrincipalAmountDue = 0.0;
		double totalPenalty = 0.0;
		double totalOtherCharges = 0.0;

		for (Object[] objects : reports) {
			Map<String, Object> memberReport = new LinkedHashMap<>();
			memberReport.put("id", objects[0]);
			memberReport.put("amount_paid", objects[1]);
			memberReport.put("createddate", objects[2]);
			memberReport.put("description", objects[3]);

			Double interestComponent = objects[4] != null ? Double.parseDouble(objects[4].toString()) : 0.0;
			totalInterestPaid += interestComponent;

			memberReport.put("interest_component", interestComponent);
			memberReport.put("other_charges", objects[5]);
			Double otherCharges = objects[5] != null ? Double.parseDouble(objects[5].toString()) : 0.0;
			totalOtherCharges += otherCharges;
			memberReport.put("payment_date", objects[6]);
			memberReport.put("payment_mode", objects[7]);
			memberReport.put("penalty_component", objects[8]);
			Double principalComponent = objects[9] != null ? Double.parseDouble(objects[9].toString()) : 0.0;
			Double penaltyComponent = objects[8] != null ? Double.parseDouble(objects[8].toString()) : 0.0;
			totalPenalty += penaltyComponent;
			totalPrincipalPaid += principalComponent;
			memberReport.put("principal_component", principalComponent);
			memberReport.put("disbursment_id", objects[10]);
			memberReport.put("repayment_id", objects[11]);
			memberReport.put("updateddate", objects[12]);

			Double interest = objects[13] != null ? Double.parseDouble(objects[13].toString()) : 0.0;
			totalInterestDue += interest;

			memberReport.put("interest", interest);
			Double principalAmount = objects[14] != null ? Double.parseDouble(objects[14].toString()) : 0.0;
			totalPrincipalAmountDue += principalAmount;
			memberReport.put("principal_amount", principalAmount);
			if (objects[0] != null) {
				memberReports.add(memberReport);
			}
		}

		// Member details map
		Map<String, Object> memberDetails = new LinkedHashMap<>();
		Map<String, Object> disbursementDetails = new LinkedHashMap<>();
		Map<String, Object> referencesDetails = new LinkedHashMap<>();
		List<Object[]> report = repaymentRepository.getMemeberDetails(disbursmentId);
		for (Object[] objects : report) {
			memberDetails.put("fullname", objects[0]);
			memberDetails.put("application_reference_id", objects[1]);
			memberDetails.put("address", objects[2]);
			memberDetails.put("city", objects[3]);
			memberDetails.put("location", objects[4]);
			memberDetails.put("pincode", objects[5]);
			memberDetails.put("state", objects[6]);
			memberDetails.put("email", objects[7]);
			memberDetails.put("mobile_number", objects[8]);
			memberDetails.put("status", objects[9]);
			memberDetails.put("loan_account_number", objects[10]);

			// Disbursement Details

			disbursementDetails.put("disbursedAmount", objects[11]);
			disbursementDetails.put("amountSanctioned", objects[12]);
			disbursementDetails.put("rateOfInterest", objects[13]);
			disbursementDetails.put("emiAmount", objects[14]);
			disbursementDetails.put("numberOfEmis", objects[15]);
			disbursementDetails.put("documentationCharges", objects[16]);

			BigDecimal amountSanctioned = new BigDecimal(String.valueOf(objects[12]));
			BigDecimal loanProcessingChargesPercentage = new BigDecimal(String.valueOf(objects[17]));
			BigDecimal processingChargesAmount = BigDecimal.ZERO;

			if (loanProcessingChargesPercentage.compareTo(BigDecimal.ZERO) > 0) {
				processingChargesAmount = amountSanctioned.multiply(loanProcessingChargesPercentage)
						.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP); // rounded to 2 decimals
			}

			disbursementDetails.put("loanProcessingCharges", processingChargesAmount);
			disbursementDetails.put("disbursedDate", objects[18]);
			
		
			//*************Reference Details***************
			referencesDetails.put("fullName", objects[19]);
			referencesDetails.put("knownForYears", objects[20]);
			referencesDetails.put("mobileNumber", objects[21]);
			referencesDetails.put("relationship", objects[22]);


		}

		ReceiptResponse dueInfo = receiptService.getReceiptInfo(disbursmentId);

		//*************Due Details***************
		Map<String, Object> dueDetails = new LinkedHashMap<>();
		dueDetails.put("dueDetails", dueInfo.getEmiDueInfo());

		//*************Summary Details***************
		Map<String, Object> summaryDetails = new LinkedHashMap<>();

		summaryDetails.put("totalInterestPaid", totalInterestPaid);
		summaryDetails.put("totalPrincipalPaid", totalPrincipalPaid);
		summaryDetails.put("totalPenalty", totalPenalty);
		summaryDetails.put("totalOtherCharges", totalOtherCharges);

		double totalRepaymentAmountPaid = totalInterestPaid + totalPrincipalPaid;

		summaryDetails.put("totalRepaymentAmount", Math.round(totalRepaymentAmountPaid));

		summaryDetails.put("totalInterestDue", Math.round(totalInterestDue));
		summaryDetails.put("totalPrincipalAmountDue", Math.round(totalPrincipalAmountDue));
		summaryDetails.put("totalOutstandingAmount", dueInfo.getOutstanding());

		memberReportDTO.setMemberDetails(memberDetails);
		memberReportDTO.setMemberReports(memberReports);
		memberReportDTO.setSummaryDetails(summaryDetails);
		memberReportDTO.setDisbursedDetails(disbursementDetails);
		memberReportDTO.setDueDetails(dueDetails);
		memberReportDTO.setReferencesDetails(referencesDetails);

		return memberReportDTO;
	}


	public void generateRepayment(Long disbursmentId) {
		logger.info("!!! inside class :PaymentServiceImpl ,method :generatePayments() ");
		Disbursment disbursment = disbursmentRepository.findById(disbursmentId).get();

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
			payment.setInterest(repayment.getInterest());
			payment.setPrincipalAmount(repayment.getPrincipalRepaid());

			payments.add(payment);

		}

		repaymentRepository.saveAll(payments);
	}

	@Override
	public void generate() {
		logger.info("!!! inside class :PaymentServiceImpl ,method :generate() ");
		List<Disbursment> dis = disbursmentRepository.findAll();
		for (Disbursment disbursment : dis) {
			generateRepayment(disbursment.getDisbursmentId());
		}

	}

}

