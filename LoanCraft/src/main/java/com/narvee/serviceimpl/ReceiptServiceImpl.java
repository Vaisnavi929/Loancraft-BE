package com.narvee.serviceimpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.Service.ReceiptService;
import com.narvee.Service.RepaymentService;
import com.narvee.controller.PayRequest;
import com.narvee.entity.CheckDeposit;
import com.narvee.entity.LmsRepayment;
import com.narvee.entity.RepaymentTransactions;
import com.narvee.helpers.EmiDueInfoDTO;
import com.narvee.repository.CheckDepositRepository;
import com.narvee.repository.RepaymentRepository;
import com.narvee.repository.RepaymentTransactionsRepository;
import com.narvee.request.dto.PaymentStatementDTO;
import com.narvee.response.ReceiptResponse;

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

	@Override
	public ReceiptResponse getReceiptInfo(Long dId) {
		logger.info("!!! inside class:ReceiptServiceImpl, method: getReceiptInfo() ");

		ReceiptResponse receipetResponse = new ReceiptResponse();
		List<PaymentStatementDTO> re = repaymentService.getRepaymentStatement(dId);

		BigDecimal totalInterest = BigDecimal.ZERO;
		BigDecimal principal = BigDecimal.ZERO;
		BigDecimal repaid = BigDecimal.ZERO;
		BigDecimal outstanding = BigDecimal.ZERO;
		BigDecimal totalDue = BigDecimal.ZERO;
		BigDecimal loanAmount = BigDecimal.ZERO;
		BigDecimal emiPenalty = BigDecimal.ZERO;
		BigDecimal totalPenalty = BigDecimal.ZERO;
		BigDecimal chequeReturnCharge = BigDecimal.ZERO;
		BigDecimal latePenalty = BigDecimal.ZERO;

		int emiCounter = 0;
		int numberOfEmisDue = 0;

		LocalDate now = LocalDate.now();
		List<EmiDueInfoDTO> emiDueDetails = new ArrayList<>();

		for (PaymentStatementDTO paymentStatementDTO : re) {
			emiCounter++;
			LocalDate dueDate = paymentStatementDTO.getDueDate();

			List<RepaymentTransactions> checkLatePenalty = transactionsRepository
					.findByRepaymentRepaymentId(paymentStatementDTO.getRepaymentId());
			double totalLatePenalty = checkLatePenalty.stream()
					.filter(p -> p.getLatePenalty() != null && p.getLatePenalty() > 0)
					.mapToDouble(p -> p.getLatePenalty()).sum();

			double paidCheckBouce = checkLatePenalty.stream()
					.filter(p -> p.getChequeReturnCharges() != null && p.getChequeReturnCharges() > 0)
					.mapToDouble(p -> p.getChequeReturnCharges()).sum();

			BigDecimal interest = BigDecimal.valueOf(paymentStatementDTO.getInterest()).setScale(2,
					RoundingMode.HALF_UP);
			BigDecimal principalAmt = BigDecimal.valueOf(paymentStatementDTO.getPrincipalAmount()).setScale(2,
					RoundingMode.HALF_UP);
			BigDecimal amountPaid = BigDecimal.valueOf(paymentStatementDTO.getAmountPaid()).setScale(2,
					RoundingMode.HALF_UP);
			BigDecimal dueAmount = BigDecimal.valueOf(paymentStatementDTO.getDueAmount()).setScale(2,
					RoundingMode.HALF_UP);

			totalInterest = totalInterest.add(interest);
			repaid = repaid.add(amountPaid);
			outstanding = outstanding.add(principalAmt);

			loanAmount = BigDecimal.valueOf(paymentStatementDTO.getAmountSanctioned()).setScale(2,
					RoundingMode.HALF_UP);

			if ((dueDate.getYear() < now.getYear()
					|| (dueDate.getYear() == now.getYear() && dueDate.getMonthValue() <= now.getMonthValue()))
					&& dueAmount.compareTo(BigDecimal.ZERO) != 0) {
				receipetResponse.setDueDate(dueDate);
				totalDue = totalDue.add(dueAmount);
			}

			// Reset latePenalty and cheque return for each iteration
			latePenalty = BigDecimal.ZERO;
			chequeReturnCharge = BigDecimal.ZERO;

			CheckDeposit cheque = checkDepositRepository.LmsRepaymentRepaymentId(paymentStatementDTO.getRepaymentId());

			if (cheque != null && !cheque.getStatus().equalsIgnoreCase("Cleared")
					&& !paymentStatementDTO.getAmountStatus().equalsIgnoreCase("PAID") && paidCheckBouce < 500) {
				double charges = cheque.getChequeReturnCharges() != null ? cheque.getChequeReturnCharges() : 0.0;
				chequeReturnCharge = BigDecimal.valueOf(charges).setScale(2, RoundingMode.HALF_UP);
			} else {

				if (now.isAfter(dueDate) && totalLatePenalty < 500 && paidCheckBouce < 500
						&& dueAmount.compareTo(BigDecimal.ZERO) != 0) {
					latePenalty = BigDecimal.valueOf(500 - totalLatePenalty);

				}
			}

			if (now.isAfter(dueDate) && dueAmount.compareTo(BigDecimal.ZERO) != 0) {
				numberOfEmisDue++;
				LocalDate firstOfMonth = dueDate.withDayOfMonth(1);
				long overdueDays = ChronoUnit.DAYS.between(firstOfMonth, now);
				double dailyPenaltyRate = 0.03 / 30;

				BigDecimal currentEmiPenalty = dueAmount.multiply(BigDecimal.valueOf(dailyPenaltyRate))
						.multiply(BigDecimal.valueOf(overdueDays)).setScale(2, RoundingMode.HALF_UP);

				emiPenalty = emiPenalty.add(currentEmiPenalty);
				totalPenalty = totalPenalty.add(currentEmiPenalty).add(chequeReturnCharge).add(latePenalty).setScale(2,
						RoundingMode.HALF_UP);

				String emiMonth = dueDate.getMonth().toString() + " " + dueDate.getYear();
				emiDueDetails.add(new EmiDueInfoDTO(emiCounter, dueDate, overdueDays, dueAmount.doubleValue(),
						chequeReturnCharge.doubleValue(), currentEmiPenalty.doubleValue(), emiMonth,
						latePenalty.doubleValue()));
			} else if (dueDate.getYear() == now.getYear() && dueDate.getMonthValue() == now.getMonthValue()
					&& dueAmount.compareTo(BigDecimal.ZERO) != 0) {
				numberOfEmisDue++;
				long overdueDays = 0;
				String emiMonth = dueDate.getMonth().toString() + " " + dueDate.getYear();
				emiDueDetails.add(new EmiDueInfoDTO(emiCounter, dueDate, overdueDays, dueAmount.doubleValue(),
						chequeReturnCharge.doubleValue(), 0, emiMonth, latePenalty.doubleValue()));

			}

			totalLatePenalty = 0;
		}
		// Set values with rounding
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

		logger.info("!!! inside class:ReceiptServiceImpl, method: getReceiptInfo() END");
		return receipetResponse;
	}

	@Override
	public ReceiptResponse getReceiptInfoByReceiptDate(Long dId, LocalDate localDate) {
		logger.info("!!! inside class:ReceiptServiceImpl, method: getReceiptInfoByReceiptDate");
		ReceiptResponse receipetResponse = new ReceiptResponse();
		List<PaymentStatementDTO> re = repaymentService.getRepaymentStatement(dId);

		BigDecimal totalInterest = BigDecimal.ZERO;
		BigDecimal principal = BigDecimal.ZERO;
		BigDecimal repaid = BigDecimal.ZERO;
		BigDecimal outstanding = BigDecimal.ZERO;
		BigDecimal totalDue = BigDecimal.ZERO;
		BigDecimal loanAmount = BigDecimal.ZERO;
		BigDecimal emiPenalty = BigDecimal.ZERO;
		BigDecimal totalPenalty = BigDecimal.ZERO;
		BigDecimal chequeReturnCharge = BigDecimal.ZERO;
		BigDecimal latePenalty = BigDecimal.ZERO;

		int emiCounter = 0;
		int numberOfEmisDue = 0;

		LocalDate now = localDate;
		List<EmiDueInfoDTO> emiDueDetails = new ArrayList<>();

		for (PaymentStatementDTO paymentStatementDTO : re) {
			emiCounter++;
			LocalDate dueDate = paymentStatementDTO.getDueDate();

			List<RepaymentTransactions> checkLatePenalty = transactionsRepository
					.findByRepaymentRepaymentId(paymentStatementDTO.getRepaymentId());
			double totalLatePenalty = checkLatePenalty.stream()
					.filter(p -> p.getLatePenalty() != null && p.getLatePenalty() > 0)
					.mapToDouble(p -> p.getLatePenalty()).sum();

			double paidCheckBouce = checkLatePenalty.stream()
					.filter(p -> p.getChequeReturnCharges() != null && p.getChequeReturnCharges() > 0)
					.mapToDouble(p -> p.getChequeReturnCharges()).sum();

			BigDecimal interest = BigDecimal.valueOf(paymentStatementDTO.getInterest()).setScale(2,
					RoundingMode.HALF_UP);
			BigDecimal principalAmt = BigDecimal.valueOf(paymentStatementDTO.getPrincipalAmount()).setScale(2,
					RoundingMode.HALF_UP);
			BigDecimal amountPaid = BigDecimal.valueOf(paymentStatementDTO.getAmountPaid()).setScale(2,
					RoundingMode.HALF_UP);
			BigDecimal dueAmount = BigDecimal.valueOf(paymentStatementDTO.getDueAmount()).setScale(2,
					RoundingMode.HALF_UP);

			totalInterest = totalInterest.add(interest);
			repaid = repaid.add(amountPaid);
			outstanding = outstanding.add(principalAmt);

			loanAmount = BigDecimal.valueOf(paymentStatementDTO.getAmountSanctioned()).setScale(2,
					RoundingMode.HALF_UP);

			if ((dueDate.getYear() < now.getYear()
					|| (dueDate.getYear() == now.getYear() && dueDate.getMonthValue() <= now.getMonthValue()))
					&& dueAmount.compareTo(BigDecimal.ZERO) != 0) {
				receipetResponse.setDueDate(dueDate);
				totalDue = totalDue.add(dueAmount);
			}

			// Reset latePenalty and cheque return for each iteration
			latePenalty = BigDecimal.ZERO;
			chequeReturnCharge = BigDecimal.ZERO;

			CheckDeposit cheque = checkDepositRepository.LmsRepaymentRepaymentId(paymentStatementDTO.getRepaymentId());

			if (cheque != null && !cheque.getStatus().equalsIgnoreCase("Cleared")
					&& !paymentStatementDTO.getAmountStatus().equalsIgnoreCase("PAID") && paidCheckBouce < 500) {
				double charges = cheque.getChequeReturnCharges() != null ? cheque.getChequeReturnCharges() : 0.0;
				chequeReturnCharge = BigDecimal.valueOf(charges).setScale(2, RoundingMode.HALF_UP);
			} else {

				if (now.isAfter(dueDate) && totalLatePenalty < 500 && paidCheckBouce < 500
						&& dueAmount.compareTo(BigDecimal.ZERO) != 0) {
					latePenalty = BigDecimal.valueOf(500 - totalLatePenalty);

				}
			}

			if (now.isAfter(dueDate) && dueAmount.compareTo(BigDecimal.ZERO) != 0) {
				numberOfEmisDue++;
				LocalDate firstOfMonth = dueDate.withDayOfMonth(1);
				long overdueDays = ChronoUnit.DAYS.between(firstOfMonth, now);
				double dailyPenaltyRate = 0.03 / 30;

				BigDecimal currentEmiPenalty = dueAmount.multiply(BigDecimal.valueOf(dailyPenaltyRate))
						.multiply(BigDecimal.valueOf(overdueDays)).setScale(2, RoundingMode.HALF_UP);

				emiPenalty = emiPenalty.add(currentEmiPenalty);
				totalPenalty = totalPenalty.add(currentEmiPenalty).add(chequeReturnCharge).add(latePenalty).setScale(2,
						RoundingMode.HALF_UP);

				String emiMonth = dueDate.getMonth().toString() + " " + dueDate.getYear();
				emiDueDetails.add(new EmiDueInfoDTO(emiCounter, dueDate, overdueDays, dueAmount.doubleValue(),
						chequeReturnCharge.doubleValue(), currentEmiPenalty.doubleValue(), emiMonth,
						latePenalty.doubleValue()));
			} else if (dueDate.getYear() == now.getYear() && dueDate.getMonthValue() == now.getMonthValue()
					&& dueAmount.compareTo(BigDecimal.ZERO) != 0) {
				numberOfEmisDue++;
				long overdueDays = 0;
				String emiMonth = dueDate.getMonth().toString() + " " + dueDate.getYear();
				emiDueDetails.add(new EmiDueInfoDTO(emiCounter, dueDate, overdueDays, dueAmount.doubleValue(),
						chequeReturnCharge.doubleValue(), 0, emiMonth, latePenalty.doubleValue()));

			}

			totalLatePenalty = 0;
		}

		// Set values with rounding
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

		logger.info("!!! inside class:ReceiptServiceImpl, method: getReceiptInfoByReceiptDate");
		return receipetResponse;
	}

	@Override
	public ReceiptResponse payLoan(PayRequest payRequest) {
		logger.info("!!! inside class:ReceiptServiceImpl, method: payLoan");
		ReceiptResponse receiptResponse = new ReceiptResponse();
		List<PaymentStatementDTO> repaymentList = repaymentService.getRepaymentStatement(payRequest.getDid());

		BigDecimal paidAmount = BigDecimal.valueOf(payRequest.getAmount());
		LocalDate paymentDate = payRequest.getPaymentDate();

		repaymentList.sort(Comparator.comparing(PaymentStatementDTO::getDueDate));

		for (PaymentStatementDTO payment : repaymentList) {
			LocalDate dueDate = payment.getDueDate();

			CheckDeposit cheque = checkDepositRepository.LmsRepaymentRepaymentId(payment.getRepaymentId());
			List<RepaymentTransactions> checkLatePenalty = transactionsRepository
					.findByRepaymentRepaymentId(payment.getRepaymentId());
			double totalLatePenalty = checkLatePenalty.stream()
					.filter(p -> p.getLatePenalty() != null && p.getLatePenalty() > 0)
					.mapToDouble(p -> p.getLatePenalty()).sum();

			double paidCheckBouce = checkLatePenalty.stream()
					.filter(p -> p.getChequeReturnCharges() != null && p.getChequeReturnCharges() > 0)
					.mapToDouble(p -> p.getChequeReturnCharges()).sum();

			BigDecimal chequeReturnCharge;
			BigDecimal latePenalty = BigDecimal.ZERO;

			if (cheque != null && !cheque.getStatus().equalsIgnoreCase("Cleared")
					&& !payment.getAmountStatus().equalsIgnoreCase("PAID") && paidCheckBouce < 500) {
				double charges = cheque.getChequeReturnCharges() != null ? cheque.getChequeReturnCharges() : 0.0;
				chequeReturnCharge = BigDecimal.valueOf(charges).setScale(2, RoundingMode.HALF_UP);
			} else {
				chequeReturnCharge = BigDecimal.ZERO;
				if (paymentDate.isAfter(dueDate) && totalLatePenalty < 500 && paidCheckBouce < 500
						&& payment.getDueAmount() != 0) {
					latePenalty = BigDecimal.valueOf(500 - totalLatePenalty);

				}
			}

			BigDecimal dueAmount = BigDecimal.valueOf(Math.max(0, payment.getDueAmount()));
			if (dueAmount.compareTo(BigDecimal.ZERO) == 0)
				continue;
			BigDecimal emiPenalty = BigDecimal.ZERO;
			if (paymentDate.isAfter(dueDate) && dueAmount.compareTo(BigDecimal.ZERO) != 0) {
				LocalDate firstOfMonth = dueDate.withDayOfMonth(1);
				long overdueDays = ChronoUnit.DAYS.between(firstOfMonth, paymentDate);
				double dailyPenaltyRate = 0.03 / 30;
				emiPenalty = dueAmount.multiply(BigDecimal.valueOf(dailyPenaltyRate))
						.multiply(BigDecimal.valueOf(Math.max(overdueDays, 0)));
				emiPenalty = BigDecimal.valueOf(roundToNearestRupee(emiPenalty));
			}

			BigDecimal penaltyComponent = emiPenalty;
			BigDecimal chequeReturnChargesComponent = chequeReturnCharge;
			BigDecimal latePenaltyComponent = latePenalty;

			BigDecimal penaltyPaid = BigDecimal.ZERO;
			BigDecimal chequeReturnChargesPaid = BigDecimal.ZERO;
			BigDecimal latePenaltyPaid = BigDecimal.ZERO;
			BigDecimal interestPaid = BigDecimal.ZERO;
			BigDecimal principalPaid = BigDecimal.ZERO;

			// Deduct penalty
			if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
				penaltyPaid = paidAmount.min(penaltyComponent);
				paidAmount = paidAmount.subtract(penaltyPaid);
			}

			if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
				chequeReturnChargesPaid = paidAmount.min(chequeReturnChargesComponent);
				paidAmount = paidAmount.subtract(chequeReturnChargesPaid);
			}

			if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
				latePenaltyPaid = paidAmount.min(latePenaltyComponent);
				paidAmount = paidAmount.subtract(latePenaltyPaid);
			}

			BigDecimal interest = BigDecimal.valueOf(payment.getInterest());
			if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
				interestPaid = paidAmount.min(interest);
				paidAmount = paidAmount.subtract(interestPaid);
			}

			BigDecimal principal = BigDecimal.valueOf(payment.getPrincipalAmount());
			if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
				principalPaid = paidAmount.min(principal);
				paidAmount = paidAmount.subtract(principalPaid);
			}

			BigDecimal remainingInterest = interest.subtract(interestPaid).max(BigDecimal.ZERO);
			BigDecimal remainingPrincipal = principal.subtract(principalPaid).max(BigDecimal.ZERO);
			BigDecimal emiPaid = principalPaid.add(interestPaid);
			BigDecimal remainingDue = dueAmount.subtract(emiPaid).max(BigDecimal.ZERO);

			String emiMonth = dueDate.getMonth().toString() + " " + dueDate.getYear();
			LmsRepayment lmsRepayment = repaymentService.getRepaymentById(payment.getRepaymentId());

			RepaymentTransactions transaction = new RepaymentTransactions();
			transaction.setAmountPaid(roundToNearestRupee(emiPaid));
			transaction.setOtherCharges(payRequest.getOtherCharges());
			transaction.setPaymentMode(payRequest.getPaymentMode());
			transaction.setInterestComponent(roundToNearestRupee(interestPaid));
			transaction.setPenaltyComponent(roundToNearestRupee(penaltyPaid));
			transaction.setPrincipalComponent(roundToNearestRupee(principalPaid));
			transaction.setLatePenalty(roundToNearestRupee(latePenaltyPaid));
			transaction.setChequeReturnCharges(roundToNearestRupee(chequeReturnChargesPaid));
			transaction.setPaymentDate(paymentDate);
			transaction.setDescription("EMI Payment - " + emiMonth);
			transaction.setRepayment(lmsRepayment);

			if (roundToNearestRupee(remainingPrincipal) == 1.0) {
				remainingPrincipal = BigDecimal.ZERO;
			}
			lmsRepayment.setInterest(roundToNearestRupee(remainingInterest));
			lmsRepayment.setPrincipalAmount(roundToNearestRupee(remainingPrincipal));
			lmsRepayment.setDueAmount(roundToNearestRupee(remainingDue));
			lmsRepayment.setAmountPaid(roundToNearestRupee(BigDecimal.valueOf(payment.getAmountPaid()).add(emiPaid)));
			lmsRepayment.setOtherCharges(payRequest.getOtherCharges() != null ? payRequest.getOtherCharges() : 0d);

			if (remainingDue.compareTo(BigDecimal.valueOf(1.00)) <= 0) {
				lmsRepayment.setAmountStatus("Paid");
				lmsRepayment.setDueAmount(0.0);
			}

			transactionsRepository.save(transaction);
			repaymentRepository.save(lmsRepayment);

//			System.err.println("Repayment:" + lmsRepayment);
//			System.err.println("transaction:" + transaction);

			if (paidAmount.compareTo(BigDecimal.ONE) < 0)
				break;
		}

		return receiptResponse;
	}

	private double roundToNearestRupee(BigDecimal amount) {
		return amount.setScale(0, RoundingMode.HALF_UP).doubleValue();
	}

}
