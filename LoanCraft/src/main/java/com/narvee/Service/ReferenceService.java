package com.narvee.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.narvee.entity.CustomerCallTracking;
import com.narvee.entity.CustomerRelationshipMangement;
import com.narvee.entity.ReferenceInfo;
import com.narvee.request.dto.ReferenceRequestDto;
import com.narvee.request.dto.RequestDTO;

public interface ReferenceService {

	public Object saveRi(ReferenceRequestDto referenceRequestDto);
	
	public ReferenceInfo updateRi(ReferenceInfo referenceInfo);
	
	public  Optional<ReferenceInfo>getByIdRi(Long referenceId);
	
	public List< ReferenceInfo>getAllRi();
	
	public Page<ReferenceInfo>paginationReferenceInfo(RequestDTO requestDTO);
}


//public Page<CustomerRelationshipMangement> findAllCrm(RequestDTO CrmRequestDto);