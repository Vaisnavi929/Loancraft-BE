package com.narvee.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.Service.LowCheckDepositService;
import com.narvee.entity.LmsRepayment;
import com.narvee.entity.LowCheckDepositApproval;
import com.narvee.repository.LowCheckDepositApprovalRespository;
import com.narvee.request.dto.FetchLowCheckDepositDTO;
import com.narvee.request.dto.LowCheckDepositDTO;

@Service
public class LowCheckDepositServiceImpl implements LowCheckDepositService {

	private static final Logger logger = LoggerFactory.getLogger(LowCheckDepositServiceImpl.class);

	@Autowired
	ModelMapper mapper;

	@Autowired
	private LowCheckDepositApprovalRespository approvalRespository;

	@Override
	public LowCheckDepositApproval saveCheckDepositApproval(LowCheckDepositDTO LowCheckDepositDTO) {
		logger.info("!!! inside class:LowCheckDepositServiceImpl ,!! method:saveCheckDepositApproval() ");
		LowCheckDepositApproval lowCheckDepositApproval = mapper.map(LowCheckDepositDTO, LowCheckDepositApproval.class);
		LmsRepayment lmsRepayment = new LmsRepayment();
		lmsRepayment.setRepaymentId(LowCheckDepositDTO.getRepaymentId());
		lowCheckDepositApproval.setLmsRepayment(lmsRepayment);
		return approvalRespository.save(lowCheckDepositApproval);
	}

	@Override
	public LowCheckDepositApproval updateCheckDepositApproval(LowCheckDepositDTO LowCheckDepositDTO) {
		logger.info("!!! inside class:LowCheckDepositServiceImpl ,!! method:updateCheckDepositApproval() ");
		LowCheckDepositApproval lowCheckDepositApproval = mapper.map(LowCheckDepositDTO, LowCheckDepositApproval.class);
		LmsRepayment lmsRepayment = new LmsRepayment();
		lmsRepayment.setRepaymentId(LowCheckDepositDTO.getRepaymentId());
		lowCheckDepositApproval.setLmsRepayment(lmsRepayment);
		return approvalRespository.save(lowCheckDepositApproval);
	}

	@Override
	public LowCheckDepositApproval getById(Long id) {
		logger.info("!!! inside class:LowCheckDepositServiceImpl ,!! method:getById() ");
		return approvalRespository.findById(id).get();
	}

	@Override
	public List<LowCheckDepositApproval> getAllLowCheckDepositApproval() {
		logger.info("!!! inside class:LowCheckDepositServiceImpl ,!! method:getAllLowCheckDepositApproval() ");
		return approvalRespository.findAll();
	}

	@Override
	public List<FetchLowCheckDepositDTO> getLowCheckDepositDetails() {
		logger.info("!!! inside class:LowCheckDepositServiceImpl ,!! method:getLowCheckDepositDetails() ");
		return approvalRespository.fetchLowCheckDepositDetails();
	}

	@Override
	public Boolean updateIsApproved(Long id, Boolean value) {
		logger.info("!!! inside class:CheckDepositServiceImpl,!! method :updateIsApproved() ");
		int updatedCount = approvalRespository.updateIsApproved(id, value);
		return updatedCount > 0;
	}
}