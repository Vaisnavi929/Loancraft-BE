package com.narvee.serviceimpl;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.Service.DashBoardService;
import com.narvee.repository.CustomersRepository;
import com.narvee.repository.DisbursmentRepository;
import com.narvee.repository.RepaymentTransactionsRepository;

@Service
public class DashBoardServiceImpl implements DashBoardService {

	private static final Logger logger = LoggerFactory.getLogger(ReceiptServiceImpl.class);

	@Autowired
	private DisbursmentRepository disbursmentRepository;
	@Autowired
	private RepaymentTransactionsRepository repaymentTransactionsRepository;

	@Autowired
	private CustomersRepository customersRepository;

	@Override
	public Map<String, Object> getDashboardSummary() {
	    logger.info("!!! inside class:DashBoardServiceImpl ,!! method:getDashboardSummary()");

	    // Fetch Disbursement Summary
	    List<Object[]> disbursmentResults = disbursmentRepository.findAllDisbursedIdAndAmounts();
	    long disbursmentCount = 0;
	    BigDecimal totalDisbursedAmount = BigDecimal.ZERO;

	    for (Object[] row : disbursmentResults) {
	        if (row[1] != null) {
	            BigDecimal amount = new BigDecimal(row[1].toString());
	            totalDisbursedAmount = totalDisbursedAmount.add(amount);
	            disbursmentCount++;
	        }
	    }

	    // Fetch Repayment Summary
	    List<Object[]> repaymentResults = repaymentTransactionsRepository.findAlltransactionsIdAndInterestandprincipalList();
	    long repaymentCount = 0;
	    double interestPaid = 0.0;
	    double principalPaid = 0.0;

	    for (Object[] row : repaymentResults) {
	        Double interest = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
	        Double principal = row[2] != null ? ((Number) row[2]).doubleValue() : 0.0;

	        interestPaid += interest;
	        principalPaid += principal;
	        repaymentCount++;
	    }

	    double totalRepaidAmount = interestPaid + principalPaid;

	    // Fetch Customer Count
	    List<Object[]> customerCountResult = customersRepository.findAllCustomersCountList();
	    long totalCustomerCount = 0;

	    if (!customerCountResult.isEmpty() && customerCountResult.get(0)[0] != null) {
	        totalCustomerCount = ((Number) customerCountResult.get(0)[0]).longValue();
	    }

	    // Prepare Maps
	    Map<String, Object> disbursementSummary = new HashMap<>();
	    disbursementSummary.put("totalDisbursementCount", disbursmentCount);
	    disbursementSummary.put("totalDisbursedAmount", totalDisbursedAmount);

	    Map<String, Object> repaymentSummary = new HashMap<>();
	    repaymentSummary.put("totalRepaymentCount", repaymentCount);
	    repaymentSummary.put("totalInterestPaid", interestPaid);
	    repaymentSummary.put("totalPrincipalPaid", principalPaid);
	    repaymentSummary.put("totalRepaidAmount", totalRepaidAmount);

	    Map<String, Object> customerSummary = new HashMap<>();
	    customerSummary.put("totalCustomerCount", totalCustomerCount);

		Map<String, Object> summary = new HashMap<>();
		summary.put("disbursementSummary", disbursementSummary);
		summary.put("repaymentSummary", repaymentSummary);
		summary.put("customerSummary", customerSummary);

		return summary;
	}

}
