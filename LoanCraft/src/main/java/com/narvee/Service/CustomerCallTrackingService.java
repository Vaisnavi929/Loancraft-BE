package com.narvee.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.narvee.entity.CustomerCallTracking;
import com.narvee.request.dto.CctResponsesDTO;
import com.narvee.request.dto.CrmRquestDto;
import com.narvee.request.dto.RequestDTO;

public interface CustomerCallTrackingService {

	public CustomerCallTracking saveCct(CustomerCallTracking callTracking);

	public CustomerCallTracking updateCct(CustomerCallTracking callTracking);

	public Optional<CustomerCallTracking> getById(Long cCtid);

	public List<CustomerCallTracking> getAll();

	public Page<CctResponsesDTO> getAllCallTracking(RequestDTO requestDTO);

}
