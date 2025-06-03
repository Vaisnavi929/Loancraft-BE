package com.narvee.serviceimpl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.Service.ReceiptService;
import com.narvee.Service.RepaymentService;
import com.narvee.helpers.EmiDueInfoDTO;
import com.narvee.request.dto.PaymentStatementDTO;
import com.narvee.response.ReceipetResponse;

@Service
public class ReceiptServiceImpl implements ReceiptService {
	private static final Logger logger = LoggerFactory.getLogger(ReceiptServiceImpl.class);

	@Autowired
	private RepaymentService repaymentService;

	@Override
	public ReceipetResponse getReceiptInfo(Long dId) {
		logger.info("!!! inside class:ReceiptServiceImpl, method: getReceiptInfo() ");

		ReceipetResponse receipetResponse = new ReceipetResponse();
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

				emiPenalty += (dueAmount * dailyPenaltyRate) * overdueDays;
				totalPenalty += emiPenalty + chequeReturnCharge;

				String emiMonth = dueDate.getMonth().toString() + " " + dueDate.getYear();
				
				if (overdueDays > 0) {
					chequeReturnCharge = 500;
				}

				emiDueDetails.add(new EmiDueInfoDTO(emiCounter, dueDate, overdueDays,
						paymentStatementDTO.getDueAmount(), chequeReturnCharge, emiPenalty, emiMonth));

			}

		}

		receipetResponse.setInterest(totalInterest);
		receipetResponse.setPrincipal(principal);
		receipetResponse.setRepaid(repaid);
		receipetResponse.setOutstanding(outstanding);
		receipetResponse.setTotalDue(totalDue);
		receipetResponse.setNumberOfEmisDue(numberOfEmisDue);
		receipetResponse.setLoanAmount(loanAmount);
		receipetResponse.setEmiDueInfo(emiDueDetails);
		receipetResponse.setTotalPenalty(totalPenalty);
		receipetResponse.setTotalDue(totalDue + totalPenalty + chequeReturnCharge);
		receipetResponse.setEmiDueAmount(totalDue);
		// Make sure your DTO has this field

//		System.err.println("Total Interest: " + totalInterest);
//		System.err.println("Total Principal: " + principal);
//		System.err.println("Total Penalty (3%): " + totalPenalty);
		logger.info("!!! inside class:ReceiptServiceImpl, method: getReceiptInfo() END");
		return receipetResponse;
	}

	@Override
	public ReceipetResponse getReceiptInfoByReceiptDate(Long dId, LocalDate localDate) {
		logger.info("!!! inside class:ReceiptServiceImpl, method: getReceiptInfoByReceiptDate");
		ReceipetResponse receipetResponse = new ReceipetResponse();
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
				System.err.println(paymentStatementDTO.getDueDate() + "--ttt--" + paymentStatementDTO.getDueAmount());

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

}
