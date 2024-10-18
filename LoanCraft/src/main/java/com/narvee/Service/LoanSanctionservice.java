package com.narvee.Service;

import java.util.List;

import com.narvee.entity.LoanSanction;
import com.narvee.request.dto.LoanSanctionDTO;

public interface LoanSanctionservice {
	
	public LoanSanction saveLoanSanction(LoanSanctionDTO loanSanctionDTO);
	public LoanSanction updateLoanSanction(LoanSanctionDTO LoanSanctionDTO);
	public LoanSanction getById(Long loanSactionId);
	public List<LoanSanction> getAllLoanSanction();
	public Boolean deleteById(Long loanSactionId);

}
