package com.narvee.Service;

import java.util.List;

import com.narvee.entity.LowCheckDepositApproval;
import com.narvee.request.dto.FetchLowCheckDepositDTO;
import com.narvee.request.dto.LowCheckDepositDTO;

public interface LowCheckDepositService {

	public LowCheckDepositApproval saveCheckDepositApproval(LowCheckDepositDTO LowCheckDepositDTO);

	public LowCheckDepositApproval updateCheckDepositApproval(LowCheckDepositDTO LowCheckDepositDTO);

	public LowCheckDepositApproval getById(Long id);

	public List<LowCheckDepositApproval> getAllLowCheckDepositApproval();

	public List<FetchLowCheckDepositDTO> getLowCheckDepositDetails();

	public boolean updateIsApproved(Long id, Boolean value);

}
