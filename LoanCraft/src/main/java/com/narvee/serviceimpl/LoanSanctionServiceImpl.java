package com.narvee.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.Service.LoanSanctionservice;
import com.narvee.entity.LoanApplication;
import com.narvee.entity.LoanSanction;
import com.narvee.repository.LoanSanctionRepository;
import com.narvee.request.dto.LoanSanctionDTO;


@Service
public class LoanSanctionServiceImpl implements LoanSanctionservice{
	
	public static final Logger logger=LoggerFactory.getLogger(LoanSanctionServiceImpl.class);
	
	@Autowired
	private LoanSanctionRepository loanSanctionRepository;
	
	@Autowired
	ModelMapper mapper;

	@Override
	public LoanSanction saveLoanSanction(LoanSanctionDTO loanSanctionDTO) {
		logger.info("!!! inside class : LoanSanctionServiceImpl ,!! method :saveLoanSanction");
		LoanSanction loanSanction=	mapper.map(loanSanctionDTO, LoanSanction.class);
		LoanApplication loanApplication=new LoanApplication();
		loanApplication.setLoanId(loanSanctionDTO.getLoanId());
		loanSanction.setLoanApplication(loanApplication);
		return loanSanctionRepository.save(loanSanction);
	}


	@Override
	public LoanSanction updateLoanSanction(LoanSanctionDTO loanSanctionDTO) {
		logger.info("!!! inside class : LoanSanctionServiceImpl ,!! method :saveLoanSanction");
		LoanSanction loanSanction=	mapper.map(loanSanctionDTO, LoanSanction.class);
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

}
