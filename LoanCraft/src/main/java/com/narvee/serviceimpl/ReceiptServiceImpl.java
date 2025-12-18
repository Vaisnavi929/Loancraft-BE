package com.narvee.serviceimpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.Service.ReceiptService;
import com.narvee.Service.RepaymentService;
import com.narvee.controller.PayRequest;
import com.narvee.entity.CheckDeposit;
import com.narvee.entity.Disbursment;
import com.narvee.entity.LmsRepayment;
import com.narvee.entity.RepaymentTransactions;
import com.narvee.helpers.EmiDueInfoDTO;
import com.narvee.repository.CheckDepositRepository;
import com.narvee.repository.RepaymentRepository;
import com.narvee.repository.RepaymentTransactionsRepository;
import com.narvee.request.dto.PaymentStatementDTO;
import com.narvee.response.PenaltyResponse;
import com.narvee.response.ReceiptResponse;
import com.narvee.util.PenaltyUtil;

@Service
public class ReceiptServiceImpl implements ReceiptService {
	private static final Logger logger = LoggerFactory.getLogger(ReceiptServiceImpl.class);

	@Autowired
	private RepaymentService repaymentService;

	@Autowired
	private RepaymentRepository repaymentRepository;

	@Autowired
	private CheckDepositRepository checkDepositRepository;

	@Autowired
	private RepaymentTransactionsRepository transactionsRepository;

//	@Override
//	public ReceiptResponse getReceiptInfo(Long dId)               {
//		logger.info("!!! inside class:ReceiptServiceImpl, method: getReceiptInfo() ");
//		ReceiptResponse receipetResponse = new ReceiptResponse();
//		Date lastPaidDate = transactionsRepository.findLastPaidDateByDisbursmentId(dId);
//		List<PaymentStatementDTO> re = repaymentService.getRepaymentStatement(dId);
//
//		BigDecimal totalInterest = BigDecimal.ZERO;
//		BigDecimal principal = BigDecimal.ZERO;
//		BigDecimal repaid = BigDecimal.ZERO;
//		BigDecimal outstanding = BigDecimal.ZERO;
//		BigDecimal totalDue = BigDecimal.ZERO;
//		BigDecimal loanAmount = BigDecimal.ZERO;
//		BigDecimal emiPenalty = BigDecimal.ZERO;
//		BigDecimal totalPenalty = BigDecimal.ZERO;
//		BigDecimal chequeReturnCharge = BigDecimal.ZERO;
//		BigDecimal latePenalty = BigDecimal.ZERO;
//
//		int emiCounter = 0;
//		int numberOfEmisDue = 0;
//
//		LocalDate now = LocalDate.now();
//		List<EmiDueInfoDTO> emiDueDetails = new ArrayList<>();
//
//		for (PaymentStatementDTO paymentStatementDTO : re) {
//			emiCounter++;
//			LocalDate dueDate = paymentStatementDTO.getDueDate();
//
//			List<RepaymentTransactions> checkLatePenalty = transactionsRepository
//					.findByRepaymentRepaymentId(paymentStatementDTO.getRepaymentId());
//
//			double totalLatePenalty = checkLatePenalty.stream()
//					.filter(p -> p.getLatePenalty() != null && p.getLatePenalty() > 0)
//					.mapToDouble(RepaymentTransactions::getLatePenalty).sum();
//
//			double paidCheckBounce = checkLatePenalty.stream()
//					.filter(p -> p.getChequeReturnCharges() != null && p.getChequeReturnCharges() > 0)
//					.mapToDouble(RepaymentTransactions::getChequeReturnCharges).sum();
//
//			BigDecimal interest = BigDecimal.valueOf(paymentStatementDTO.getInterest()).setScale(2,
//					RoundingMode.HALF_UP);
//			BigDecimal principalAmt = BigDecimal.valueOf(paymentStatementDTO.getPrincipalAmount()).setScale(2,
//					RoundingMode.HALF_UP);
//			BigDecimal amountPaid = BigDecimal.valueOf(paymentStatementDTO.getAmountPaid()).setScale(2,
//					RoundingMode.HALF_UP);
//			BigDecimal dueAmount = BigDecimal.valueOf(paymentStatementDTO.getDueAmount()).setScale(2,
//					RoundingMode.HALF_UP);
//
//			totalInterest = totalInterest.add(interest);
//			repaid = repaid.add(amountPaid);
//			outstanding = outstanding.add(principalAmt);
//
//			loanAmount = BigDecimal.valueOf(paymentStatementDTO.getAmountSanctioned()).setScale(2,
//					RoundingMode.HALF_UP);
//
//			if ((dueDate.getYear() < now.getYear()
//					|| (dueDate.getYear() == now.getYear() && dueDate.getMonthValue() <= now.getMonthValue()))
//					&& dueAmount.compareTo(BigDecimal.ZERO) != 0) {
//				receipetResponse.setDueDate(dueDate);
//				totalDue = totalDue.add(dueAmount);
//			}
//
//			// Reset for each EMI
//			latePenalty = BigDecimal.ZERO;
//			chequeReturnCharge = BigDecimal.ZERO;
//			boolean hasChequeReturnCharges = false;
//
//			List<CheckDeposit> cheques = checkDepositRepository
//					.LmsRepaymentRepaymentId(paymentStatementDTO.getRepaymentId());
//
//			for (CheckDeposit cheque : cheques) {
//				if (cheque != null && !cheque.getStatus().equalsIgnoreCase("Cleared")
//						&& !paymentStatementDTO.getAmountStatus().equalsIgnoreCase("PAID")) {
//
//					double charges = cheque.getChequeReturnCharges() != null ? cheque.getChequeReturnCharges() : 0.0;
//
//					if (charges > 0) {
//						hasChequeReturnCharges = true;
//						chequeReturnCharge = chequeReturnCharge
//								.add(BigDecimal.valueOf(charges).setScale(2, RoundingMode.HALF_UP));
//					}
//				}
//			}
//
//			// Subtract already paid cheque bounce charges
//			chequeReturnCharge = chequeReturnCharge
//					.subtract(BigDecimal.valueOf(paidCheckBounce).setScale(2, RoundingMode.HALF_UP));
//
//			if (chequeReturnCharge.compareTo(BigDecimal.ZERO) < 0) {
//				chequeReturnCharge = BigDecimal.ZERO;
//			}
//
//			// Late penalty only if no cheque return charges
//			if (!hasChequeReturnCharges && now.isAfter(dueDate) && totalLatePenalty < 500
//					&& dueAmount.compareTo(BigDecimal.ZERO) != 0) {
//				latePenalty = BigDecimal.valueOf(500 - totalLatePenalty);
//			}
//
//			if (now.isAfter(dueDate) && dueAmount.compareTo(BigDecimal.ZERO) != 0) {
//				numberOfEmisDue++;
//				
//				LocalDate firstOfMonth = paymentStatementDTO.getLastPaidDate();
//				if (firstOfMonth == null) {
//					firstOfMonth = dueDate.withDayOfMonth(1);
//
//				}
//
//				long overdueDays = ChronoUnit.DAYS.between(firstOfMonth, now);
//				double dailyPenaltyRate = 0.03 / 30;
//
//				BigDecimal currentEmiPenalty = dueAmount.multiply(BigDecimal.valueOf(dailyPenaltyRate))
//						.multiply(BigDecimal.valueOf(overdueDays)).setScale(2, RoundingMode.HALF_UP);
//
//				emiPenalty = emiPenalty.add(currentEmiPenalty);
//				totalPenalty = totalPenalty.add(currentEmiPenalty).add(chequeReturnCharge).add(latePenalty).setScale(2,
//						RoundingMode.HALF_UP);
//
//				String emiMonth = dueDate.getMonth().toString() + " " + dueDate.getYear();
//				emiDueDetails.add(new EmiDueInfoDTO(emiCounter, dueDate, overdueDays, dueAmount.doubleValue(),
//						chequeReturnCharge.doubleValue(), currentEmiPenalty.doubleValue(), emiMonth,
//						latePenalty.doubleValue()));
//			} else if (dueDate.getYear() == now.getYear() && dueDate.getMonthValue() == now.getMonthValue()
//					&& dueAmount.compareTo(BigDecimal.ZERO) != 0) {
//				numberOfEmisDue++;
//				long overdueDays = 0;
//				String emiMonth = dueDate.getMonth().toString() + " " + dueDate.getYear();
//				emiDueDetails.add(new EmiDueInfoDTO(emiCounter, dueDate, overdueDays, dueAmount.doubleValue(),
//						chequeReturnCharge.doubleValue(), 0, emiMonth, latePenalty.doubleValue()));
//			}
//
//			totalLatePenalty = 0;
//		}
//
//		receipetResponse.setInterest(roundToNearestRupee(totalInterest));
//		receipetResponse.setPrincipal(roundToNearestRupee(principal));
//		receipetResponse.setRepaid(roundToNearestRupee(repaid));
//		receipetResponse.setOutstanding(roundToNearestRupee(outstanding));
//		receipetResponse.setTotalDue(roundToNearestRupee(totalDue));
//		receipetResponse.setNumberOfEmisDue(numberOfEmisDue);
//		receipetResponse.setLoanAmount(roundToNearestRupee(loanAmount));
//		receipetResponse.setEmiDueInfo(emiDueDetails);
//		receipetResponse.setTotalPenalty(roundToNearestRupee(totalPenalty));
//		receipetResponse.setTotalDue(roundToNearestRupee(totalDue.add(totalPenalty)));
//		receipetResponse.setEmiDueAmount(roundToNearestRupee(totalDue));
//		receipetResponse.setLastPaidDate(lastPaidDate);
//
//		logger.info("!!! inside class:ReceiptServiceImpl, method: getReceiptInfo() END");
//		return receipetResponse;
//	}

//	@Override
//	public ReceiptResponse getReceiptInfoByReceiptDate(Long dId, LocalDate localDate) {
//		logger.info("!!! inside class:ReceiptServiceImpl, method: getReceiptInfoByReceiptDate");
//		ReceiptResponse receipetResponse = new ReceiptResponse();
//		Date lastPaidDate = transactionsRepository.findLastPaidDateByDisbursmentId(dId);
//		List<PaymentStatementDTO> re = repaymentService.getRepaymentStatement(dId);
//
//		BigDecimal totalInterest = BigDecimal.ZERO;
//		BigDecimal principal = BigDecimal.ZERO;
//		BigDecimal repaid = BigDecimal.ZERO;
//		BigDecimal outstanding = BigDecimal.ZERO;
//		BigDecimal totalDue = BigDecimal.ZERO;
//		BigDecimal loanAmount = BigDecimal.ZERO;
//		BigDecimal emiPenalty = BigDecimal.ZERO;
//		BigDecimal totalPenalty = BigDecimal.ZERO;
//		BigDecimal chequeReturnCharge = BigDecimal.ZERO;
//		BigDecimal latePenalty = BigDecimal.ZERO;
//
//		int emiCounter = 0;
//		int numberOfEmisDue = 0;
//
//		LocalDate now = localDate;
//		List<EmiDueInfoDTO> emiDueDetails = new ArrayList<>();
//
//		for (PaymentStatementDTO paymentStatementDTO : re) {
//			emiCounter++;
//			LocalDate dueDate = paymentStatementDTO.getDueDate();
//
//			List<RepaymentTransactions> checkLatePenalty = transactionsRepository
//					.findByRepaymentRepaymentId(paymentStatementDTO.getRepaymentId());
//
//			double totalLatePenalty = checkLatePenalty.stream()
//					.filter(p -> p.getLatePenalty() != null && p.getLatePenalty() > 0)
//					.mapToDouble(RepaymentTransactions::getLatePenalty).sum();
//
//			double paidCheckBounce = checkLatePenalty.stream()
//					.filter(p -> p.getChequeReturnCharges() != null && p.getChequeReturnCharges() > 0)
//					.mapToDouble(RepaymentTransactions::getChequeReturnCharges).sum();
//
//			BigDecimal interest = BigDecimal.valueOf(paymentStatementDTO.getInterest()).setScale(2,
//					RoundingMode.HALF_UP);
//			BigDecimal principalAmt = BigDecimal.valueOf(paymentStatementDTO.getPrincipalAmount()).setScale(2,
//					RoundingMode.HALF_UP);
//			BigDecimal amountPaid = BigDecimal.valueOf(paymentStatementDTO.getAmountPaid()).setScale(2,
//					RoundingMode.HALF_UP);
//			BigDecimal dueAmount = BigDecimal.valueOf(paymentStatementDTO.getDueAmount()).setScale(2,
//					RoundingMode.HALF_UP);
//
//			totalInterest = totalInterest.add(interest);
//			repaid = repaid.add(amountPaid);
//			outstanding = outstanding.add(principalAmt);
//
//			loanAmount = BigDecimal.valueOf(paymentStatementDTO.getAmountSanctioned()).setScale(2,
//					RoundingMode.HALF_UP);
//
//			if ((dueDate.getYear() < now.getYear()
//					|| (dueDate.getYear() == now.getYear() && dueDate.getMonthValue() <= now.getMonthValue()))
//					&& dueAmount.compareTo(BigDecimal.ZERO) != 0) {
//				receipetResponse.setDueDate(dueDate);
//				totalDue = totalDue.add(dueAmount);
//			}
//
//			// Reset for each EMI
//			latePenalty = BigDecimal.ZERO;
//			chequeReturnCharge = BigDecimal.ZERO;
//			boolean hasChequeReturnCharges = false;
//
//			List<CheckDeposit> cheques = checkDepositRepository
//					.LmsRepaymentRepaymentId(paymentStatementDTO.getRepaymentId());
//
//			for (CheckDeposit cheque : cheques) {
//				if (cheque != null && !cheque.getStatus().equalsIgnoreCase("Cleared")
//						&& !paymentStatementDTO.getAmountStatus().equalsIgnoreCase("PAID")) {
//
//					double charges = cheque.getChequeReturnCharges() != null ? cheque.getChequeReturnCharges() : 0.0;
//
//					if (charges > 0) {
//						hasChequeReturnCharges = true;
//						chequeReturnCharge = chequeReturnCharge
//								.add(BigDecimal.valueOf(charges).setScale(2, RoundingMode.HALF_UP));
//					}
//				}
//			}
//
//			// Subtract already paid cheque bounce charges
//			chequeReturnCharge = chequeReturnCharge
//					.subtract(BigDecimal.valueOf(paidCheckBounce).setScale(2, RoundingMode.HALF_UP));
//
//			if (chequeReturnCharge.compareTo(BigDecimal.ZERO) < 0) {
//				chequeReturnCharge = BigDecimal.ZERO;
//			}
//
//			// Late penalty only if no cheque return charges
//			if (!hasChequeReturnCharges && now.isAfter(dueDate) && totalLatePenalty < 500
//					&& dueAmount.compareTo(BigDecimal.ZERO) != 0) {
//				latePenalty = BigDecimal.valueOf(500 - totalLatePenalty);
//			}
//
//			if (now.isAfter(dueDate) && dueAmount.compareTo(BigDecimal.ZERO) != 0) {
//				numberOfEmisDue++;
//				LocalDate firstOfMonth = paymentStatementDTO.getLastPaidDate();
//				if (firstOfMonth == null) {
//					firstOfMonth = dueDate.withDayOfMonth(1);
//
//				}
//				long overdueDays = ChronoUnit.DAYS.between(firstOfMonth, now);
//				double dailyPenaltyRate = 0.03 / 30;
//
//				BigDecimal currentEmiPenalty = dueAmount.multiply(BigDecimal.valueOf(dailyPenaltyRate))
//						.multiply(BigDecimal.valueOf(overdueDays)).setScale(2, RoundingMode.HALF_UP);
//
//				emiPenalty = emiPenalty.add(currentEmiPenalty);
//				totalPenalty = totalPenalty.add(currentEmiPenalty).add(chequeReturnCharge).add(latePenalty).setScale(2,
//						RoundingMode.HALF_UP);
//
//				String emiMonth = dueDate.getMonth().toString() + " " + dueDate.getYear();
//				emiDueDetails.add(new EmiDueInfoDTO(emiCounter, dueDate, overdueDays, dueAmount.doubleValue(),
//						chequeReturnCharge.doubleValue(), currentEmiPenalty.doubleValue(), emiMonth,
//						latePenalty.doubleValue()));
//			} else if (dueDate.getYear() == now.getYear() && dueDate.getMonthValue() == now.getMonthValue()
//					&& dueAmount.compareTo(BigDecimal.ZERO) != 0) {
//				numberOfEmisDue++;
//				long overdueDays = 0;
//				String emiMonth = dueDate.getMonth().toString() + " " + dueDate.getYear();
//				emiDueDetails.add(new EmiDueInfoDTO(emiCounter, dueDate, overdueDays, dueAmount.doubleValue(),
//						chequeReturnCharge.doubleValue(), 0, emiMonth, latePenalty.doubleValue()));
//			}
//
//			totalLatePenalty = 0;
//		}
//
//		receipetResponse.setInterest(roundToNearestRupee(totalInterest));
//		receipetResponse.setPrincipal(roundToNearestRupee(principal));
//		receipetResponse.setRepaid(roundToNearestRupee(repaid));
//		receipetResponse.setOutstanding(roundToNearestRupee(outstanding));
//		receipetResponse.setTotalDue(roundToNearestRupee(totalDue));
//		receipetResponse.setNumberOfEmisDue(numberOfEmisDue);
//		receipetResponse.setLoanAmount(roundToNearestRupee(loanAmount));
//		receipetResponse.setEmiDueInfo(emiDueDetails);
//		receipetResponse.setTotalPenalty(roundToNearestRupee(totalPenalty));
//		receipetResponse.setTotalDue(roundToNearestRupee(totalDue.add(totalPenalty)));
//		receipetResponse.setEmiDueAmount(roundToNearestRupee(totalDue));
//		receipetResponse.setLastPaidDate(lastPaidDate);
//
//		logger.info("!!! inside class:ReceiptServiceImpl, method: getReceiptInfoByReceiptDate");
//		return receipetResponse;
//	}

	// Updated payLoan method with correct lump-sum payment allocation logic
//	@Override
//	public ReceiptResponse payLoan(PayRequest payRequest) {
//		logger.info("!!! inside class:ReceiptServiceImpl, method: payLoan");
//		ReceiptResponse receiptResponse = new ReceiptResponse();
//		List<PaymentStatementDTO> repaymentList = repaymentService.getRepaymentStatement(payRequest.getDid());
//
//		BigDecimal paidAmount = BigDecimal.valueOf(payRequest.getAmount());
//		LocalTime currentTime = LocalTime.now();
//		LocalDateTime paymentDate = LocalDateTime.of(payRequest.getPaymentDate(), currentTime);
//
//		repaymentList.sort(Comparator.comparing(PaymentStatementDTO::getDueDate));
//
//		for (PaymentStatementDTO payment : repaymentList) {
//			if (paidAmount.compareTo(BigDecimal.ZERO) <= 0)
//				break;
//
//			LocalDate dueDate = payment.getDueDate();
//			BigDecimal dueAmount = BigDecimal.valueOf(Math.max(0, payment.getDueAmount()));
//
//			if (dueAmount.compareTo(BigDecimal.ZERO) == 0)
//				continue;
//
//			List<RepaymentTransactions> checkLatePenalty = transactionsRepository
//					.findByRepaymentRepaymentId(payment.getRepaymentId());
//
//			double totalLatePenalty = checkLatePenalty.stream()
//					.filter(p -> p.getLatePenalty() != null && p.getLatePenalty() > 0)
//					.mapToDouble(RepaymentTransactions::getLatePenalty).sum();
//
//			double paidCheckBounce = checkLatePenalty.stream()
//					.filter(p -> p.getChequeReturnCharges() != null && p.getChequeReturnCharges() > 0)
//					.mapToDouble(RepaymentTransactions::getChequeReturnCharges).sum();
//
//			BigDecimal chequeReturnCharge = BigDecimal.ZERO;
//			boolean hasChequeReturnCharges = false;
//
//			List<CheckDeposit> cheques = checkDepositRepository.LmsRepaymentRepaymentId(payment.getRepaymentId());
//			for (CheckDeposit cheque : cheques) {
//				if (cheque != null && !"Cleared".equalsIgnoreCase(cheque.getStatus())
//						&& !"PAID".equalsIgnoreCase(payment.getAmountStatus())) {
//					double charges = cheque.getChequeReturnCharges() != null ? cheque.getChequeReturnCharges() : 0.0;
//					if (charges > 0) {
//						hasChequeReturnCharges = true;
//						chequeReturnCharge = chequeReturnCharge
//								.add(BigDecimal.valueOf(charges).setScale(2, RoundingMode.HALF_UP));
//					}
//				}
//			}
//
//			chequeReturnCharge = chequeReturnCharge.subtract(BigDecimal.valueOf(paidCheckBounce)).max(BigDecimal.ZERO);
//
//			BigDecimal latePenalty = (!hasChequeReturnCharges && paymentDate.toLocalDate().isAfter(dueDate)
//					&& totalLatePenalty < 500 && dueAmount.compareTo(BigDecimal.ZERO) != 0)
//							? BigDecimal.valueOf(500 - totalLatePenalty)
//							: BigDecimal.ZERO;
//
//			BigDecimal emiPenalty = BigDecimal.ZERO;
//			if (paymentDate.toLocalDate().isAfter(dueDate)) {
//				LocalDate firstOfMonth = dueDate.withDayOfMonth(1);
//				long overdueDays = ChronoUnit.DAYS.between(firstOfMonth, paymentDate);
//				double dailyPenaltyRate = 0.03 / 30;
//				emiPenalty = BigDecimal.valueOf(payment.getDueAmount()).multiply(BigDecimal.valueOf(dailyPenaltyRate))
//						.multiply(BigDecimal.valueOf(Math.max(overdueDays, 0))).setScale(0, RoundingMode.HALF_UP);
//			}
//
//			// Begin payment allocation
//			BigDecimal penaltyPaid = BigDecimal.ZERO;
//			BigDecimal chequeReturnChargesPaid = BigDecimal.ZERO;
//			BigDecimal latePenaltyPaid = BigDecimal.ZERO;
//			BigDecimal interestPaid = BigDecimal.ZERO;
//			BigDecimal principalPaid = BigDecimal.ZERO;
//
//			if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
//				penaltyPaid = paidAmount.min(emiPenalty);
//				paidAmount = paidAmount.subtract(penaltyPaid);
//			}
//
//			if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
//				chequeReturnChargesPaid = paidAmount.min(chequeReturnCharge);
//				paidAmount = paidAmount.subtract(chequeReturnChargesPaid);
//			}
//
//			if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
//				latePenaltyPaid = paidAmount.min(latePenalty);
//				paidAmount = paidAmount.subtract(latePenaltyPaid);
//			}
//
//			BigDecimal interest = BigDecimal.valueOf(payment.getInterest());
//			if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
//				interestPaid = paidAmount.min(interest);
//				paidAmount = paidAmount.subtract(interestPaid);
//			}
//
//			BigDecimal principal = BigDecimal.valueOf(payment.getPrincipalAmount());
//			if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
//				principalPaid = paidAmount.min(principal);
//				paidAmount = paidAmount.subtract(principalPaid);
//			}
//
//			BigDecimal remainingInterest = interest.subtract(interestPaid).max(BigDecimal.ZERO);
//			BigDecimal remainingPrincipal = principal.subtract(principalPaid).max(BigDecimal.ZERO);
//			BigDecimal emiPaid = principalPaid.add(interestPaid);
//			BigDecimal remainingDue = dueAmount.subtract(emiPaid).max(BigDecimal.ZERO);
//
//			String emiMonth = dueDate.getMonth().toString() + " " + dueDate.getYear();
//			LmsRepayment lmsRepayment = repaymentService.getRepaymentById(payment.getRepaymentId());
//
//			RepaymentTransactions transaction = new RepaymentTransactions();
//			transaction.setAmountPaid(roundToNearestRupee(emiPaid));
//			transaction.setOtherCharges(payRequest.getOtherCharges());
//			transaction.setPaymentMode(payRequest.getPaymentMode());
//			transaction.setInterestComponent(roundToNearestRupee(interestPaid));
//			transaction.setPenaltyComponent(roundToNearestRupee(penaltyPaid));
//			transaction.setPrincipalComponent(roundToNearestRupee(principalPaid));
//			transaction.setLatePenalty(roundToNearestRupee(latePenaltyPaid));
//			transaction.setChequeReturnCharges(roundToNearestRupee(chequeReturnChargesPaid));
//			transaction.setPaymentDate(paymentDate);
//			transaction.setDescription("EMI Payment - " + emiMonth);
//			transaction.setRepayment(lmsRepayment);
//
//			if (roundToNearestRupee(remainingPrincipal) == 1.0) {
//				remainingPrincipal = BigDecimal.ZERO;
//			}
//
//			lmsRepayment.setInterest(roundToNearestRupee(remainingInterest));
//			lmsRepayment.setLastPaidDate(payRequest.getPaymentDate());
//			lmsRepayment.setPrincipalAmount(roundToNearestRupee(remainingPrincipal));
//			lmsRepayment.setDueAmount(roundToNearestRupee(remainingDue));
//			lmsRepayment.setAmountPaid(roundToNearestRupee(BigDecimal.valueOf(payment.getAmountPaid()).add(emiPaid)));
//			lmsRepayment.setOtherCharges(payRequest.getOtherCharges() != 0 ? payRequest.getOtherCharges() : 0d);
//
//			if (remainingDue.compareTo(BigDecimal.valueOf(1.00)) <= 0) {
//				lmsRepayment.setAmountStatus("Paid");
//				lmsRepayment.setDueAmount(0.0);
//			}
//
//			transactionsRepository.save(transaction);
//			repaymentRepository.save(lmsRepayment);
//
//			if (paidAmount.compareTo(BigDecimal.ONE) < 0)
//				break;
//		}
//
//		return receiptResponse;
//	}
	public void penaltyAdjuster(List<PaymentStatementDTO> repaymentSchedule, PayRequest payRequest) {
		logger.info("!!! inside class: ReceiptServiceImpl, method: penaltyAdjuster, Processing");

		LocalTime currentTime = LocalTime.now();
		LocalDateTime paymentDate = LocalDateTime.of(payRequest.getPaymentDate(), currentTime);
		LocalDate now = payRequest.getPaymentDate();

		BigDecimal amountPaid = BigDecimal.valueOf(payRequest.getAmount());
		List<RepaymentTransactions> repaymentTransactions = new ArrayList<>();

		// Step 1: Handle Interest Penalty
		for (PaymentStatementDTO repayment : repaymentSchedule) {
			if (amountPaid.compareTo(BigDecimal.ZERO) == 0)
				break;

			RepaymentTransactions lastTrans = transactionsRepository
					.findByRepaymentIdAndMaxPaymentDate(repayment.getRepaymentId());
			LocalDate dueDate = repayment.getDueDate();
			BigDecimal dueAmount = BigDecimal.valueOf(repayment.getDueAmount());

			LocalDate lastPaid;
			if (lastTrans == null) {
				lastPaid = dueDate.withDayOfMonth(1);

			} else {
				if (lastTrans.getPaymentDate().toLocalDate().isBefore(dueDate)) {
					lastPaid = dueDate.withDayOfMonth(1);
				} else {
					lastPaid = lastTrans.getPaymentDate().toLocalDate();
				}
			}

			if (now.isAfter(dueDate) && dueAmount.compareTo(BigDecimal.ZERO) != 0) {
				PenaltyResponse penaltyResponse = PenaltyUtil.calculatePenaltyCoverage(lastPaid,
						repayment.getDueAmount(), amountPaid.doubleValue(), lastTrans, now);

				BigDecimal penaltyPaid = BigDecimal.valueOf(penaltyResponse.getPenaltyPaid());
				amountPaid = BigDecimal.valueOf(penaltyResponse.getExcessAmount());

				if (penaltyPaid.compareTo(BigDecimal.ZERO) > 0) {
					RepaymentTransactions txn = new RepaymentTransactions();
					txn.setAmountPaid(roundToNearestRupee(penaltyPaid));
					txn.setPenaltyComponent(penaltyPaid.doubleValue());
					txn.setPaymentMode(payRequest.getPaymentMode());
					txn.setOtherCharges(payRequest.getOtherCharges());
					txn.setOtherChargesType(payRequest.getOtherChargesType());
					txn.setPaymentDate(paymentDate);
					txn.setTxnDescription(payRequest.getDescription());
					txn.setTransactionId(payRequest.getTransactionId());
					txn.setDueDate(dueDate);
					txn.setDueAmount(repayment.getDueAmount());
					txn.setPenaltyUncoveredDays(penaltyResponse.getUncoveredDays());

					String dueMonthYear = dueDate.format(DateTimeFormatter.ofPattern("MMM-yyyy"));
					String formattedDate = paymentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
					txn.setDescription("Penalty of ₹" + penaltyPaid + " for due of " + dueMonthYear + " received on "
							+ formattedDate);

					LmsRepayment repay = new LmsRepayment();
					repay.setRepaymentId(repayment.getRepaymentId());
					Disbursment disbursment = new Disbursment();
					disbursment.setDisbursmentId(payRequest.getDid());
					txn.setDisbursment(disbursment);
					txn.setRepayment(repay);

					repaymentTransactions.add(txn);
				}
			}
		}

		// Step 2: Cheque Bounce & Late Penalty Adjustment
		for (RepaymentTransactions trans : repaymentTransactions) {
			if (amountPaid.compareTo(BigDecimal.ZERO) == 0)
				break;

			BigDecimal latePenalty = BigDecimal.ZERO;
			BigDecimal chequeReturnCharge = BigDecimal.ZERO;
			boolean hasChequeReturnCharges = false;

			List<CheckDeposit> cheques = checkDepositRepository
					.LmsRepaymentRepaymentId(trans.getRepayment().getRepaymentId());
			List<RepaymentTransactions> checkLatePenalty = transactionsRepository
					.findByRepaymentRepaymentId(trans.getRepayment().getRepaymentId());

			double totalLatePenalty = checkLatePenalty.stream()
					.filter(p -> p.getLatePenalty() != null && p.getLatePenalty() > 0)
					.mapToDouble(RepaymentTransactions::getLatePenalty).sum();

			double paidCheckBounce = checkLatePenalty.stream()
					.filter(p -> p.getChequeReturnCharges() != null && p.getChequeReturnCharges() > 0)
					.mapToDouble(RepaymentTransactions::getChequeReturnCharges).sum();

			for (CheckDeposit cheque : cheques) {
				if ("rejected".equalsIgnoreCase(cheque.getStatus()) && cheque.getChequeReturnCharges() != null) {
					hasChequeReturnCharges = true;
					chequeReturnCharge = chequeReturnCharge.add(BigDecimal.valueOf(cheque.getChequeReturnCharges()));
				}
			}
			chequeReturnCharge = chequeReturnCharge.subtract(BigDecimal.valueOf(paidCheckBounce)).max(BigDecimal.ZERO);

			if (!hasChequeReturnCharges && now.isAfter(trans.getDueDate()) && trans.getDueAmount() != 0
					&& totalLatePenalty < 500) {
				latePenalty = BigDecimal.valueOf(500 - totalLatePenalty);
			}

			// Deduct Cheque Bounce
			BigDecimal chequeReturnChargesPaid = amountPaid.min(chequeReturnCharge);
			amountPaid = amountPaid.subtract(chequeReturnChargesPaid);

			// Deduct Late Penalty
			BigDecimal latePenaltyPaid = amountPaid.min(latePenalty);
			amountPaid = amountPaid.subtract(latePenaltyPaid);

			double totalPenaltyPaid = trans.getAmountPaid() + latePenaltyPaid.doubleValue()
					+ chequeReturnChargesPaid.doubleValue();

			String formattedDate = paymentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			String dueMonthYear = trans.getDueDate().format(DateTimeFormatter.ofPattern("MMM-yyyy"));

			trans.setDescription("Penalty of ₹" + totalPenaltyPaid + " for due of " + dueMonthYear + " received on "
					+ formattedDate);

			trans.setAmountPaid(totalPenaltyPaid);
			trans.setLatePenalty(latePenaltyPaid.doubleValue());
			trans.setChequeReturnCharges(chequeReturnChargesPaid.doubleValue());
		}

		// Step 3: EMI Adjustment (for existing transactions)
		for (RepaymentTransactions trans : repaymentTransactions) {
			if (amountPaid.compareTo(BigDecimal.ZERO) == 0)
				break;

			LmsRepayment repayment = repaymentRepository.findById(trans.getRepayment().getRepaymentId()).orElse(null);
//			if (repayment == null)
//				continue;

//			if ("Paid".equalsIgnoreCase(repayment.getAmountStatus()))
//				continue;

			BigDecimal interest = BigDecimal.valueOf(repayment.getInterest());
			BigDecimal principal = BigDecimal.valueOf(repayment.getPrincipalAmount());

			BigDecimal interestPaid = amountPaid.min(interest);
			amountPaid = amountPaid.subtract(interestPaid);

			BigDecimal principalPaid = amountPaid.min(principal);
			amountPaid = amountPaid.subtract(principalPaid);

			BigDecimal totalEMI = interestPaid.add(principalPaid);

			BigDecimal totalPaid = totalEMI.add(BigDecimal.valueOf(trans.getAmountPaid()));

			repayment.setInterest(interest.subtract(interestPaid).doubleValue());
			repayment.setPrincipalAmount(principal.subtract(principalPaid).doubleValue());

			BigDecimal dueAmount = BigDecimal.valueOf(repayment.getDueAmount());
			BigDecimal calculatedDue = totalEMI.subtract(dueAmount).abs();

			if (roundToNearestRupee(calculatedDue) == 1) {
				calculatedDue = BigDecimal.ZERO;
			}

			repayment.setDueAmount(roundToNearestRupee(calculatedDue));
			repayment.setLastPaidDate(payRequest.getPaymentDate());
			repayment.setAmountPaid(roundToNearestRupee(totalEMI));

			if (BigDecimal.valueOf(repayment.getDueAmount()).compareTo(BigDecimal.ZERO) == 0) {
				repayment.setAmountStatus("Paid");
			}
			trans.setInterestComponent(interestPaid.doubleValue());
			trans.setPrincipalComponent(principalPaid.doubleValue());
			trans.setAmountPaid(roundToNearestRupee(totalPaid));
			trans.setOtherCharges(payRequest.getOtherCharges() != 0 ? payRequest.getOtherCharges() : 0d);

			String formattedDate = paymentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			String dueMonthYear = trans.getDueDate().format(DateTimeFormatter.ofPattern("MMM-yyyy"));

			if (totalEMI.compareTo(BigDecimal.ZERO) != 0) {
				trans.setDescription((Optional.ofNullable(trans.getDescription()).orElse("")) + "\nEMI Amount of ₹"
						+ roundToNearestRupee(totalEMI) + " for due of " + dueMonthYear + " received on "
						+ formattedDate);
			}

			repaymentRepository.save(repayment);
		}

		// Step 4: Prepayment EMI Adjustment
		if (amountPaid.compareTo(BigDecimal.ZERO) > 0) {
			List<PaymentStatementDTO> remainingSchedule = repaymentService.getRepaymentStatement(payRequest.getDid());
			for (PaymentStatementDTO dto : remainingSchedule) {
				if (amountPaid.compareTo(BigDecimal.ZERO) == 0)
					break;

				LmsRepayment repayment = repaymentRepository.findById(dto.getRepaymentId()).orElse(null);
//				if (repayment == null || "Paid".equalsIgnoreCase(repayment.getAmountStatus()))
//					continue;

				RepaymentTransactions txn = new RepaymentTransactions();

				BigDecimal interest = BigDecimal.valueOf(dto.getInterest());
				BigDecimal principal = BigDecimal.valueOf(dto.getPrincipalAmount());

				BigDecimal interestPaid = amountPaid.min(interest);
				amountPaid = amountPaid.subtract(interestPaid);

				BigDecimal principalPaid = amountPaid.min(principal);
				amountPaid = amountPaid.subtract(principalPaid);

				BigDecimal totalEMI = interestPaid.add(principalPaid);

				repayment.setInterest(roundToNearestRupee(interest.subtract(interestPaid)));
				repayment.setPrincipalAmount(roundToNearestRupee(principal.subtract(principalPaid)));
				BigDecimal dueAmount = BigDecimal.valueOf(repayment.getDueAmount());
				BigDecimal calculatedDue = totalEMI.subtract(dueAmount).abs();

				if (roundToNearestRupee(calculatedDue) == 1) {
					calculatedDue = BigDecimal.ZERO;
				}
				repayment.setDueAmount(roundToNearestRupee(calculatedDue));
				repayment.setLastPaidDate(payRequest.getPaymentDate());
				repayment.setAmountPaid(roundToNearestRupee(totalEMI));

				if (BigDecimal.valueOf(repayment.getDueAmount()).compareTo(BigDecimal.ZERO) == 0) {
					repayment.setAmountStatus("Paid");
				}

				txn.setRepayment(repayment);
				txn.setPaymentDate(paymentDate);
				txn.setAmountPaid(roundToNearestRupee(totalEMI));
				txn.setInterestComponent(interestPaid.doubleValue());
				txn.setPrincipalComponent(principalPaid.doubleValue());
				txn.setOtherCharges(payRequest.getOtherCharges() != 0 ? payRequest.getOtherCharges() : 0d);
				txn.setPaymentMode(payRequest.getPaymentMode());
				txn.setOtherChargesType(payRequest.getOtherChargesType());
				txn.setPaymentDate(paymentDate);
				txn.setTxnDescription(payRequest.getDescription());
				txn.setTransactionId(payRequest.getTransactionId());

				String formattedDate = paymentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
				String dueMonthYear = dto.getDueDate().format(DateTimeFormatter.ofPattern("MMM-yyyy"));

				if (totalEMI.compareTo(BigDecimal.ZERO) != 0) {
					txn.setDescription("EMI Amount of ₹" + roundToNearestRupee(totalEMI) + " for due of " + dueMonthYear
							+ " received on " + formattedDate);
				}

				repaymentTransactions.add(txn);
				repaymentRepository.save(repayment);
			}
		}

		// Remove zero-paid transactions
		repaymentTransactions.removeIf(txn -> txn.getAmountPaid() == 0);

		transactionsRepository.saveAll(repaymentTransactions);

		logger.info("!!! inside class: ReceiptServiceImpl, method: penaltyAdjuster , Completed");
	}

	@Override
	public ReceiptResponse payLoan(PayRequest payRequest) {
		logger.info("!!! inside class:ReceiptServiceImpl, method: payLoan");

		LocalTime currentTime = LocalTime.now();
		LocalDateTime paymentDate = LocalDateTime.of(payRequest.getPaymentDate(), currentTime);
		List<PaymentStatementDTO> repaymentSchedule = repaymentService.getRepaymentStatement(payRequest.getDid());
		RepaymentTransactions repaymentTransactions = new RepaymentTransactions();
		repaymentTransactions.setAmountPaid(payRequest.getAmount());
		repaymentTransactions.setOtherCharges(payRequest.getOtherCharges());
		repaymentTransactions.setOtherChargesType(payRequest.getOtherChargesType());
		repaymentTransactions.setPaymentDate(paymentDate);
		repaymentTransactions.setTxnDescription(payRequest.getDescription());
		repaymentTransactions.setTransactionId(payRequest.getTransactionId());
		repaymentTransactions.setPaymentDate(paymentDate);
		repaymentTransactions.setPaymentMode(payRequest.getPaymentMode());
		String formattedDate = paymentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		String description = "Payment of ₹" + payRequest.getAmount() + " received on " + formattedDate;
		repaymentTransactions.setDescription(description);
		Disbursment disbursment = new Disbursment();
		disbursment.setDisbursmentId(payRequest.getDid());
		repaymentTransactions.setDisbursment(disbursment);
		transactionsRepository.save(repaymentTransactions);

		penaltyAdjuster(repaymentSchedule, payRequest);

		logger.info("!!! inside class:ReceiptServiceImpl, method: payLoan");
		return null;

	}
	// ---------------------------------------------------------------------------------

	@Override
	public ReceiptResponse getReceiptInfo(Long dId) {
		logger.info("!!! inside class:ReceiptServiceImpl, method: getReceiptInfo()");
		ReceiptResponse receipetResponse = new ReceiptResponse();
//		System.err.println("***DID***" + dId);
		BigDecimal totalInterest = BigDecimal.ZERO;
		BigDecimal totalPenalty = BigDecimal.ZERO;
		BigDecimal totalDue = BigDecimal.ZERO;
		BigDecimal principal = BigDecimal.ZERO;
		BigDecimal loanAmount = BigDecimal.ZERO;
		BigDecimal repaid = BigDecimal.ZERO;
		BigDecimal outstanding = BigDecimal.ZERO;
		BigDecimal emiPenalty = BigDecimal.ZERO;
		BigDecimal chequeReturnCharge = BigDecimal.ZERO;
		BigDecimal latePenalty = BigDecimal.ZERO;
		BigDecimal totalEmiAmount = BigDecimal.ZERO;

		List<PaymentStatementDTO> repaymentSchedule = repaymentService.getRepaymentStatement(dId);
		Date lastPaidDate = transactionsRepository.findLastPaidDateByDisbursmentId(dId);
		LocalDate now = LocalDate.now();
		List<EmiDueInfoDTO> emiDueDetails = new ArrayList<>();
		int emiCounter = 0;
		int numberOfEmisDue = 0;
		for (PaymentStatementDTO repayment : repaymentSchedule) {
			emiCounter++;

			LocalDate dueDate = repayment.getDueDate();
			RepaymentTransactions lastTrans = transactionsRepository
					.findByRepaymentIdAndMaxPaymentDate(repayment.getRepaymentId());
			List<RepaymentTransactions> checkLatePenalty = transactionsRepository
					.findByRepaymentRepaymentId(repayment.getRepaymentId());
			double totalLatePenalty = checkLatePenalty.stream()
					.filter(p -> p.getLatePenalty() != null && p.getLatePenalty() > 0)
					.mapToDouble(RepaymentTransactions::getLatePenalty).sum();

			double paidCheckBounce = checkLatePenalty.stream()
					.filter(p -> p.getChequeReturnCharges() != null && p.getChequeReturnCharges() > 0)
					.mapToDouble(RepaymentTransactions::getChequeReturnCharges).sum();

			BigDecimal interest = BigDecimal.valueOf(repayment.getInterest()).setScale(2, RoundingMode.HALF_UP);
			BigDecimal principalAmt = BigDecimal.valueOf(repayment.getPrincipalAmount()).setScale(2,
					RoundingMode.HALF_UP);
			BigDecimal amountPaid = BigDecimal.valueOf(repayment.getAmountPaid()).setScale(2, RoundingMode.HALF_UP);
			BigDecimal dueAmount = BigDecimal.valueOf(repayment.getDueAmount()).setScale(2, RoundingMode.HALF_UP);

			totalInterest = totalInterest.add(interest);
			repaid = repaid.add(amountPaid);
			outstanding = outstanding.add(principalAmt);

			loanAmount = BigDecimal.valueOf(repayment.getAmountSanctioned()).setScale(2, RoundingMode.HALF_UP);

			if ((dueDate.getYear() < now.getYear()
					|| (dueDate.getYear() == now.getYear() && dueDate.getMonthValue() <= now.getMonthValue()))
					&& dueAmount.compareTo(BigDecimal.ZERO) != 0) {
				receipetResponse.setDueDate(dueDate);
				totalDue = totalDue.add(dueAmount);
			}

			// Reset for each EMI
			latePenalty = BigDecimal.ZERO;
			chequeReturnCharge = BigDecimal.ZERO;
			boolean hasChequeReturnCharges = false;

			List<CheckDeposit> cheques = checkDepositRepository.LmsRepaymentRepaymentId(repayment.getRepaymentId());

			for (CheckDeposit cheque : cheques) {
				if (cheque != null && cheque.getStatus().equalsIgnoreCase("rejected")
						&& !repayment.getAmountStatus().equalsIgnoreCase("PAID")) {

					double charges = cheque.getChequeReturnCharges() != null ? cheque.getChequeReturnCharges() : 0.0;

					if (charges > 0) {
						hasChequeReturnCharges = true;
						chequeReturnCharge = chequeReturnCharge
								.add(BigDecimal.valueOf(charges).setScale(2, RoundingMode.HALF_UP));
					}
				}
			}

			// Subtract already paid cheque bounce charges
			chequeReturnCharge = chequeReturnCharge
					.subtract(BigDecimal.valueOf(paidCheckBounce).setScale(2, RoundingMode.HALF_UP));

			if (chequeReturnCharge.compareTo(BigDecimal.ZERO) < 0) {
				chequeReturnCharge = BigDecimal.ZERO;
			}

			// Late penalty only if no cheque return charges
			if (!hasChequeReturnCharges && now.isAfter(dueDate) && totalLatePenalty < 500
					&& dueAmount.compareTo(BigDecimal.ZERO) != 0) {
				latePenalty = BigDecimal.valueOf(500 - totalLatePenalty);
			}

			LocalDate lastPaid;
			if (lastTrans == null) {
				lastPaid = dueDate.withDayOfMonth(1);

			} else {
				if (lastTrans.getPaymentDate().toLocalDate().isBefore(dueDate)) {
					lastPaid = dueDate.withDayOfMonth(1);
				} else {
					lastPaid = lastTrans.getPaymentDate().toLocalDate();
				}
			}

			if (now.isAfter(dueDate) && dueAmount.compareTo(BigDecimal.ZERO) != 0) {
				String emiMonth = dueDate.getMonth().toString() + " " + dueDate.getYear();
				numberOfEmisDue++;

				long overdueDays = ChronoUnit.DAYS.between(lastPaid, now);
				if (lastTrans != null) {
					overdueDays = overdueDays + lastTrans.getPenaltyUncoveredDays();
				}

				double dailyPenaltyRate = 0.03 / 30;
				BigDecimal currentEmiPenalty = dueAmount.multiply(BigDecimal.valueOf(dailyPenaltyRate))
						.multiply(BigDecimal.valueOf(overdueDays)).setScale(2, RoundingMode.HALF_UP);
				emiPenalty = emiPenalty.add(currentEmiPenalty);
				totalPenalty = totalPenalty.add(currentEmiPenalty).add(chequeReturnCharge).add(latePenalty).setScale(2,
						RoundingMode.HALF_UP);

				totalEmiAmount = BigDecimal
						.valueOf(dueAmount.doubleValue() + chequeReturnCharge.doubleValue()
								+ currentEmiPenalty.doubleValue() + latePenalty.doubleValue())
						.setScale(2, RoundingMode.HALF_UP);

				emiDueDetails.add(new EmiDueInfoDTO(emiCounter, dueDate, overdueDays, dueAmount.doubleValue(),
						chequeReturnCharge.doubleValue(), currentEmiPenalty.doubleValue(), emiMonth,
						latePenalty.doubleValue(), totalEmiAmount.doubleValue()));

			} else if (dueDate.getYear() == now.getYear() && dueDate.getMonthValue() == now.getMonthValue()
					&& dueAmount.compareTo(BigDecimal.ZERO) != 0) {
				numberOfEmisDue++;
				long overdueDays = 0;
				String emiMonth = dueDate.getMonth().toString() + " " + dueDate.getYear();

				totalEmiAmount = BigDecimal
						.valueOf(dueAmount.doubleValue() + chequeReturnCharge.doubleValue() + latePenalty.doubleValue())
						.setScale(2, RoundingMode.HALF_UP);

				emiDueDetails.add(new EmiDueInfoDTO(emiCounter, dueDate, overdueDays, dueAmount.doubleValue(),
						chequeReturnCharge.doubleValue(), 0, emiMonth, latePenalty.doubleValue(),
						totalEmiAmount.doubleValue()));
			}

		}

		receipetResponse.setInterest(roundToNearestRupee(totalInterest));
		receipetResponse.setPrincipal(roundToNearestRupee(principal));
		receipetResponse.setRepaid(roundToNearestRupee(repaid));
		receipetResponse.setOutstanding(roundToNearestRupee(outstanding));
		receipetResponse.setTotalDue(roundToNearestRupee(totalDue));
		receipetResponse.setNumberOfEmisDue(numberOfEmisDue);
		receipetResponse.setLoanAmount(roundToNearestRupee(loanAmount));
		receipetResponse.setEmiDueInfo(emiDueDetails);
		receipetResponse.setTotalPenalty(roundToNearestRupee(totalPenalty));
		receipetResponse.setTotalDue(roundToNearestRupee(totalDue.add(totalPenalty)));
		receipetResponse.setEmiDueAmount(roundToNearestRupee(totalDue));
		receipetResponse.setLastPaidDate(lastPaidDate);

		logger.info("!!! inside class:ReceiptServiceImpl, method: getReceiptInfo() END");
		return receipetResponse;
	}

//-------------------------------------------------------------------------------
	/// With date
	@Override
	public ReceiptResponse getReceiptInfoByReceiptDate(Long dId, LocalDate date) {
		logger.info("!!! inside class:ReceiptServiceImpl, method: getReceiptInfo() ");
		ReceiptResponse receipetResponse = new ReceiptResponse();
//		System.err.println("***DID***" + dId);
		BigDecimal totalInterest = BigDecimal.ZERO;
		BigDecimal totalPenalty = BigDecimal.ZERO;
		BigDecimal totalDue = BigDecimal.ZERO;
		BigDecimal principal = BigDecimal.ZERO;
		BigDecimal loanAmount = BigDecimal.ZERO;
		BigDecimal repaid = BigDecimal.ZERO;
		BigDecimal outstanding = BigDecimal.ZERO;
		BigDecimal emiPenalty = BigDecimal.ZERO;
		BigDecimal chequeReturnCharge = BigDecimal.ZERO;
		BigDecimal latePenalty = BigDecimal.ZERO;
		BigDecimal totalEmiAmount = BigDecimal.ZERO;

		List<PaymentStatementDTO> repaymentSchedule = repaymentService.getRepaymentStatement(dId);
		Date lastPaidDate = transactionsRepository.findLastPaidDateByDisbursmentId(dId);
		LocalDate now = date;
		List<EmiDueInfoDTO> emiDueDetails = new ArrayList<>();
		int emiCounter = 0;
		int numberOfEmisDue = 0;
		for (PaymentStatementDTO repayment : repaymentSchedule) {
			emiCounter++;

			LocalDate dueDate = repayment.getDueDate();
			RepaymentTransactions lastTrans = transactionsRepository
					.findByRepaymentIdAndMaxPaymentDate(repayment.getRepaymentId());
			List<RepaymentTransactions> checkLatePenalty = transactionsRepository
					.findByRepaymentRepaymentId(repayment.getRepaymentId());
			double totalLatePenalty = checkLatePenalty.stream()
					.filter(p -> p.getLatePenalty() != null && p.getLatePenalty() > 0)
					.mapToDouble(RepaymentTransactions::getLatePenalty).sum();

			double paidCheckBounce = checkLatePenalty.stream()
					.filter(p -> p.getChequeReturnCharges() != null && p.getChequeReturnCharges() > 0)
					.mapToDouble(RepaymentTransactions::getChequeReturnCharges).sum();

			BigDecimal interest = BigDecimal.valueOf(repayment.getInterest()).setScale(2, RoundingMode.HALF_UP);
			BigDecimal principalAmt = BigDecimal.valueOf(repayment.getPrincipalAmount()).setScale(2,
					RoundingMode.HALF_UP);
			BigDecimal amountPaid = BigDecimal.valueOf(repayment.getAmountPaid()).setScale(2, RoundingMode.HALF_UP);
			BigDecimal dueAmount = BigDecimal.valueOf(repayment.getDueAmount()).setScale(2, RoundingMode.HALF_UP);

			totalInterest = totalInterest.add(interest);
			repaid = repaid.add(amountPaid);
			outstanding = outstanding.add(principalAmt);

			loanAmount = BigDecimal.valueOf(repayment.getAmountSanctioned()).setScale(2, RoundingMode.HALF_UP);

			if ((dueDate.getYear() < now.getYear()
					|| (dueDate.getYear() == now.getYear() && dueDate.getMonthValue() <= now.getMonthValue()))
					&& dueAmount.compareTo(BigDecimal.ZERO) != 0) {
				receipetResponse.setDueDate(dueDate);
				totalDue = totalDue.add(dueAmount);
			}

			// Reset for each EMI
			latePenalty = BigDecimal.ZERO;
			chequeReturnCharge = BigDecimal.ZERO;
			boolean hasChequeReturnCharges = false;

			List<CheckDeposit> cheques = checkDepositRepository.LmsRepaymentRepaymentId(repayment.getRepaymentId());

			for (CheckDeposit cheque : cheques) {
				if (cheque != null && cheque.getStatus().equalsIgnoreCase("rejected")
						&& !repayment.getAmountStatus().equalsIgnoreCase("PAID")) {

					double charges = cheque.getChequeReturnCharges() != null ? cheque.getChequeReturnCharges() : 0.0;

					if (charges > 0) {
						hasChequeReturnCharges = true;
						chequeReturnCharge = chequeReturnCharge
								.add(BigDecimal.valueOf(charges).setScale(2, RoundingMode.HALF_UP));
					}
				}
			}

			// Subtract already paid cheque bounce charges
			chequeReturnCharge = chequeReturnCharge
					.subtract(BigDecimal.valueOf(paidCheckBounce).setScale(2, RoundingMode.HALF_UP));

			if (chequeReturnCharge.compareTo(BigDecimal.ZERO) < 0) {
				chequeReturnCharge = BigDecimal.ZERO;
			}

			// Late penalty only if no cheque return charges
			if (!hasChequeReturnCharges && now.isAfter(dueDate) && totalLatePenalty < 500
					&& dueAmount.compareTo(BigDecimal.ZERO) != 0) {
				latePenalty = BigDecimal.valueOf(500 - totalLatePenalty);
			}

			LocalDate lastPaid = null;
			if (lastTrans == null) {
				lastPaid = dueDate.withDayOfMonth(1);

			} else {
				if (lastTrans.getPaymentDate().toLocalDate().isBefore(dueDate)) {
					lastPaid = dueDate.withDayOfMonth(1);
				} else {
					lastPaid = lastTrans.getPaymentDate().toLocalDate();
				}

			}

			if (now.isAfter(dueDate) && dueAmount.compareTo(BigDecimal.ZERO) != 0) {
				String emiMonth = dueDate.getMonth().toString() + " " + dueDate.getYear();
				numberOfEmisDue++;

				long overdueDays = ChronoUnit.DAYS.between(lastPaid, now);
				if (lastTrans != null) {
					overdueDays = overdueDays + lastTrans.getPenaltyUncoveredDays();
				}

				double dailyPenaltyRate = 0.03 / 30;
				BigDecimal currentEmiPenalty = dueAmount.multiply(BigDecimal.valueOf(dailyPenaltyRate))
						.multiply(BigDecimal.valueOf(overdueDays)).setScale(2, RoundingMode.HALF_UP);
				emiPenalty = emiPenalty.add(currentEmiPenalty);
				totalPenalty = totalPenalty.add(currentEmiPenalty).add(chequeReturnCharge).add(latePenalty).setScale(2,
						RoundingMode.HALF_UP);

				totalEmiAmount = BigDecimal
						.valueOf(dueAmount.doubleValue() + chequeReturnCharge.doubleValue()
								+ currentEmiPenalty.doubleValue() + latePenalty.doubleValue())
						.setScale(2, RoundingMode.HALF_UP);

				emiDueDetails.add(new EmiDueInfoDTO(emiCounter, dueDate, overdueDays, dueAmount.doubleValue(),
						chequeReturnCharge.doubleValue(), currentEmiPenalty.doubleValue(), emiMonth,
						latePenalty.doubleValue(), totalEmiAmount.doubleValue()));

			} else if (dueDate.getYear() == now.getYear() && dueDate.getMonthValue() == now.getMonthValue()
					&& dueAmount.compareTo(BigDecimal.ZERO) != 0) {

				numberOfEmisDue++;
				long overdueDays = 0;
				String emiMonth = dueDate.getMonth().toString() + " " + dueDate.getYear();

				totalEmiAmount = BigDecimal
						.valueOf(dueAmount.doubleValue() + chequeReturnCharge.doubleValue() + latePenalty.doubleValue())
						.setScale(2, RoundingMode.HALF_UP);

				emiDueDetails.add(new EmiDueInfoDTO(emiCounter, dueDate, overdueDays, dueAmount.doubleValue(),
						chequeReturnCharge.doubleValue(), 0, emiMonth, latePenalty.doubleValue(),
						totalEmiAmount.doubleValue()));
			}

		}

		receipetResponse.setInterest(roundToNearestRupee(totalInterest));
		receipetResponse.setPrincipal(roundToNearestRupee(principal));
		receipetResponse.setRepaid(roundToNearestRupee(repaid));
		receipetResponse.setOutstanding(roundToNearestRupee(outstanding));
		receipetResponse.setTotalDue(roundToNearestRupee(totalDue));
		receipetResponse.setNumberOfEmisDue(numberOfEmisDue);
		receipetResponse.setLoanAmount(roundToNearestRupee(loanAmount));
		receipetResponse.setEmiDueInfo(emiDueDetails);
		receipetResponse.setTotalPenalty(roundToNearestRupee(totalPenalty));
		receipetResponse.setTotalDue(roundToNearestRupee(totalDue.add(totalPenalty)));
		receipetResponse.setEmiDueAmount(roundToNearestRupee(totalDue));
		receipetResponse.setLastPaidDate(lastPaidDate);
		logger.info("!!! inside class:ReceiptServiceImpl, method: getReceiptInfo() END");
		return receipetResponse;
	}

	private double roundToNearestRupee(BigDecimal amount) {
		return amount.setScale(0, RoundingMode.HALF_UP).doubleValue();
	}

	private double roundToNearestRupee(double amount) {
		return new BigDecimal(amount).setScale(0, RoundingMode.HALF_UP).doubleValue();
	}

}
