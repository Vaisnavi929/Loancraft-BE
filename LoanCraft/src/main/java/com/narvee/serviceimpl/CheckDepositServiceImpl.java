package com.narvee.serviceimpl;

import java.time.LocalDate;
import java.util.List;

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
import com.narvee.repository.CheckDepositRepository;
import com.narvee.repository.RepaymentRepository;
import com.narvee.request.dto.CheckDepositDTO;
import com.narvee.request.dto.CheckDepositResponseDTO;

@Service
public class CheckDepositServiceImpl implements CheckDepositService{

	private static final Logger logger=LoggerFactory.getLogger(CheckDepositServiceImpl.class);

	@Autowired
	private CheckDepositRepository depositRepository;
	@Autowired
	private RepaymentRepository repaymentRepository;
	
	@Autowired
	ModelMapper mapper;
	@Override
	public CheckDeposit saveDeposit(CheckDepositDTO checkDepositDTO) {		
		logger.info("!!! inside class : CheckDepositServiceImpl,!! method: saveBounceEntry()  ");
		CheckDeposit checkDeposit = mapper.map(checkDepositDTO, CheckDeposit.class);
		Disbursment disbursment=new Disbursment();
		disbursment.setDisbursmentId(checkDepositDTO.getDisbursmentId());
		checkDeposit.setDisbursment(disbursment);
		return depositRepository.save(checkDeposit);
	}

	@Override
	public CheckDeposit updateDeposit(CheckDepositDTO checkDepositDTO) {
		logger.info("!!! inside class : CheckDepositServiceImpl,!! method: updateBounceEntry()  ");
		CheckDeposit checkDeposit = mapper.map(checkDepositDTO, CheckDeposit.class);
		Disbursment disbursment=new Disbursment();
		disbursment.setDisbursmentId(checkDepositDTO.getDisbursmentId());
		checkDeposit.setDisbursment(disbursment);
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
		else if (sortField.equalsIgnoreCase("RecievableAmount"))
			sortField = "recievable_amount";
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
			return depositRepository.getAllCheckDepositWithPagination(pageable);

		} else {
			return depositRepository.getAllCheckDepositWithKeyword(pageable, keyword);

		}

	}

	@Override
	public String updateStatus(Long id, String value,String reason) {
		logger.info("!!! inside class:CheckDepositServiceImpl,!! method :updateStatus() ");
	    int rows = depositRepository.updateStatus(id, value, reason);
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
	public List<Object[]> getRepaymentMonthYear(Long disbursmentId) {
		logger.info("!!! inside class:CheckDepositServiceImpl,!! method :getRepaymentMonthYear() ");
		return depositRepository.getRepaymentMonthAndYearByDisbursmentId(disbursmentId);
	}
	
	
}
