package com.narvee.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.narvee.entity.LowCheckDepositApproval;
import com.narvee.request.dto.FetchLowCheckDepositDTO;
import com.narvee.request.dto.LowCheckDepositDTO;

public interface LowCheckDepositService {

	public LowCheckDepositApproval saveCheckDepositApproval(LowCheckDepositDTO LowCheckDepositDTO);

	public LowCheckDepositApproval updateCheckDepositApproval(LowCheckDepositDTO LowCheckDepositDTO);

	public LowCheckDepositApproval getById(Long id);

	public List<LowCheckDepositApproval> getAllLowCheckDepositApproval();

	public Page<FetchLowCheckDepositDTO> getLowCheckDepositDetails(SortingRequestDto sortingRequestDto);

	public String updateApprovalStatusAndRemarks(Long id, Boolean value, String remarks,Long ApprovedBy);
}
