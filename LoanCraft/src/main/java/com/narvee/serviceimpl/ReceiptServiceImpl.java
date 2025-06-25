package com.narvee.serviceimpl;

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

	private double round(double value) {
		return Math.round(value);
	}

	@Override
	public ReceiptResponse getReceiptInfo(Long dId) {
		logger.info("!!! inside class:ReceiptServiceImpl, method: getReceiptInfo() ");

		ReceiptResponse receipetResponse = new ReceiptResponse();
		List<PaymentStatementDTO> re = repaymentService.getRepaymentStatement(dId);
		double totalInterest = 0;
		double principal = 0;
		double repaid = 0;
		double outstanding = 0;
		double totalDue = 0;
		double loanAmount = 0;
		int emiCounter = 0;
		double emiPenalty = 0;
		double totalPenalty = 0;
		int numberOfEmisDue = 0;
		double chequeReturnCharge = 0;

		LocalDate now = LocalDate.now();
		List<EmiDueInfoDTO> emiDueDetails = new ArrayList<>();

		for (PaymentStatementDTO paymentStatementDTO : re) {
			emiCounter++;
			LocalDate dueDate = paymentStatementDTO.getDueDate();
			totalInterest += round(paymentStatementDTO.getInterest());
			principal += round(paymentStatementDTO.getPrincipalAmount());
			repaid += round(paymentStatementDTO.getAmountPaid());
			outstanding = round((totalInterest + principal) - repaid);
			loanAmount = round(paymentStatementDTO.getAmountSanctioned());

			if ((dueDate.getYear() < now.getYear()
					|| (dueDate.getYear() == now.getYear() && dueDate.getMonthValue() <= now.getMonthValue()))
					&& paymentStatementDTO.getDueAmount() != 0) {
				receipetResponse.setDueDate(dueDate);
				totalDue += round(paymentStatementDTO.getDueAmount());
			}

			if (!dueDate.isAfter(now) && paymentStatementDTO.getDueAmount() != 0) {
				numberOfEmisDue++;
				double dueAmount = round(paymentStatementDTO.getDueAmount());
				long overdueDays = ChronoUnit.DAYS.between(paymentStatementDTO.getDueDate(), now);
				double dailyPenaltyRate = 0.03 / 30; // ≈ 0.001 per day

				double currentEmiPenalty = round((dueAmount * dailyPenaltyRate) * overdueDays);
				emiPenalty += currentEmiPenalty;

				CheckDeposit cheque = checkDepositRepository
						.LmsRepaymentRepaymentId(paymentStatementDTO.getRepaymentId());

				if (cheque != null) {
					double charges = cheque.getChequeReturnCharges() != null ? cheque.getChequeReturnCharges() : 0.0;
					chequeReturnCharge = charges;
				} else {
					chequeReturnCharge = 0;
				}

				totalPenalty = round(emiPenalty + chequeReturnCharge);

				String emiMonth = dueDate.getMonth().toString() + " " + dueDate.getYear();

				emiDueDetails.add(new EmiDueInfoDTO(emiCounter, dueDate, overdueDays, dueAmount,
						round(chequeReturnCharge), currentEmiPenalty, emiMonth));
			}
		}

		receipetResponse.setInterest(round(totalInterest));
		receipetResponse.setPrincipal(round(principal));
		receipetResponse.setRepaid(round(repaid));
		receipetResponse.setOutstanding(round(outstanding));
		receipetResponse.setTotalDue(round(totalDue));
		receipetResponse.setNumberOfEmisDue(numberOfEmisDue);
		receipetResponse.setLoanAmount(round(loanAmount));
		receipetResponse.setEmiDueInfo(emiDueDetails);
		receipetResponse.setTotalPenalty(round(totalPenalty));
		receipetResponse.setTotalDue(round(totalDue + totalPenalty + chequeReturnCharge));
		receipetResponse.setEmiDueAmount(round(totalDue));

		logger.info("!!! inside class:ReceiptServiceImpl, method: getReceiptInfo() END");
		return receipetResponse;
	}

	@Override
	public ReceiptResponse getReceiptInfoByReceiptDate(Long dId, LocalDate localDate) {
		logger.info("!!! inside class:ReceiptServiceImpl, method: getReceiptInfoByReceiptDate");
		ReceiptResponse receipetResponse = new ReceiptResponse();
		List<PaymentStatementDTO> re = repaymentService.getRepaymentStatement(dId);
		double totalInterest = 0;
		double principal = 0;
		double repaid = 0;
		double outstanding = 0;
		double totalDue = 0;
		double loanAmount = 0;
		int emiCounter = 0;
		double emiPenalty = 0;
		double totalPenalty = 0;
		int numberOfEmisDue = 0;
		double chequeReturnCharge = 0;

		LocalDate now = localDate;
		List<EmiDueInfoDTO> emiDueDetails = new ArrayList<>();

		for (PaymentStatementDTO paymentStatementDTO : re) {
			emiCounter++;
			LocalDate dueDate = paymentStatementDTO.getDueDate();
			totalInterest += paymentStatementDTO.getInterest();
			principal += paymentStatementDTO.getPrincipalAmount();
			repaid += paymentStatementDTO.getAmountPaid();
			outstanding = (totalInterest + principal) - repaid;
			loanAmount = paymentStatementDTO.getAmountSanctioned();

			if ((dueDate.getYear() < now.getYear()
					|| (dueDate.getYear() == now.getYear() && dueDate.getMonthValue() <= now.getMonthValue()))
					&& paymentStatementDTO.getDueAmount() != 0) {
				receipetResponse.setDueDate(dueDate);
				totalDue += paymentStatementDTO.getDueAmount();
			}

			if (!dueDate.isAfter(now) && paymentStatementDTO.getDueAmount() != 0) {
				numberOfEmisDue++;
				double dueAmount = paymentStatementDTO.getDueAmount();
				long overdueDays = ChronoUnit.DAYS.between(paymentStatementDTO.getDueDate(), now);
				double dailyPenaltyRate = 0.03 / 30; // 3% per month ≈ 0.1% per day
				if (overdueDays > 0) {
					chequeReturnCharge = 500;
				} else {
					chequeReturnCharge = 0;
				}

				emiPenalty += (dueAmount * dailyPenaltyRate) * overdueDays;
				totalPenalty += Math.round(emiPenalty + chequeReturnCharge);

				String emiMonth = dueDate.getMonth().toString() + " " + dueDate.getYear();

				emiDueDetails.add(new EmiDueInfoDTO(emiCounter, dueDate, overdueDays,
						paymentStatementDTO.getDueAmount(), chequeReturnCharge, emiPenalty, emiMonth));

			}

		}

		receipetResponse.setInterest(totalInterest);
		receipetResponse.setPrincipal(principal);
		receipetResponse.setRepaid(repaid);
		receipetResponse.setOutstanding(outstanding);
		receipetResponse.setTotalDue(totalDue + totalPenalty);
		receipetResponse.setEmiDueAmount(totalDue);
		receipetResponse.setNumberOfEmisDue(numberOfEmisDue);
		receipetResponse.setLoanAmount(loanAmount);
		receipetResponse.setEmiDueInfo(emiDueDetails);
		receipetResponse.setTotalPenalty(totalPenalty);
		// Make sure your DTO has this field

		logger.info("!!! inside class:ReceiptServiceImpl, method: getReceiptInfoByReceiptDate");

		return receipetResponse;
	}

	@Override
	public ReceiptResponse payLoan(PayRequest payRequest) {
		logger.info("!!! inside class:ReceiptServiceImpl, method: payLoan");

		ReceiptResponse receiptResponse = new ReceiptResponse();
		List<PaymentStatementDTO> repaymentList = repaymentService.getRepaymentStatement(payRequest.getDid());

		int emiCounter = 0;
		double paidAmount = payRequest.getAmount();
		LocalDate paymentDate = LocalDate.now();

		List<EmiDueInfoDTO> emiDueDetails = new ArrayList<>();

		// Sort the EMIs by due date (ascending)
		repaymentList.sort(Comparator.comparing(PaymentStatementDTO::getDueDate));

		for (PaymentStatementDTO payment : repaymentList) {
			emiCounter++;
			LocalDate dueDate = payment.getDueDate();

			if (payment.getDueAmount() != 0) {
				double dueAmount = payment.getDueAmount();

				long overdueDays = ChronoUnit.DAYS.between(dueDate, paymentDate);
				double dailyPenaltyRate = 0.03 / 30;
				double emiPenalty = Math.max(0, (dueAmount * dailyPenaltyRate) * overdueDays); // ensure non-negative
				double chequeReturnCharge = 0;
				double penalty = Math.round(emiPenalty + chequeReturnCharge);

				// Payment breakdown
				double penaltyPaid = 0, interestPaid = 0, principalPaid = 0;

				if (paidAmount > 0) {
					penaltyPaid = Math.min(paidAmount, penalty);
					paidAmount -= penaltyPaid;
				}

				if (paidAmount > 0) {
					interestPaid = Math.min(paidAmount, payment.getInterest());
					paidAmount -= interestPaid;
				}

				if (paidAmount > 0) {
					principalPaid = Math.min(paidAmount, payment.getPrincipalAmount());
					paidAmount -= principalPaid;
				}

				double remainingInterest = payment.getInterest() - interestPaid;
				double remainingPrincipal = payment.getPrincipalAmount() - principalPaid;
				double emiPaid = principalPaid + interestPaid;
				double remainingDue = dueAmount - emiPaid;

				String emiMonth = dueDate.getMonth().toString() + " " + dueDate.getYear();
				emiDueDetails.add(new EmiDueInfoDTO(emiCounter, dueDate, overdueDays, dueAmount, chequeReturnCharge,
						emiPenalty, emiMonth));

				LmsRepayment lmsRepayment = repaymentService.getRepaymentById(payment.getRepaymentId());

				RepaymentTransactions transaction = new RepaymentTransactions();
				transaction.setAmountPaid(emiPaid);
				transaction.setPaymentMode(payRequest.getPaymentMode());
				transaction.setInterestComponent(interestPaid);
				transaction.setPenaltyComponent(penaltyPaid);
				transaction.setPrincipalComponent(principalPaid);
				transaction.setPaymentDate(paymentDate);
				transaction.setDescription("EMI Payment - " + emiMonth);
				transaction.setRepayment(lmsRepayment);

				// Update repayment
				lmsRepayment.setInterest(remainingInterest);
				lmsRepayment.setPrincipalAmount(remainingPrincipal);
				lmsRepayment.setDueAmount(remainingDue);
				lmsRepayment.setAmountPaid(payment.getAmountPaid() + emiPaid);

				if (remainingDue == 0) {
					lmsRepayment.setAmountStatus("Paid");
				}

				// Save both
				transactionsRepository.save(transaction);
				repaymentRepository.save(lmsRepayment);

				if (paidAmount <= 0)
					break;
			}
		}

		// You can set response fields like receipt number, paid amount, details, etc.
//	    receiptResponse.setEmiDueDetails(emiDueDetails);
//	    receiptResponse.setPaidAmount(payRequest.getAmount());
//	    receiptResponse.setPaymentDate(paymentDate);
//	    receiptResponse.setMessage("Payment processed successfully");

		return receiptResponse;
	}

}
