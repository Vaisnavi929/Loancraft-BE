package com.narvee.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.Service.LoanApplicationService;
import com.narvee.entity.LoanApplication;
import com.narvee.repository.LoanApplicationRepository;
import com.narvee.request.dto.LoanApplicationDto;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LoanApplicationServiceImpl implements LoanApplicationService{
	
	public static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
	
	
	@Autowired
	private LoanApplicationRepository loanApplicationRepository;
	
	@Autowired
	ModelMapper mapper;
	
	private static final int DIGIT_PADDING=4;

	@Override
	public LoanApplication saveLoanApplication(LoanApplicationDto loanApplicationDto) {
	    logger.info("!!! inside class: LoanApplicationServiceImpl , !! method: saveLoanApplication()");
	    LoanApplication loan = mapper.map(loanApplicationDto, LoanApplication.class);
	    Long amaxnumber = loanApplicationRepository.appmaxNumber();
	    if (amaxnumber == null) {
	        amaxnumber = 0L; 
	    }

	    String valueWithPadding = String.format("%0" + DIGIT_PADDING + "d", amaxnumber + 1);
	    String value = "BEACON" + valueWithPadding;
	    loan.setApplicationReferenceId(value);
	    loan.setAmaxnum(amaxnumber + 1); 
	    return loanApplicationRepository.save(loan);
	}


	@Override
	public LoanApplication getById(Long loanId) {
		logger.info("!!! inside class: LoanApplicationServiceImpl , !! method: getById()");
		return loanApplicationRepository.findById(loanId).get();
	}    

	@Override
	public List<LoanApplication> getAll() {
		logger.info("!!! inside class: LoanApplicationServiceImpl , !! method: getAll()");
		return loanApplicationRepository.findAll();
	}
 
	@Override
	public boolean deltedById(Long loanId) {
		logger.info("!!! inside class: LoanApplicationServiceImpl , !! method: deltedById()");
    loanApplicationRepository.deleteById(loanId);
		return true;
	}

	@Override
	public LoanApplication updateLoanApplication(LoanApplicationDto loanApplication) {
	logger.info("!!! inside class: LoanApplicationserviceimpl , !! method: updateLoanApplication");
	LoanApplication loan=mapper.map(loanApplication, LoanApplication.class);
		return loanApplicationRepository.save(loan);
	}


	@Override
	public boolean existsByAadharNumber(String aadharNumber) {
		logger.info("!!! inside class: LoanApplicationserviceimpl , !! method: updateLoanApplication");
		return loanApplicationRepository.existsByAadharNumber(aadharNumber);
	}


}
