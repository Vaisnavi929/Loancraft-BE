package com.narvee.Service;



import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import com.narvee.entity.CustomerCallTracking;

import com.narvee.request.dto.RequestDTO;

public interface CustomerCallTrackingService {

	public CustomerCallTracking saveCctAll(CustomerCallTracking customerCallTracking);

	public List<CustomerCallTracking> getCctAll();
	
	//public Page<CustomerCallTracking> findAllCct (RequestDTO CctRequestDto);
	
	public CustomerCallTracking updateCct(CustomerCallTracking callTracking);
	
	public boolean deleteById(Long cCtid);
	
	public Page<CustomerCallTracking>getCctByCrmId(RequestDTO requestDTO);

}
