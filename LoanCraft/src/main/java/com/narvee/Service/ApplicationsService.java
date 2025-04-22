package com.narvee.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.narvee.entity.Applications;
import com.narvee.request.dto.ApplicationDTO;
import com.narvee.request.dto.DisbursmentIdDTO;
import com.narvee.request.dto.LoanApplicationDto;
import com.narvee.request.dto.LoanApplicationUpdateDto;

public interface ApplicationsService {

	public Applications saveLoanApplication(LoanApplicationDto loanApplication) throws Exception;

	public LoanApplicationUpdateDto updateLoanApplication(LoanApplicationUpdateDto loanApplicationUpdateDto);
//	public Applications updateLoanApplication(LoanApplicationUpdateDto loanApplication);

	public Applications getById(Long loanId);

	public List<Applications> getAll();

	public boolean deltedById(Long loanId);

	public boolean existsByAadharNumber(String aadharNumber);

	public Page<ApplicationDTO> getAllLoanApplicationPagination(SortingRequestDto sortingRequestDto);

	public Page<ApplicationDTO> getAllLoanApplicationListWitApproved(SortingRequestDto sortingRequestDto);

	public void updateLoanStatus(Long loanId, String status, String remarks, Long creditScore);
	
	public List<DisbursmentIdDTO> getReviewedAppDropDown();

}
