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

import com.narvee.Service.CheckBounceService;
import com.narvee.Service.SortingRequestDto;
import com.narvee.entity.CheckBounceEntry;
import com.narvee.entity.Disbursment;
import com.narvee.entity.LmsRepayment;
import com.narvee.repository.CheckBounceRepository;
import com.narvee.repository.RepaymentRepository;
import com.narvee.request.dto.CheckBounceDTO;
import com.narvee.request.dto.CheckBounceResponseDTO;

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
		Disbursment disbursment=new Disbursment();
		disbursment.setDisbursmentId(checkBounceDTO.getDisbursmentId());
		checkBounceEntry.setDisbursment(disbursment);
		return bounceRepository.save(checkBounceEntry);
	}

	@Override
	public CheckBounceEntry updateBounceEntry(CheckBounceDTO checkBounceDTO) {
		logger.info("!!! inside class : CheckBounceServiceImpl,!! method: updateBounceEntry  ");
		CheckBounceEntry checkBounceEntry = mapper.map(checkBounceDTO, CheckBounceEntry.class);
		Disbursment disbursment=new Disbursment();
		disbursment.setDisbursmentId(checkBounceDTO.getDisbursmentId());
		checkBounceEntry.setDisbursment(disbursment);
		return bounceRepository.save(checkBounceEntry);
	}


	@Override
	public Page<CheckBounceResponseDTO> getAllCheckBounce(SortingRequestDto sortingRequestDto) {
		logger.info("!!! inside class : CheckBounceServiceImpl,method : getAllCheckBounce");
		String sortField = sortingRequestDto.getSortField();
		String sortOrder = sortingRequestDto.getSortOrder();
		String keyword = sortingRequestDto.getKeyword();
		Integer pageNumber = sortingRequestDto.getPageNumber();
		Integer pageSize = sortingRequestDto.getPageSize();
		
		if (sortField.equalsIgnoreCase("Amount"))
			sortField = "amount";
		else if (sortField.equalsIgnoreCase("BounceCharges"))
			sortField = "bounce_charges";
		else if (sortField.equalsIgnoreCase("BounceDate"))
			sortField = "bounce_date";
		else if (sortField.equalsIgnoreCase("ChequeNumber"))
			sortField = "cheque_number";
		else if (sortField.equalsIgnoreCase("Reason"))
			sortField = "reason";
		else if (sortField.equalsIgnoreCase("Remarks"))
			sortField = "remarks";
		else if (sortField.equalsIgnoreCase("Status"))
			sortField = "status";
		Sort.Direction sortDirection = Sort.Direction.ASC;
		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
		}

		Sort sort = Sort.by(sortDirection, sortField);
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
		if (keyword.equalsIgnoreCase("empty")) {
			return bounceRepository.getAllCheckBounceWithPagination(pageable);

		} else {
			return bounceRepository.getAllCheckBounceWithKeyword(pageable, keyword);

		}

	}

}
