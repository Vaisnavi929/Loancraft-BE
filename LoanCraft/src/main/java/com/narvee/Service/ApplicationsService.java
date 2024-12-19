package com.narvee.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.narvee.entity.Applications;
import com.narvee.request.dto.LoanApplicationDTOS;
import com.narvee.request.dto.LoanApplicationDto;

public interface ApplicationsService {
	
	public Applications saveLoanApplication(LoanApplicationDto loanApplication);
	public Applications updateLoanApplication(LoanApplicationDto loanApplication);
	public Applications getById(Long loanId);
	public List<Applications> getAll();
	public boolean deltedById(Long loanId);
	
	public boolean existsByAadharNumber(String aadharNumber);
	public Page<LoanApplicationDTOS> getAllLoanApplicationPagination(SortingRequestDto sortingRequestDto);
}
