package com.narvee.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.narvee.entity.CustomerCallTracking;
import com.narvee.request.dto.RequestDTO;
import com.narvee.request.dto.callRequestDTO;
import com.narvee.vo.CctResponseDTO;

public interface CustomerCallTrackingService {

	public Object saveCct(callRequestDTO callRequestDTO);

	public Object updateCct(callRequestDTO callRequestDTO);

	public Optional<CustomerCallTracking> getById(Long cCtid);

	public List<CustomerCallTracking> getAll();

	public Page<CctResponseDTO> getAllCallTracking(RequestDTO requestDTO);

}
