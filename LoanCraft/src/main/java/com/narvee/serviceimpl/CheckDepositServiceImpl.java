package com.narvee.serviceimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.narvee.Service.CheckDepositService;
import com.narvee.Service.SortingRequestDto;
import com.narvee.entity.CheckDeposit;
import com.narvee.entity.Disbursment;
import com.narvee.entity.LmsRepayment;
import com.narvee.repository.CheckDepositRepository;
import com.narvee.request.dto.CheckDepositDTO;
import com.narvee.request.dto.CheckDepositResponseDTO;
import com.narvee.request.dto.RepaymentDropdown;

@Service
public class CheckDepositServiceImpl implements CheckDepositService {

	private static final Logger logger = LoggerFactory.getLogger(CheckDepositServiceImpl.class);

	@Autowired
	private CheckDepositRepository depositRepository;
//	@Autowired
//	private RepaymentRepository repaymentRepository;

	@Autowired
	ModelMapper mapper;

	@Override
	public CheckDeposit saveDeposit(CheckDepositDTO checkDepositDTO) {
		logger.info("!!! inside class : CheckDepositServiceImpl,!! method: saveBounceEntry()  ");
		CheckDeposit checkDeposit = mapper.map(checkDepositDTO, CheckDeposit.class);
		Disbursment disbursment = new Disbursment();
		disbursment.setDisbursmentId(checkDepositDTO.getDisbursmentId());
		checkDeposit.setDisbursment(disbursment);
		LmsRepayment lmsRepayment = new LmsRepayment();
		lmsRepayment.setRepaymentId(checkDepositDTO.getRepaymentId());
		checkDeposit.setLmsRepayment(lmsRepayment);
		return depositRepository.save(checkDeposit);
	}

	@Override
	public CheckDeposit updateDeposit(CheckDepositDTO checkDepositDTO) {
		logger.info("!!! inside class : CheckDepositServiceImpl,!! method: updateBounceEntry()  ");
		CheckDeposit checkDeposit = mapper.map(checkDepositDTO, CheckDeposit.class);
		Disbursment disbursment = new Disbursment();
		disbursment.setDisbursmentId(checkDepositDTO.getDisbursmentId());
		checkDeposit.setDisbursment(disbursment);
		LmsRepayment lmsRepayment = new LmsRepayment();
		lmsRepayment.setRepaymentId(checkDepositDTO.getRepaymentId());
		checkDeposit.setLmsRepayment(lmsRepayment);
		return depositRepository.save(checkDeposit);
	}

	@Override
	public Page<CheckDepositResponseDTO> getAllCheckDeposit(SortingRequestDto sortingRequestDto) {
		logger.info("!!! inside class : CheckDepositServiceImpl, !! method : getAllCheckDeposit()");
		String sortField = sortingRequestDto.getSortField();
		String sortOrder = sortingRequestDto.getSortOrder();
		String keyword = sortingRequestDto.getKeyword();
		Integer pageNumber = sortingRequestDto.getPageNumber();
		Integer pageSize = sortingRequestDto.getPageSize();

		if (sortField.equalsIgnoreCase("Amount"))
			sortField = "amount";
		else if (sortField.equalsIgnoreCase("PostingDate"))
			sortField = "posting_date";
		else if (sortField.equalsIgnoreCase("ChequeNumber"))
			sortField = "cheque_number";
		else if (sortField.equalsIgnoreCase("Reason"))
			sortField = "reason";
		else if (sortField.equalsIgnoreCase("Remarks"))
			sortField = "remarks";
		else if (sortField.equalsIgnoreCase("Status"))
			sortField = "status";
		else if (sortField.equalsIgnoreCase("ChequeClearanceDate"))
			sortField = "cheque_clearance_date";
		else if (sortField.equalsIgnoreCase("createddate"))
			sortField = "createddate";
		else if (sortField.equalsIgnoreCase("referenceId"))
			sortField = "referenceId";
		else
			sortField = "updateddate";
		Sort.Direction sortDirection = Sort.Direction.ASC;
		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
		}

		Sort sort = Sort.by(sortDirection, sortField);
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
		if (keyword.equalsIgnoreCase("empty")) {
			return depositRepository.getAllCheckDepositWithPagination(pageable);

		} else {
			return depositRepository.getAllCheckDepositWithKeyword(pageable, keyword);

		}

	}

	@Override
	public String updateStatus(Long id, String status, String reason, LocalDate chequeClearanceDate, Long userid) {
		logger.info("!!! inside class:CheckDepositServiceImpl,!! method :updateStatus() ");
		LocalDateTime now = LocalDateTime.now();
		Double chequeReturnCharges = null;
		if (!status.equalsIgnoreCase("Cleared")) {
			chequeReturnCharges = 500.00;
		} else {
			chequeReturnCharges = 0.0;
		}

		int rows = depositRepository.updateStatus(id, status, reason, userid, chequeReturnCharges, now,chequeClearanceDate);
		if (rows > 0) {
			return "Update successful";
		} else {
			return "No record found to update";
		}
	}

	@Override
	public CheckDeposit getById(Long id) {
		logger.info("!!! inside class:CheckDepositServiceImpl,!! method :getById() ");
		return depositRepository.findById(id).get();
	}

	@Override
	@Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
	public void updatePostedChecksToInProgress() {
		logger.info("!!! inside class:CheckDepositServiceImpl,!! method :updatePostedChecksToInProgress() ");
		LocalDate yesterday = LocalDate.now().minusDays(1);
		List<CheckDeposit> checks = depositRepository.findByStatusAndPostingDate("Posted", yesterday);

		for (CheckDeposit check : checks) {
			check.setStatus("Pending");
		}

		depositRepository.saveAll(checks);
	}

	@Override
	public List<Map<String, Object>> getRepaymentMonthYear(Long disbursmentId) {
	    logger.info("!!! inside class:CheckDepositServiceImpl,!! method :getRepaymentMonthYear() ");

	    List<RepaymentDropdown> repaymentList = depositRepository.getRepaymentMonthAndYearByDisbursmentId(disbursmentId);
	    List<Map<String, Object>> responseList = new ArrayList<>();

	    for (RepaymentDropdown repaymentDropdown : repaymentList) {
	        // Build individual repayment info
	        Map<String, Object> repaymentMap = new LinkedHashMap<>();
	        repaymentMap.put("repaymentId", repaymentDropdown.getRepaymentId());
	        repaymentMap.put("dueMonthYear", repaymentDropdown.getDueMonthYear());

	        // Get requests for this repayment
	        List<Object[]> requests = depositRepository.getlowDepositRequests(repaymentDropdown.getRepaymentId());
	        List<Map<String, Object>> requestList = new ArrayList<>();

	        for (Object[] req : requests) {
	            Map<String, Object> requestMap = new HashMap<>();
	            requestMap.put("repaymentId", req[0]);
	            requestMap.put("requstedAmount", req[1]);
	            requestMap.put("isApproved", req[2]);
	            requestList.add(requestMap);
	        }

	        // Add requests to the repayment object
	        repaymentMap.put("requests", requestList);

	        // Add to final list
	        responseList.add(repaymentMap);
	    }

	    return responseList;
	}



	@Override
	public boolean existsByLmsRepaymentRepaymentId(Long rid) {
		logger.info("!!! inside class:CheckDepositServiceImpl,!! method : ExistsByLmsRepaymentRepaymentId() ");
		return false; //depositRepository.existsByLmsRepaymentRepaymentId(rid);
	}

	@Override
	public boolean updateStausofRequestedCheque(CheckDepositDTO checkDepositDTO) {
		depositRepository.updateStatusofRequestedCheque(checkDepositDTO.getPostingDate(), 
				checkDepositDTO.getChequeNumber(), checkDepositDTO.getAmount(),checkDepositDTO.getStatus());
		return true;
		
	}

}