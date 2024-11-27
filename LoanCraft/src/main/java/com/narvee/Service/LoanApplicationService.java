package com.narvee.Service;

import java.io.InputStream;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.narvee.entity.LoanApplication;
import com.narvee.request.dto.LoanApplicationDto;

public interface LoanApplicationService {
	
	public LoanApplication saveLoanApplication(LoanApplicationDto loanApplication);
	public LoanApplication updateLoanApplication(LoanApplicationDto loanApplication);
	public LoanApplication getById(Long loanId);
	public List<LoanApplication> getAll();
	public boolean deltedById(Long loanId);
	
}
