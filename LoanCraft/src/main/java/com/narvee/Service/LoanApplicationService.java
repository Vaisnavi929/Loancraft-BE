package com.narvee.Service;

import java.util.List;

import com.narvee.entity.LoanApplication;
import com.narvee.request.dto.LoanApplicationDto;

public interface LoanApplicationService {
	
	public LoanApplication saveLoanApplication(LoanApplicationDto loanApplication);
	public LoanApplication updateLoanApplication(LoanApplicationDto loanApplication);
	public LoanApplication getById(Long loanId);
	public List<LoanApplication> getAll();
	public boolean deltedById(Long loanId);
	
	public boolean existsByAadharNumber(String aadharNumber);
	
}
