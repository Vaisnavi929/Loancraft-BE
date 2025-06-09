package com.narvee.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.Service.CheckBounceService;
import com.narvee.entity.CheckBounceEntry;
import com.narvee.entity.LmsRepayment;
import com.narvee.repository.CheckBounceRepository;
import com.narvee.repository.RepaymentRepository;
import com.narvee.request.dto.CheckBounceDTO;

@Service
public class CheckBounceServiceImpl implements CheckBounceService{

	private static final Logger logger=LoggerFactory.getLogger(CheckBounceServiceImpl.class);

	@Autowired
	private CheckBounceRepository bounceRepository;
	@Autowired
	private RepaymentRepository repaymentRepository;
	
	@Autowired
	ModelMapper mapper;
	@Override
	public CheckBounceEntry saveBounceEntry(CheckBounceDTO checkBounceDTO) {		
		logger.info("!!! inside class : CheckBounceServiceImpl,!! method: saveBounceEntry  ");
		CheckBounceEntry checkBounceEntry = mapper.map(checkBounceDTO, CheckBounceEntry.class);
		LmsRepayment lmsRepayment=new LmsRepayment();
		lmsRepayment.setRepaymentId(checkBounceDTO.getRepaymentId());
		checkBounceEntry.setLmsRepayment(lmsRepayment);
		return bounceRepository.save(checkBounceEntry);
	}

	@Override
	public CheckBounceEntry updateBounceEntry(CheckBounceDTO checkBounceDTO) {
		logger.info("!!! inside class : CheckBounceServiceImpl,!! method: updateBounceEntry  ");
		CheckBounceEntry checkBounceEntry = mapper.map(checkBounceDTO, CheckBounceEntry.class);
		LmsRepayment lmsRepayment=new LmsRepayment();
		lmsRepayment.setRepaymentId(checkBounceDTO.getRepaymentId());
		checkBounceEntry.setLmsRepayment(lmsRepayment);
		return bounceRepository.save(checkBounceEntry);
	}


	@Override
	public List<CheckBounceEntry> getAllCheckBounce() {
		logger.info("!!! inside class : CheckBounceServiceImpl,method : getAllCheckBounce");
		return bounceRepository.findAll();
	}

}
