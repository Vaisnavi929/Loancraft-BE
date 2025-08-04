package com.narvee.Service;

import java.util.List;

import com.narvee.entity.CustomerRelationshipMangement;

public interface CustomerRelationshipMangementService {

	public CustomerRelationshipMangement saveCrm(CustomerRelationshipMangement crm);

	public List<CustomerRelationshipMangement> getCrm();
	
	public boolean existsByEmail(String email);
	
	public boolean existsByMoblieNumber(String moblieNumber);
}
