package com.narvee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.narvee.entity.CustomerRelationshipMangement;
@Repository
public interface CustomerRelationshipMangementRepo extends JpaRepository<CustomerRelationshipMangement, Long> {

	
	   boolean existsByMoblieNumber(String moblieNumber);
	   
	   boolean existsByEmailIgnoreCase(String email);
	   
	
}
