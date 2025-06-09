package com.narvee.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.narvee.entity.CheckBounceEntry;
import com.narvee.request.dto.CheckBounceDTO;
import com.narvee.request.dto.CheckBounceResponseDTO;

public interface CheckBounceService {
	
	public CheckBounceEntry saveBounceEntry(CheckBounceDTO checkBounceDTO);

	public CheckBounceEntry updateBounceEntry(CheckBounceDTO checkBounceDTO);
	
	public Page<CheckBounceResponseDTO> getAllCheckBounce(SortingRequestDto sortingRequestDto);
}
