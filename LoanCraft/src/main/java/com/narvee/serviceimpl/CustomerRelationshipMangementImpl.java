package com.narvee.serviceimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.Service.CustomerRelationshipMangementService;
import com.narvee.entity.CustomerRelationshipMangement;
import com.narvee.repository.CustomerRelationshipMangementRepo;

@Service
public class CustomerRelationshipMangementImpl implements CustomerRelationshipMangementService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerRelationshipMangementImpl.class);
	
	@Autowired
	private CustomerRelationshipMangementRepo customerRelationshipMangementRepo;
	
	@Override
	public CustomerRelationshipMangement saveCrm(CustomerRelationshipMangement crm) {
		logger.info("!!! inside class : CustomerRelationshipMangementImpl,!! method: saveCrm()  ");
		return customerRelationshipMangementRepo.save(crm);
	}

	@Override
	public List<CustomerRelationshipMangement> getCrm() {
		logger.info("!!! inside class : CustomerRelationshipMangementImpl,!! method: getcrm()  ");
		return customerRelationshipMangementRepo.findAll();
	}

	@Override
	public boolean existsByEmail(String email) {
		
		return customerRelationshipMangementRepo.existsByEmailIgnoreCase(email);
	}

	@Override
	public boolean existsByMoblieNumber(String moblieNumber) {
		
		return customerRelationshipMangementRepo.existsByMoblieNumber(moblieNumber);
	}
	
	

}
