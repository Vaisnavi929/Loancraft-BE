package com.narvee.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.narvee.entity.CheckDeposit;
import com.narvee.request.dto.CheckDepositDTO;
import com.narvee.request.dto.CheckDepositResponseDTO;
import com.narvee.request.dto.RepaymentDropdown;

public interface CheckDepositService {

	public CheckDeposit saveDeposit(CheckDepositDTO checkBounceDTO);

	public CheckDeposit updateDeposit(CheckDepositDTO checkBounceDTO);

	public Page<CheckDepositResponseDTO> getAllCheckDeposit(SortingRequestDto sortingRequestDto);

	public CheckDeposit getById(Long id);

	public String updateStatus(Long id, String status, String reason, Long userid);

	public void updatePostedChecksToInProgress();

	public List<RepaymentDropdown> getRepaymentMonthYear(Long disbursmentId);

	public boolean existsByLmsRepaymentRepaymentId(Long rid);

}
