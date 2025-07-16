package com.narvee.serviceimpl;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.Service.DashBoardService;
import com.narvee.repository.DisbursmentRepository;
import com.narvee.repository.RepaymentTransactionsRepository;

@Service
public class DashBoardServiceImpl implements DashBoardService {

	private static final Logger logger = LoggerFactory.getLogger(ReceiptServiceImpl.class);

	@Autowired
	private DisbursmentRepository disbursmentRepository;
	@Autowired
	private RepaymentTransactionsRepository repaymentTransactionsRepository;

	@Override
	public Map<String, Object> getDashboardSummary() {
	    logger.info("!!! inside class:DashBoardServiceImpl ,!! method:getDashboardSummary()");

	    // Fetch Disbursement Summary
	    List<Object[]> disbursmentResults = disbursmentRepository.findAllDisbursedIdAndAmounts();
	    long disbursmentCount = 0;
	    double totalDisbursedAmount = 0.0;

	    for (Object[] row : disbursmentResults) {
	        if (row[1] != null) {
	            totalDisbursedAmount += ((Number) row[1]).doubleValue();
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

	    // Prepare Disbursement Summary Map
	    Map<String, Object> disbursementSummary = new HashMap<>();
	    disbursementSummary.put("totalDisbursementCount", disbursmentCount);
	    disbursementSummary.put("totalDisbursedAmount", totalDisbursedAmount);

	    // Prepare Repayment Summary Map
	    Map<String, Object> repaymentSummary = new HashMap<>();
	    repaymentSummary.put("totalRepaymentCount", repaymentCount);
	    repaymentSummary.put("totalInterestPaid", interestPaid);
	    repaymentSummary.put("totalPrincipalPaid", principalPaid);
	    repaymentSummary.put("totalRepaidAmount", totalRepaidAmount);

	    // Combine Both Summaries
	    Map<String, Object> summary = new HashMap<>();
	    summary.put("disbursementSummary", disbursementSummary);
	    summary.put("repaymentSummary", repaymentSummary);

	    return summary;
	}

}
