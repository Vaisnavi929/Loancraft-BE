package com.narvee.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import com.narvee.entity.CustomerRelationshipMangement;
import com.narvee.request.dto.CrmRquestDto;
import com.narvee.request.dto.RequestDTO;


public interface CustomerRelationshipMangementService {

	public CustomerRelationshipMangement saveCrm(CustomerRelationshipMangement crm);

	public boolean existsByEmail(String email);
	
	public boolean existsByMoblieNumber(String moblieNumber);
	
	public Page<CustomerRelationshipMangement> findAllCrm (RequestDTO CrmRequestDto);
	
	public CustomerRelationshipMangement update(CustomerRelationshipMangement crm);
	
	public boolean deleteById(Long crmId);
	
	public Optional<CustomerRelationshipMangement> getById(Long crmId);
	
}
