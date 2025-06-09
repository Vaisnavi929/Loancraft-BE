package com.narvee.Service;

import java.util.List;

import com.narvee.entity.CheckBounceEntry;
import com.narvee.request.dto.CheckBounceDTO;

public interface CheckBounceService {
	
	public CheckBounceEntry saveBounceEntry(CheckBounceDTO checkBounceDTO);

	public CheckBounceEntry updateBounceEntry(CheckBounceDTO checkBounceDTO);
	
	public List<CheckBounceEntry> getAllCheckBounce();
}
