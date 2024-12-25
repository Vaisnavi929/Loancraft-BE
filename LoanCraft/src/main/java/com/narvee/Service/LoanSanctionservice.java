package com.narvee.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.narvee.entity.LoanSanction;
import com.narvee.request.dto.LoanSanctionDTO;
import com.narvee.request.dto.LoanSanctionDTOS;

public interface LoanSanctionservice {
	
	public LoanSanction saveLoanSanction(LoanSanctionDTO loanSanctionDTO);
	public LoanSanction updateLoanSanction(LoanSanctionDTO LoanSanctionDTO);
	public LoanSanction getById(Long loanSactionId);
	public List<LoanSanction> getAllLoanSanction();
	public Boolean deleteById(Long loanSactionId);
	public Page<LoanSanctionDTOS> getAllLoanSanctionWithPAgination(SortingRequestDto sortingRequestDto);

}
