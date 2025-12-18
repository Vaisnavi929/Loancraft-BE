
package com.narvee.serviceimpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.narvee.entity.LoanSanction;
import com.narvee.entity.RepaymentTransactions;
import com.narvee.filter.Event;
import com.narvee.helpers.EmiDueInfoDTO;
import com.narvee.repository.DisbursmentRepository;
import com.narvee.repository.RepaymentRepository;
import com.narvee.repository.RepaymentTransactionsRepository;
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
	private RepaymentTransactionsRepository repaymentTransactionRepository;

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
			statement.setLastPaidDate(payment.getLastPaidDate());
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
		else if (sortField.equalsIgnoreCase("applicationReferenceId"))

			sortField = "application_reference_id";

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

	/*
	 * @Override public MemberReportDTO
	 * getAllRepaymentTrasactionsBydisbursmentid(Long disbursmentId) { logger.
	 * info("!!! inside class :PaymentServiceImpl ,method :getAllRepaymentTrasactionsBydisbursmentid() "
	 * );
	 * 
	 * List<Map<String, Object>> memberReports = new ArrayList<>(); MemberReportDTO
	 * memberReportDTO = new MemberReportDTO();
	 * 
	 * List<Object[]> reports =
	 * repaymentRepository.getAllRepaymentTrasactionsBydisbursmentid(disbursmentId);
	 * 
	 * double totalInterestPaid = 0.0; double totalPrincipalPaid = 0.0; double
	 * totalInterestDue = 0.0; double totalPrincipalAmountDue = 0.0; double
	 * totalPenalty = 0.0; double totalOtherCharges = 0.0;
	 * 
	 * for (Object[] objects : reports) { Map<String, Object> memberReport = new
	 * LinkedHashMap<>(); memberReport.put("id", objects[0]);
	 * memberReport.put("amount_paid", objects[1]); memberReport.put("createddate",
	 * objects[2]); memberReport.put("description", objects[3]);
	 * 
	 * Double interestComponent = objects[4] != null ?
	 * Double.parseDouble(objects[4].toString()) : 0.0; totalInterestPaid +=
	 * interestComponent;
	 * 
	 * memberReport.put("interest_component", interestComponent);
	 * memberReport.put("other_charges", objects[5]); Double otherCharges =
	 * objects[5] != null ? Double.parseDouble(objects[5].toString()) : 0.0;
	 * totalOtherCharges += otherCharges; memberReport.put("payment_date",
	 * objects[6]); memberReport.put("payment_mode", objects[7]);
	 * memberReport.put("penalty_component", objects[8]); Double principalComponent
	 * = objects[9] != null ? Double.parseDouble(objects[9].toString()) : 0.0;
	 * Double penaltyComponent = objects[8] != null ?
	 * Double.parseDouble(objects[8].toString()) : 0.0; totalPenalty +=
	 * penaltyComponent; totalPrincipalPaid += principalComponent;
	 * memberReport.put("principal_component", principalComponent);
	 * memberReport.put("disbursment_id", objects[10]);
	 * memberReport.put("repayment_id", objects[11]);
	 * memberReport.put("updateddate", objects[12]);
	 * 
	 * Double interest = objects[13] != null ?
	 * Double.parseDouble(objects[13].toString()) : 0.0; totalInterestDue +=
	 * interest;
	 * 
	 * memberReport.put("interest", interest); Double principalAmount = objects[14]
	 * != null ? Double.parseDouble(objects[14].toString()) : 0.0;
	 * totalPrincipalAmountDue += principalAmount;
	 * memberReport.put("principal_amount", principalAmount); if (objects[0] !=
	 * null) { memberReports.add(memberReport); } }
	 * 
	 * // Member details map Map<String, Object> memberDetails = new
	 * LinkedHashMap<>(); Map<String, Object> disbursementDetails = new
	 * LinkedHashMap<>(); Map<String, Object> referencesDetails = new
	 * LinkedHashMap<>(); List<Object[]> report =
	 * repaymentRepository.getMemeberDetails(disbursmentId); for (Object[] objects :
	 * report) { memberDetails.put("fullname", objects[0]);
	 * memberDetails.put("application_reference_id", objects[1]);
	 * memberDetails.put("address", objects[2]); memberDetails.put("city",
	 * objects[3]); memberDetails.put("location", objects[4]);
	 * memberDetails.put("pincode", objects[5]); memberDetails.put("state",
	 * objects[6]); memberDetails.put("email", objects[7]);
	 * memberDetails.put("mobile_number", objects[8]); memberDetails.put("status",
	 * objects[9]); memberDetails.put("loan_account_number", objects[10]);
	 * 
	 * 
	 * // Disbursement Details
	 * 
	 * disbursementDetails.put("disbursedAmount", objects[11]);
	 * disbursementDetails.put("amountSanctioned", objects[12]);
	 * disbursementDetails.put("rateOfInterest", objects[13]);
	 * disbursementDetails.put("emiAmount", objects[14]);
	 * disbursementDetails.put("numberOfEmis", objects[15]);
	 * disbursementDetails.put("documentationCharges", objects[16]);
	 * 
	 * BigDecimal amountSanctioned = new BigDecimal(String.valueOf(objects[12]));
	 * BigDecimal loanProcessingChargesPercentage = new
	 * BigDecimal(String.valueOf(objects[17])); BigDecimal processingChargesAmount =
	 * BigDecimal.ZERO;
	 * 
	 * if (loanProcessingChargesPercentage.compareTo(BigDecimal.ZERO) > 0) {
	 * processingChargesAmount =
	 * amountSanctioned.multiply(loanProcessingChargesPercentage)
	 * .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP); // rounded to 2
	 * decimals }
	 * 
	 * disbursementDetails.put("loanProcessingCharges", processingChargesAmount);
	 * disbursementDetails.put("disbursedDate", objects[18]);
	 * 
	 * 
	 * //*************Reference Details***************
	 * referencesDetails.put("fullName", objects[19]);
	 * referencesDetails.put("knownForYears", objects[20]);
	 * referencesDetails.put("mobileNumber", objects[21]);
	 * referencesDetails.put("relationship", objects[22]);
	 * 
	 * 
	 * }
	 * 
	 * ReceiptResponse dueInfo = receiptService.getReceiptInfo(disbursmentId);
	 * 
	 * //*************Due Details*************** Map<String, Object> dueDetails =
	 * new LinkedHashMap<>(); dueDetails.put("dueDetails", dueInfo.getEmiDueInfo());
	 * 
	 * //*************Summary Details*************** Map<String, Object>
	 * summaryDetails = new LinkedHashMap<>();
	 * 
	 * summaryDetails.put("totalInterestPaid", totalInterestPaid);
	 * summaryDetails.put("totalPrincipalPaid", totalPrincipalPaid);
	 * summaryDetails.put("totalPenalty", totalPenalty);
	 * summaryDetails.put("totalOtherCharges", totalOtherCharges);
	 * 
	 * double totalRepaymentAmountPaid = totalInterestPaid + totalPrincipalPaid;
	 * 
	 * summaryDetails.put("totalRepaymentAmount",
	 * Math.round(totalRepaymentAmountPaid));
	 * 
	 * summaryDetails.put("totalInterestDue", Math.round(totalInterestDue));
	 * summaryDetails.put("totalPrincipalAmountDue",
	 * Math.round(totalPrincipalAmountDue));
	 * summaryDetails.put("totalOutstandingAmount", dueInfo.getOutstanding());
	 * 
	 * memberReportDTO.setMemberDetails(memberDetails);
	 * memberReportDTO.setMemberReports(memberReports);
	 * memberReportDTO.setSummaryDetails(summaryDetails);
	 * memberReportDTO.setDisbursedDetails(disbursementDetails);
	 * memberReportDTO.setDueDetails(dueDetails);
	 * memberReportDTO.setReferencesDetails(referencesDetails);
	 * 
	 * return memberReportDTO; }
	 */
	// added by vaishnavi

	/*
	 * @Override public MemberReportDTO
	 * getAllRepaymentTrasactionsBydisbursmentid(Long disbursmentId) { logger.
	 * info("!!! inside class :PaymentServiceImpl ,method :getAllRepaymentTrasactionsBydisbursmentid() "
	 * );
	 * 
	 * List<Map<String, Object>> memberReports = new ArrayList<>(); MemberReportDTO
	 * memberReportDTO = new MemberReportDTO();
	 * 
	 * List<Object[]> reports =
	 * repaymentRepository.getAllRepaymentTrasactionsBydisbursmentid(disbursmentId);
	 * 
	 * 
	 * // Member details map Map<String, Object> memberDetails = new
	 * LinkedHashMap<>(); Map<String, Object> disbursementDetails = new
	 * LinkedHashMap<>();
	 * 
	 * List<Object[]> report = repaymentRepository.getMemeberDetails(disbursmentId);
	 * for (Object[] objects : report) { memberDetails.put("fullname", objects[0]);
	 * memberDetails.put("address", objects[1]); memberDetails.put("mobile_number",
	 * objects[2]); memberDetails.put("status", objects[3]);
	 * memberDetails.put("first_installment_date", objects[4]);
	 * memberDetails.put("interest_rate", objects[5]);
	 * memberDetails.put("monthly_emi", objects[6]);
	 * memberDetails.put("loan_account_number", objects[7]);
	 * memberDetails.put("loan_amount", objects[8]);
	 * memberDetails.put("loan_tenure", objects[9]);
	 * memberDetails.put("last_installment_date", objects[10]);
	 * memberDetails.put("due_amount", objects[11]); memberDetails.put("due_date",
	 * objects[12]); //memberDetails.put("no_of_notices", objects[14]);
	 * //memberDetails.put("type_of_loan", objects[3]);
	 * //memberDetails.put("monthly_emi", objects[7]);
	 * memberReportDTO.setMemberDetails(memberDetails);}
	 * 
	 * 
	 * // --------------- // 2) Load disbursement amount + date (for the first row)
	 * // --------------- BigDecimal disbursedAmount = BigDecimal.ZERO; LocalDate
	 * disbursedDate = null; List<Object[]> disbRows =
	 * repaymentRepository.getDisbursementInfo(disbursmentId); if (disbRows != null
	 * && !disbRows.isEmpty()) { Object[] d = disbRows.get(0); disbursedAmount =
	 * parseBigDecimal(d[0]); disbursedDate = parseToLocalDate(d[1]);
	 * disbursementDetails.put("disbursedAmount", disbursedAmount);
	 * disbursementDetails.put("disbursedDate", disbursedDate);
	 * memberReportDTO.setDisbursedDetails(disbursementDetails); }
	 * 
	 * // --------------- // 3) Load repayment schedule rows (principal + interest)
	 * // --------------- List<Object[]> repayments =
	 * repaymentRepository.getRepaymentsByDisbursmentId(disbursmentId); // We'll map
	 * repaymentId -> dueDate so payments can refer to due_date for the "Date"
	 * column Map<Long, LocalDate> repaymentDueDateMap = new HashMap<>(); // We'll
	 * create events list (unsorted now) class Event { LocalDate date; // the "Date"
	 * column in Excel (schedule/generation date) LocalDate paymentDate; // actual
	 * payment date for credit rows (nullable) String description; String mode;
	 * BigDecimal debit; BigDecimal credit; int priority; // used to order when same
	 * date } List<Event> events = new ArrayList<>();
	 * 
	 * if (repayments != null) { int emiIndex = 1; for (Object[] r : repayments) {
	 * // r = [repayment_id, due_date, principal_amount, interest, due_amount] Long
	 * repaymentId = r[0] == null ? null : Long.valueOf(r[0].toString()); LocalDate
	 * dueDate = parseToLocalDate(r[1]); BigDecimal principal =
	 * parseBigDecimal(r[2]); BigDecimal interest = parseBigDecimal(r[3]);
	 * BigDecimal dueAmount = parseBigDecimal(r[4]);
	 * 
	 * if (repaymentId != null) { repaymentDueDateMap.put(repaymentId, dueDate); }
	 * 
	 * // Principal event (Emi Amount) if (principal != null &&
	 * principal.compareTo(BigDecimal.ZERO) > 0) { Event ev = new Event(); ev.date =
	 * dueDate; ev.paymentDate = null; ev.description = ordinal(emiIndex) +
	 * " Emi Amount"; // "1st Emi Amount", "2nd Emi Amount", ... ev.mode = null;
	 * ev.debit = principal; ev.credit = null; ev.priority = 2; // principal before
	 * interest? we choose principal priority 2 events.add(ev); emiIndex++; }
	 * 
	 * // Interest event if (interest != null && interest.compareTo(BigDecimal.ZERO)
	 * > 0) { Event ev = new Event(); ev.date = dueDate; ev.paymentDate = null;
	 * ev.description = "interest charges"; ev.mode = null; ev.debit = interest;
	 * ev.credit = null; ev.priority = 3; events.add(ev); } } }
	 * 
	 * // --------------- // 4) Load repayment transactions (payments + penalty +
	 * other charges) // We reuse your existing method
	 * getAllRepaymentTrasactionsBydisbursmentid which returns: // [rt.id,
	 * rt.amount_paid, createddate, rt.description, rt.interest_component,
	 * rt.other_charges, // rt.payment_date, rt.payment_mode, rt.penalty_component,
	 * rt.principal_component, d.disbursment_id, // rt.repayment_id, updateddate,
	 * r.interest, r.principal_amount] // --------------- List<Object[]>
	 * transactions =
	 * repaymentRepository.getAllRepaymentTrasactionsBydisbursmentid(disbursmentId);
	 * if (transactions != null) { for (Object[] t : transactions) { // defensive
	 * parsing BigDecimal rtAmountPaid = parseBigDecimal(t[1]); LocalDate
	 * createdDate = parseToLocalDate(t[2]); String rtDescription = t[3] != null ?
	 * t[3].toString() : null; BigDecimal rtInterestComponent =
	 * parseBigDecimal(t[4]); BigDecimal rtOtherCharges = parseBigDecimal(t[5]);
	 * LocalDate rtPaymentDate = parseToLocalDate(t[6]); String rtPaymentMode = t[7]
	 * != null ? t[7].toString() : null; BigDecimal rtPenaltyComponent =
	 * parseBigDecimal(t[8]); BigDecimal rtPrincipalComponent =
	 * parseBigDecimal(t[9]); Long rtRepaymentId = t[11] == null ? null :
	 * Long.valueOf(t[11].toString());
	 * 
	 * // 4.a If there are penalty_component or other_charges (delayed charges) ->
	 * create debit event if (rtPenaltyComponent.compareTo(BigDecimal.ZERO) > 0) {
	 * Event ev = new Event(); // use createdDate for charge generation date
	 * (matches your screenshot pattern) ev.date = createdDate != null ? createdDate
	 * : repaymentDueDateMap.get(rtRepaymentId); ev.paymentDate = null;
	 * ev.description = "Delayed charges"; ev.mode = null; ev.debit =
	 * rtPenaltyComponent; ev.credit = null; ev.priority = 4; events.add(ev); } if
	 * (rtOtherCharges.compareTo(BigDecimal.ZERO) > 0) { // if 'other_charges'
	 * represent admin/other charges, add as separate event Event ev = new Event();
	 * ev.date = createdDate != null ? createdDate :
	 * repaymentDueDateMap.get(rtRepaymentId); ev.paymentDate = null; ev.description
	 * = rtDescription != null ? rtDescription : "Other charges"; ev.mode = null;
	 * ev.debit = rtOtherCharges; ev.credit = null; ev.priority = 4; events.add(ev);
	 * }
	 * 
	 * // 4.b If this rt row is an actual payment (amount_paid > 0) -> credit event
	 * if (rtAmountPaid.compareTo(BigDecimal.ZERO) > 0) { Event ev = new Event(); //
	 * Excel Date column should show schedule/due_date (if repayment id is present),
	 * else use payment date ev.date = (rtRepaymentId != null &&
	 * repaymentDueDateMap.containsKey(rtRepaymentId)) ?
	 * repaymentDueDateMap.get(rtRepaymentId) : (rtPaymentDate != null ?
	 * rtPaymentDate : createdDate); ev.paymentDate = rtPaymentDate; // actual
	 * payment date (Excel's Payment Date) ev.description = rtDescription != null &&
	 * !rtDescription.isBlank() ? rtDescription : "Emi Amount received"; ev.mode =
	 * rtPaymentMode; ev.debit = null; ev.credit = rtAmountPaid; ev.priority = 5; //
	 * payments after charges events.add(ev); }
	 * 
	 * // there might also be direct principal_component / interest_component
	 * entries separately recorded in rt // if needed we can create separate events
	 * for them, but above cover the visible items in your spreadsheet } }
	 * 
	 * // --------------- // 5) Load cheque return charges from check_deposit table
	 * and add as debit events // --------------- List<Object[]> checkDeposits =
	 * repaymentRepository.getCheckDepositsByDisbursmentId(disbursmentId); if
	 * (checkDeposits != null) { for (Object[] c : checkDeposits) { LocalDate
	 * postingDate = parseToLocalDate(c[0]); BigDecimal chequeReturnCharges =
	 * parseBigDecimal(c[1]); BigDecimal amount = parseBigDecimal(c[2]); // original
	 * cheque amount String reason = c[3] != null ? c[3].toString() :
	 * "Cheque return"; Long attachRepaymentId = c[4] == null ? null :
	 * Long.valueOf(c[4].toString());
	 * 
	 * if (chequeReturnCharges.compareTo(BigDecimal.ZERO) > 0) { Event ev = new
	 * Event(); ev.date = postingDate != null ? postingDate : (attachRepaymentId !=
	 * null ? repaymentDueDateMap.get(attachRepaymentId) : null); ev.paymentDate =
	 * null; ev.description = "Cheque return charges"; ev.mode = null; ev.debit =
	 * chequeReturnCharges; ev.credit = null; ev.priority = 4; events.add(ev); } } }
	 * 
	 * // --------------- // 6) Add disbursement event as the first debit row (so
	 * balance starts as loan amount) // --------------- if (disbursedAmount != null
	 * && disbursedAmount.compareTo(BigDecimal.ZERO) > 0) { Event e = new Event();
	 * e.date = disbursedDate; e.paymentDate = null; e.description = "Disbursed";
	 * e.mode = null; e.debit = disbursedAmount; e.credit = null; e.priority = 1; //
	 * ensure disbursed comes first on that date events.add(e); }
	 * 
	 * // --------------- // 7) Sort events: by date asc, then priority asc //
	 * --------------- events.sort((a, b) -> { if (a.date == null && b.date == null)
	 * return Integer.compare(a.priority, b.priority); if (a.date == null) return 1;
	 * if (b.date == null) return -1; int cmp = a.date.compareTo(b.date); if (cmp !=
	 * 0) return cmp; return Integer.compare(a.priority, b.priority); });
	 * 
	 * // --------------- // 8) Build memberReports rows and compute running balance
	 * // --------------- BigDecimal balance = BigDecimal.ZERO; int sno = 1; for
	 * (Event ev : events) { Map<String, Object> row = new LinkedHashMap<>();
	 * row.put("sno", sno++); row.put("date", ev.date); // the "Date" column
	 * (schedule/charge/generation date) row.put("paymentDate", ev.paymentDate); //
	 * actual payment date (nullable) row.put("description", ev.description);
	 * row.put("mode", ev.mode); row.put("debit", ev.debit != null ? ev.debit :
	 * null); row.put("credit", ev.credit != null ? ev.credit : null);
	 * 
	 * BigDecimal debit = ev.debit != null ? ev.debit : BigDecimal.ZERO; BigDecimal
	 * credit = ev.credit != null ? ev.credit : BigDecimal.ZERO;
	 * 
	 * balance = balance.add(debit).subtract(credit); row.put("balance", balance);
	 * 
	 * memberReports.add(row); }
	 * 
	 * // set into DTO memberReportDTO.setMemberReports(memberReports);
	 * List<Map<String, Object>> filteredEmis = new ArrayList<>(); ReceiptResponse
	 * dueInfo = receiptService.getReceiptInfo(disbursmentId); for (EmiDueInfoDTO
	 * emi : dueInfo.getEmiDueInfo()) { Map<String, Object> emiMap = new
	 * LinkedHashMap<>();
	 * 
	 * emiMap.put("emiNumber", emi.getEmiNumber()); emiMap.put("emiMonth",
	 * emi.getEmiMonth()); emiMap.put("dueDays", emi.getDueDays());
	 * emiMap.put("dueAmount", emi.getDueAmount());
	 * 
	 * // Calculate total due double totalDue = emi.getDueAmount() +
	 * emi.getPenalty() + emi.getLatePenalty() + emi.getChequeReturnCharge();
	 * emiMap.put("totalDue", totalDue);
	 * 
	 * filteredEmis.add(emiMap); }
	 * 
	 * // Put filtered list in dueDetails map Map<String, Object> dueDetails = new
	 * LinkedHashMap<>(); dueDetails.put("dueDetails", filteredEmis);
	 * memberReportDTO.setDueDetails(dueDetails);
	 * 
	 * Set<String> referenceSet = new HashSet<>(); Set<String> companySet = new
	 * HashSet<>();
	 * 
	 * List<Map<String, Object>> referenceList = new ArrayList<>(); List<Map<String,
	 * Object>> companyList = new ArrayList<>();
	 * 
	 * for (Object[] row : report) { // Reference details String refKey = row[22] +
	 * "-" + row[23] + "-" + row[24]; // fullName-knownForYears-mobile if
	 * (!referenceSet.contains(refKey)) { Map<String, Object> ref = new
	 * LinkedHashMap<>(); ref.put("fullName", row[22]); ref.put("knownForYears",
	 * row[23]); ref.put("mobileNumber", row[24]); ref.put("relationship", row[25]);
	 * referenceList.add(ref); // add Map to the list referenceSet.add(refKey); //
	 * add key to the Set for uniqueness }
	 * 
	 * // Company details String companyKey = row[20] + "-" + row[21]; //
	 * name-address if (!companySet.contains(companyKey)) { Map<String, Object>
	 * companyMap = new LinkedHashMap<>(); companyMap.put("companyName", row[20]);
	 * companyMap.put("companyAddress", row[21]); companyList.add(companyMap);
	 * companySet.add(companyKey); } }
	 * 
	 * // finally set into DTO Map<String, Object> referenceDetails = new
	 * LinkedHashMap<>(); referenceDetails.put("references", referenceList);
	 * memberReportDTO.setReferencesDetails(referenceDetails);
	 * 
	 * Map<String, Object> otherDetails = new LinkedHashMap<>();
	 * otherDetails.put("companyDetails", companyList);
	 * memberReportDTO.setOtherDetails(otherDetails); // (You may also want to
	 * recompute & set summaryDetails like totals; I can add that if needed) return
	 * memberReportDTO;}
	 */
	// end method
	
	
	@Override
	public MemberReportDTO getAllRepaymentTrasactionsBydisbursmentid(Long disbursmentId) {
	    logger.info("!!! inside PaymentServiceImpl.getAllRepaymentTrasactionsBydisbursmentid()");

	    MemberReportDTO memberReportDTO = new MemberReportDTO();
	    List<Map<String, Object>> memberReports = new ArrayList<>();

	    // ================= MEMBER DETAILS =================
	    Map<String, Object> memberDetails = new LinkedHashMap<>();
	    List<Object[]> report = repaymentRepository.getMemeberDetails(disbursmentId);

	    if (!report.isEmpty()) {
	        Object[] objects = report.get(0); // assuming only one member per disbursement
	        memberDetails.put("fullname", objects[0]);
	        memberDetails.put("address", objects[1]);
	        memberDetails.put("mobile_number", objects[2]);
	        memberDetails.put("status", objects[3]);
	        memberDetails.put("first_installment_date", objects[4]);
	        memberDetails.put("interest_rate", objects[5]);
	        memberDetails.put("monthly_emi", objects[6]);
	        memberDetails.put("loan_account_number", objects[7]);
	        memberDetails.put("loan_amount", objects[8]);
	        memberDetails.put("loan_tenure", objects[9]);
	        memberDetails.put("last_installment_date", objects[10]);
	        memberDetails.put("due_amount", objects[11]);
	        memberDetails.put("due_date", objects[12]);
	    }

	    // ================= STATUS =================
	    ReceiptResponse dueInfo = receiptService.getReceiptInfoByReceiptDate(disbursmentId, LocalDate.now());
	    BigDecimal outstanding = BigDecimal.valueOf(dueInfo.getOutstanding());

	    memberDetails.put("status", outstanding.compareTo(BigDecimal.ZERO) == 0 ? "Closed" : "Active");
	    memberDetails.put("outstanding", outstanding);

	    Date lastPaidDate = dueInfo.getLastPaidDate();
	    memberDetails.put("last_paid_date", lastPaidDate != null
	            ? lastPaidDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
	            : null);

	    memberReportDTO.setMemberDetails(memberDetails);
	 // ================= REPORT =================
	    int sno = 1;
	    BigDecimal runningBalance = BigDecimal.ZERO;

	    Disbursment disb = disbursmentRepository.findById(disbursmentId)
	            .orElseThrow(() -> new RuntimeException("Disbursement not found"));

	    LoanSanction loanSanction = disb.getLoanSanction();
	    BigDecimal sanctionedAmount = BigDecimal.valueOf(loanSanction.getAmountSanctioned());
	    BigDecimal emiAmount = BigDecimal.valueOf(loanSanction.getEmiAmount());

	    // -------- DISBURSED --------
	    Map<String, Object> disbursementRow = new LinkedHashMap<>();
	    disbursementRow.put("sno", sno++);
	    disbursementRow.put("date", disb.getDate());
	    disbursementRow.put("description", "Disbursed");
	    disbursementRow.put("mode", null);
	    disbursementRow.put("debit", sanctionedAmount);
	    disbursementRow.put("credit", null);
	    disbursementRow.put("balance", BigDecimal.ZERO);
	    memberReports.add(disbursementRow);

	    // ================= DELAYED CHARGES =================
	    Date lastPaymentDate = repaymentTransactionRepository.findLastPaidDateByDisbursmentId(disbursmentId);
	    if (lastPaymentDate != null) {
	        BigDecimal delayedCharges = BigDecimal.valueOf(dueInfo.getTotalPenalty());
	        if (delayedCharges.compareTo(BigDecimal.ZERO) > 0) {
	            runningBalance = runningBalance.add(delayedCharges);

	            Map<String, Object> delayedChargesRow = new LinkedHashMap<>();
	            delayedChargesRow.put("sno", sno++);
	            delayedChargesRow.put("date", lastPaymentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
	            delayedChargesRow.put("description", "Delayed Charges");
	            delayedChargesRow.put("mode", null);
	            delayedChargesRow.put("debit", delayedCharges);
	            delayedChargesRow.put("credit", null);
	            delayedChargesRow.put("balance", runningBalance);
	            memberReports.add(delayedChargesRow);
	        }
	    }

	    // -------- FIRST EMI --------
	    LocalDate firstEmiDate = disb.getDate().plusMonths(1)
	            .withDayOfMonth(disb.getDate().plusMonths(1).lengthOfMonth());

	    runningBalance = runningBalance.add(emiAmount); // add EMI after delayed charges
	    Map<String, Object> firstEmiRow = new LinkedHashMap<>();
	    firstEmiRow.put("sno", sno++);
	    firstEmiRow.put("date", firstEmiDate);
	    firstEmiRow.put("description", "1st EMI Amount");
	    firstEmiRow.put("mode", null);
	    firstEmiRow.put("debit", emiAmount);
	    firstEmiRow.put("credit", null);
	    firstEmiRow.put("balance", runningBalance);
	    memberReports.add(firstEmiRow);

	    // ================= TRANSACTIONS =================
	    List<Object[]> transactions = repaymentRepository.getAllRepaymentTrasactionsBydisbursmentid(disbursmentId);
	    Set<Long> processedRepaymentIds = new HashSet<>();

	    for (Object[] t : transactions) {
	        Long repaymentId = t[11] != null ? ((Number) t[11]).longValue() : null;
	        if (repaymentId == null || processedRepaymentIds.contains(repaymentId)) continue;

	        Double amountPaid = t[1] != null ? ((Number) t[1]).doubleValue() : 0.0;
	        LocalDate txnDate = t[6] != null ? LocalDate.parse(t[6].toString().substring(0, 10)) : null;
	        String paymentMode = t[7] != null ? t[7].toString() : null;
	        Double chequeReturnCharges = t[5] != null ? ((Number) t[5]).doubleValue() : 0.0;

	        // ---- Amount Received
	        Map<String, Object> creditRow = new LinkedHashMap<>();
	        creditRow.put("sno", sno++);
	        creditRow.put("date", txnDate);
	        creditRow.put("description", "Amount Received");
	        creditRow.put("mode", paymentMode);
	        creditRow.put("debit", null);
	        creditRow.put("credit", amountPaid);

	        runningBalance = runningBalance.subtract(BigDecimal.valueOf(amountPaid));
	        creditRow.put("balance", runningBalance);
	        memberReports.add(creditRow);

	        // ---- Cheque Return Charges
	        if ("CHEQUE".equalsIgnoreCase(paymentMode) && chequeReturnCharges > 0) {
	            Map<String, Object> chequeReturnRow = new LinkedHashMap<>();
	            chequeReturnRow.put("sno", sno++);
	            chequeReturnRow.put("date", txnDate);
	            chequeReturnRow.put("description", "Cheque Return Charges");
	            chequeReturnRow.put("mode", "CHEQUE");
	            chequeReturnRow.put("debit", chequeReturnCharges);
	            chequeReturnRow.put("credit", null);

	            runningBalance = runningBalance.add(BigDecimal.valueOf(chequeReturnCharges));
	            chequeReturnRow.put("balance", runningBalance);
	            memberReports.add(chequeReturnRow);
	        }

	        processedRepaymentIds.add(repaymentId);
	        break;
	    }

	    memberReportDTO.setMemberReports(memberReports);

	    // ================= EMI DUE DETAILS =================
	    List<Map<String, Object>> filteredEmis = new ArrayList<>();
	    for (EmiDueInfoDTO emi : dueInfo.getEmiDueInfo()) {
	        Map<String, Object> emiMap = new LinkedHashMap<>();
	        emiMap.put("emiNumber", emi.getEmiNumber());
	        emiMap.put("emiMonth", emi.getEmiMonth());
	        emiMap.put("dueDays", emi.getDueDays());
	        emiMap.put("dueAmount", emi.getDueAmount());
	        double totalDue = emi.getDueAmount() + emi.getPenalty() + emi.getLatePenalty() + emi.getChequeReturnCharge();
	        emiMap.put("totalDue", totalDue);
	        filteredEmis.add(emiMap);
	    }

	    Map<String, Object> dueDetails = new LinkedHashMap<>();
	    dueDetails.put("dueDetails", filteredEmis);
	    memberReportDTO.setDueDetails(dueDetails);

	    // ================= REFERENCES DETAILS =================
	    Set<String> referenceSet = new HashSet<>();
	    List<Map<String, Object>> referenceList = new ArrayList<>();

	    for (Object[] row : report) {
	        String refKey = row[22] + "-" + row[23] + "-" + row[24];
	        if (!referenceSet.contains(refKey)) {
	            Map<String, Object> ref = new LinkedHashMap<>();
	            ref.put("fullName", row[22]);
	            ref.put("knownForYears", row[23]);
	            ref.put("mobileNumber", row[24]);
	            ref.put("relationship", row[25]);
	            referenceList.add(ref);
	            referenceSet.add(refKey);
	        }
	    }

	    Map<String, Object> referenceDetails = new LinkedHashMap<>();
	    referenceDetails.put("references", referenceList);
	    memberReportDTO.setReferencesDetails(referenceDetails);

	    // ================= COMPANY DETAILS =================
	    Set<String> companySet = new HashSet<>();
	    List<Map<String, Object>> companyList = new ArrayList<>();

	    for (Object[] row : report) {
	        String companyKey = row[20] + "-" + row[21];
	        if (!companySet.contains(companyKey)) {
	            Map<String, Object> companyMap = new LinkedHashMap<>();
	            companyMap.put("companyName", row[20]);
	            companyMap.put("companyAddress", row[21]);
	            companyList.add(companyMap);
	            companySet.add(companyKey);
	        }
	    }

	    Map<String, Object> otherDetails = new LinkedHashMap<>();
	    otherDetails.put("companyDetails", companyList);
	    memberReportDTO.setOtherDetails(otherDetails);

	    return memberReportDTO;
	}

	/*@Override
	public MemberReportDTO getAllRepaymentTrasactionsBydisbursmentid(Long disbursmentId) {
	    logger.info("!!! inside PaymentServiceImpl.getAllRepaymentTrasactionsBydisbursmentid()");

	    MemberReportDTO memberReportDTO = new MemberReportDTO();
	    List<Map<String, Object>> memberReports = new ArrayList<>();

	    // ================= MEMBER DETAILS =================
	    Map<String, Object> memberDetails = new LinkedHashMap<>();
	    List<Object[]> report = repaymentRepository.getMemeberDetails(disbursmentId);

	    if (!report.isEmpty()) {
	        Object[] objects = report.get(0); // assuming only one member per disbursement
	        memberDetails.put("fullname", objects[0]);
	        memberDetails.put("address", objects[1]);
	        memberDetails.put("mobile_number", objects[2]);
	        memberDetails.put("status", objects[3]);
	        memberDetails.put("first_installment_date", objects[4]);
	        memberDetails.put("interest_rate", objects[5]);
	        memberDetails.put("monthly_emi", objects[6]);
	        memberDetails.put("loan_account_number", objects[7]);
	        memberDetails.put("loan_amount", objects[8]);
	        memberDetails.put("loan_tenure", objects[9]);
	        memberDetails.put("last_installment_date", objects[10]);
	        memberDetails.put("due_amount", objects[11]);
	        memberDetails.put("due_date", objects[12]);
	    }

	    // ================= STATUS =================
	    ReceiptResponse dueInfo = receiptService.getReceiptInfoByReceiptDate(disbursmentId, LocalDate.now());
	    BigDecimal outstanding = BigDecimal.valueOf(dueInfo.getOutstanding());

	    memberDetails.put("status", outstanding.compareTo(BigDecimal.ZERO) == 0 ? "Closed" : "Active");
	    memberDetails.put("outstanding", outstanding);

	    Date lastPaidDate = dueInfo.getLastPaidDate();
	    memberDetails.put("last_paid_date", lastPaidDate != null
	            ? lastPaidDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
	            : null);

	    memberReportDTO.setMemberDetails(memberDetails);

	    // ================= REPORT =================
	    int sno = 1;
	    BigDecimal runningBalance = BigDecimal.ZERO;

	    Disbursment disb = disbursmentRepository.findById(disbursmentId)
	            .orElseThrow(() -> new RuntimeException("Disbursement not found"));

	    LoanSanction loanSanction = disb.getLoanSanction();
	    BigDecimal sanctionedAmount = BigDecimal.valueOf(loanSanction.getAmountSanctioned());
	    BigDecimal emiAmount = BigDecimal.valueOf(loanSanction.getEmiAmount());

	    // -------- DISBURSED --------
	    Map<String, Object> disbursementRow = new LinkedHashMap<>();
	    disbursementRow.put("sno", sno++);
	    disbursementRow.put("date", disb.getDate());
	    disbursementRow.put("description", "Disbursed");
	    disbursementRow.put("mode", null);
	    disbursementRow.put("debit", sanctionedAmount);
	    disbursementRow.put("credit", null);
	    disbursementRow.put("balance", BigDecimal.ZERO);
	    memberReports.add(disbursementRow);

	    // ================= TRANSACTIONS (2nd EMI onwards, 4-row cycle) =================
	    List<Object[]> transactions = repaymentRepository.getAllRepaymentTrasactionsBydisbursmentid(disbursmentId);
	    Set<Long> processedRepaymentIds = new HashSet<>();

	    BigDecimal totalDelayedCharges = BigDecimal.valueOf(dueInfo.getTotalPenalty()); // Option 1

	    for (Object[] t : transactions) {
	        Long repaymentId = t[11] != null ? ((Number) t[11]).longValue() : null;
	        if (repaymentId == null || processedRepaymentIds.contains(repaymentId)) continue;

	        BigDecimal emiPaidAmount = parseBigDecimal(t[1]);
	        BigDecimal chequeReturnCharges = parseBigDecimal(t[5]);
	        LocalDate txnDate = t[6] != null ? LocalDate.parse(t[6].toString().substring(0, 10)) : null;
	        String paymentMode = t[7] != null ? t[7].toString() : null;

	        // ---- EMI Amount
	        Map<String, Object> emiRow = new LinkedHashMap<>();
	        emiRow.put("sno", sno++);
	        emiRow.put("date", txnDate);
	        emiRow.put("description", repaymentId + " EMI Amount");
	        emiRow.put("mode", null);
	        emiRow.put("debit", emiPaidAmount);
	        emiRow.put("credit", null);
	        runningBalance = runningBalance.add(emiPaidAmount);
	        emiRow.put("balance", runningBalance);
	        memberReports.add(emiRow);

	        // ---- Delayed Charges (from dueInfo)
	        if (totalDelayedCharges.compareTo(BigDecimal.ZERO) > 0) {
	            Map<String, Object> delayedRow = new LinkedHashMap<>();
	            delayedRow.put("sno", sno++);
	            delayedRow.put("date", txnDate);
	            delayedRow.put("description", "Delayed Charges");
	            delayedRow.put("mode", null);
	            delayedRow.put("debit", totalDelayedCharges);
	            delayedRow.put("credit", null);
	            runningBalance = runningBalance.add(totalDelayedCharges);
	            delayedRow.put("balance", runningBalance);
	            memberReports.add(delayedRow);
	        }

	        // ---- Amount Received
	        Map<String, Object> receivedRow = new LinkedHashMap<>();
	        receivedRow.put("sno", sno++);
	        receivedRow.put("date", txnDate);
	        receivedRow.put("description", "Amount Received");
	        receivedRow.put("mode", paymentMode);
	        receivedRow.put("debit", null);
	        receivedRow.put("credit", emiPaidAmount);
	        runningBalance = runningBalance.subtract(emiPaidAmount);
	        receivedRow.put("balance", runningBalance);
	        memberReports.add(receivedRow);

	        // ---- Cheque Return Charges
	        if ("CHEQUE".equalsIgnoreCase(paymentMode) && chequeReturnCharges.compareTo(BigDecimal.ZERO) > 0) {
	            Map<String, Object> chequeRow = new LinkedHashMap<>();
	            chequeRow.put("sno", sno++);
	            chequeRow.put("date", txnDate);
	            chequeRow.put("description", "Cheque Return Charges");
	            chequeRow.put("mode", "CHEQUE");
	            chequeRow.put("debit", chequeReturnCharges);
	            chequeRow.put("credit", null);
	            runningBalance = runningBalance.add(chequeReturnCharges);
	            chequeRow.put("balance", runningBalance);
	            memberReports.add(chequeRow);
	        }

	        processedRepaymentIds.add(repaymentId);
	    }

	    memberReportDTO.setMemberReports(memberReports);

	    // ================= EMI DUE DETAILS =================
	    List<Map<String, Object>> filteredEmis = new ArrayList<>();
	    for (EmiDueInfoDTO emi : dueInfo.getEmiDueInfo()) {
	        Map<String, Object> emiMap = new LinkedHashMap<>();
	        emiMap.put("emiNumber", emi.getEmiNumber());
	        emiMap.put("emiMonth", emi.getEmiMonth());
	        emiMap.put("dueDays", emi.getDueDays());
	        emiMap.put("dueAmount", emi.getDueAmount());
	        double totalDue = emi.getDueAmount() + emi.getPenalty() + emi.getLatePenalty() + emi.getChequeReturnCharge();
	        emiMap.put("totalDue", totalDue);
	        filteredEmis.add(emiMap);
	    }

	    Map<String, Object> dueDetails = new LinkedHashMap<>();
	    dueDetails.put("dueDetails", filteredEmis);
	    memberReportDTO.setDueDetails(dueDetails);

	    // ================= REFERENCES DETAILS =================
	    Set<String> referenceSet = new HashSet<>();
	    List<Map<String, Object>> referenceList = new ArrayList<>();

	    for (Object[] row : report) {
	        String refKey = row[22] + "-" + row[23] + "-" + row[24];
	        if (!referenceSet.contains(refKey)) {
	            Map<String, Object> ref = new LinkedHashMap<>();
	            ref.put("fullName", row[22]);
	            ref.put("knownForYears", row[23]);
	            ref.put("mobileNumber", row[24]);
	            ref.put("relationship", row[25]);
	            referenceList.add(ref);
	            referenceSet.add(refKey);
	        }
	    }

	    Map<String, Object> referenceDetails = new LinkedHashMap<>();
	    referenceDetails.put("references", referenceList);
	    memberReportDTO.setReferencesDetails(referenceDetails);

	    // ================= COMPANY DETAILS =================
	    Set<String> companySet = new HashSet<>();
	    List<Map<String, Object>> companyList = new ArrayList<>();

	    for (Object[] row : report) {
	        String companyKey = row[20] + "-" + row[21];
	        if (!companySet.contains(companyKey)) {
	            Map<String, Object> companyMap = new LinkedHashMap<>();
	            companyMap.put("companyName", row[20]);
	            companyMap.put("companyAddress", row[21]);
	            companyList.add(companyMap);
	            companySet.add(companyKey);
	        }
	    }

	    Map<String, Object> otherDetails = new LinkedHashMap<>();
	    otherDetails.put("companyDetails", companyList);
	    memberReportDTO.setOtherDetails(otherDetails);

	    return memberReportDTO;
	}

	// ---------------- Safe Parsing Methods ----------------
	private BigDecimal parseBigDecimal(Object obj) {
	    if (obj == null) return BigDecimal.ZERO;
	    if (obj instanceof Number) return BigDecimal.valueOf(((Number) obj).doubleValue());
	    try {
	        return new BigDecimal(obj.toString());
	    } catch (NumberFormatException e) {
	        return BigDecimal.ZERO;
	    }
	}
*/

		
		
		
		

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

//	private BigDecimal parseBigDecimal(Object o) {
//		if (o == null)
//			return BigDecimal.ZERO;
//		try {
//			return new BigDecimal(o.toString());
//		} catch (Exception ex) {
//			return BigDecimal.ZERO;
//		}
//	}

	private LocalDate parseToLocalDate(Object o) {
		if (o == null)
			return null;
		try {
			if (o instanceof java.sql.Date) {
				return ((java.sql.Date) o).toLocalDate();
			} else if (o instanceof java.sql.Timestamp) {
				return ((java.sql.Timestamp) o).toLocalDateTime().toLocalDate();
			} else if (o instanceof java.time.LocalDate) {
				return (LocalDate) o;
			} else if (o instanceof java.time.LocalDateTime) {
				return ((java.time.LocalDateTime) o).toLocalDate();
			} else {
				// if the query returned a String e.g. '2024-05-08 12:00:00'
				String s = o.toString();
				// try common formats
				if (s.length() >= 10) {
					String datePart = s.substring(0, 10);
					return LocalDate.parse(datePart); // ISO yyyy-MM-dd
				}
				return null;
			}
		} catch (Exception ex) {
			return null;
		}
	}

	private String ordinal(int i) {
		// return "1st", "2nd", "3rd", "4th"...
		int mod100 = i % 100;
		int mod10 = i % 10;
		if (mod100 - mod10 == 10) { // 11,12,13
			return i + "th";
		}
		switch (mod10) {
		case 1:
			return i + "st";
		case 2:
			return i + "nd";
		case 3:
			return i + "rd";
		default:
			return i + "th";
		}
	}
	}
