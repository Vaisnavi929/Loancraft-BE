


 package com.narvee.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.narvee.Service.LoanSanctionservice;
import com.narvee.Service.SortingRequestDto;
import com.narvee.entity.Applications;
import com.narvee.entity.LoanSanction;
import com.narvee.repository.ApplicationsRepository;
import com.narvee.repository.LoanSanctionRepository;
import com.narvee.request.dto.LoanSanctionDTO;
import com.narvee.request.dto.LoanSanctionDTOS;


@Service
public class LoanSanctionServiceImpl implements LoanSanctionservice{
	
	public static final Logger logger=LoggerFactory.getLogger(LoanSanctionServiceImpl.class);
	
	@Autowired
	private LoanSanctionRepository loanSanctionRepository;
	
	@Autowired
	private ApplicationsRepository applicationRepository;
	
	@Autowired
	ModelMapper mapper;

	@Override
	public LoanSanction saveLoanSanction(LoanSanctionDTO loanSanctionDTO) {
		logger.info("!!! inside class : LoanSanctionServiceImpl ,!! method :saveLoanSanction");
		LoanSanction loanSanction = mapper.map(loanSanctionDTO, LoanSanction.class);

		// Fetch the existing Applications entity
		Applications loanApplication = applicationRepository.findById(loanSanctionDTO.getLoanId())
				.orElseThrow(() -> new RuntimeException("Loan Application not found"));
		loanSanction.setApplications(loanApplication);
		return loanSanctionRepository.save(loanSanction);
	}

	@Override
	public LoanSanction updateLoanSanction(LoanSanctionDTO loanSanctionDTO) {
		logger.info("!!! inside class : LoanSanctionServiceImpl ,!! method :saveLoanSanction");
		LoanSanction loanSanction = mapper.map(loanSanctionDTO, LoanSanction.class);

		
		Applications loanApplication = applicationRepository.findById(loanSanctionDTO.getLoanId())
				.orElseThrow(() -> new RuntimeException("Loan Application not found"));
		loanSanction.setApplications(loanApplication);
		return loanSanctionRepository.save(loanSanction);
	}


	@Override
	public LoanSanction getById(Long loanSactionId) {
	logger.info("!!! inside class : LoanSanctionServiceImpl, !! method : getById");
		
    return loanSanctionRepository.findById(loanSactionId).get();
	}

	@Override
	public List<LoanSanction> getAllLoanSanction() {
		logger.info("!!! inside class : LoanSanctionServiceImpl, !! method :getAllLoanSanction");
		return loanSanctionRepository.findAll();
	}

	@Override
	public Boolean deleteById(Long loanSactionId) {
		logger.info("!!! inside class : LoanSanctionServiceImpl, !! method :deleteById  ");
		loanSanctionRepository.deleteById(loanSactionId);
		return true;
	}


	@Override
	public Page<LoanSanctionDTOS> getAllLoanSanctionWithPAgination(SortingRequestDto sortingRequestDto) {
		
		String sortField = sortingRequestDto.getSortField();
		String sortOrder = sortingRequestDto.getSortOrder();
		String keyword = sortingRequestDto.getKeyword();
		Integer pageNumber = sortingRequestDto.getPageNumber();
		Integer pageSize = sortingRequestDto.getPageSize();
	    if (sortField.equalsIgnoreCase("firstName"))
			sortField = "firstname";
		else if (sortField.equalsIgnoreCase("lastName"))
	     sortField = "lastname";
		else if (sortField.equalsIgnoreCase("rateOfInterest"))
		     sortField = "rate_Of_interest";
		else if (sortField.equalsIgnoreCase("numberOfEmis"))
		     sortField = "number_of_emis";
		else if (sortField.equalsIgnoreCase("emiAmount"))
		     sortField = "emi_amount";
		else if (sortField.equalsIgnoreCase("interest_type"))
		     sortField = "interest_type";   
		else if (sortField.equalsIgnoreCase("repaymentMode"))
		     sortField = "repayment_mode";
		else if (sortField.equalsIgnoreCase("createddate"))
		     sortField = "createddate";
		else if (sortField.equalsIgnoreCase("status"))
		     sortField = "status";
		else
		sortField = "updateddate";
		Sort.Direction sortDirection = Sort.Direction.ASC;
		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
		}

		Sort sort = Sort.by(sortDirection, sortField);
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
		if (keyword.equalsIgnoreCase("empty")) {
			return loanSanctionRepository.getAllLoansactionWithPagination(pageable);
			
			
			
		} else {
			return loanSanctionRepository.getAllLoansactionWithSearching(pageable, keyword);

		}

	}

}
