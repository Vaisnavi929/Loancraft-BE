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

import com.narvee.Service.LowCheckDepositService;
import com.narvee.Service.SortingRequestDto;
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
		lowCheckDepositApproval.setId(null);
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
	public Page<FetchLowCheckDepositDTO> getLowCheckDepositDetails(SortingRequestDto sortingRequestDto) {
		logger.info("!!! inside class:LowCheckDepositServiceImpl ,!! method:getLowCheckDepositDetails() ");
		String sortField = sortingRequestDto.getSortField();
		String sortOrder = sortingRequestDto.getSortOrder();
		String keyword = sortingRequestDto.getKeyword();
		Integer pageNumber = sortingRequestDto.getPageNumber();
		Integer pageSize = sortingRequestDto.getPageSize();
		if (sortField.equalsIgnoreCase("firstName"))
			sortField = "full_name";
		else if (sortField.equalsIgnoreCase("requestedAmount"))
			sortField = "requested_amount";
		else if (sortField.equalsIgnoreCase("remarks"))
			sortField = "remarks";
		else if (sortField.equalsIgnoreCase("requestedByName"))
			sortField = "ru.name";
		else if (sortField.equalsIgnoreCase("approvedByName"))
			sortField = "u.name";
		else if (sortField.equalsIgnoreCase("isApproved"))
			sortField = "is_approved";
		else if (sortField.equalsIgnoreCase("receivableAmount"))
			sortField = "receivable_amount";
		else if (sortField.equalsIgnoreCase("createddate"))
			sortField = "createddate";
		else
			sortField = "updateddate";
		Sort.Direction sortDirection = Sort.Direction.ASC;
		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
		}

		Sort sort = Sort.by(sortDirection, sortField);
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
		if (keyword.equalsIgnoreCase("empty")) {
			return approvalRespository.fetchLowCheckDepositDetails(pageable);

		} else {
			return approvalRespository.fetchLowCheckDepositDetailsWithKeyword(pageable, keyword);

		}
	}

	@Override
	public String updateApprovalStatusAndRemarks(Long id, Boolean value, String remarks) {
		logger.info("!!! inside class:CheckDepositServiceImpl, method:updateApprovalStatusAndRemarks()");
		int rows = approvalRespository.updateIsApprovedAndRemarks(id, value, remarks);
		return rows > 0 ? "Update successful" : "No record found to update";
	}

}