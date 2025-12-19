package com.narvee.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.io.exceptions.IOException;
import com.narvee.entity.CheckDeposit;
import com.narvee.request.dto.CheckDepositDTO;
import com.narvee.request.dto.CheckDepositResponseDTO;
import com.narvee.request.dto.SkippedChequeResponseDTO;

public interface CheckDepositService {

	public CheckDeposit saveDeposit(CheckDepositDTO checkBounceDTO);
	

	public CheckDeposit updateDeposit(CheckDepositDTO checkBounceDTO);

	public Page<CheckDepositResponseDTO> getAllCheckDeposit(SortingRequestDto sortingRequestDto);

	public CheckDeposit getById(Long id);

	public String updateStatus(Long id, String status, String reason, LocalDate chequeClearanceDate, Long userid);

	public void updatePostedChecksToInProgress();

	public List<Map<String, Object>> getRepaymentMonthYear(Long disbursmentId);

	public boolean existsByLmsRepaymentRepaymentId(Long rid);
	
	public boolean updateStausofRequestedCheque(CheckDepositDTO checkDepositDTO);
	
	//Added by Abhinay
	public List<SkippedChequeResponseDTO> saveCheckDepositExcel(MultipartFile file);

	//---------------

}