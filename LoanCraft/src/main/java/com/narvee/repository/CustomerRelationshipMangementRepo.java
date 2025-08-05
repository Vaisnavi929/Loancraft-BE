package com.narvee.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.narvee.entity.CustomerRelationshipMangement;

@Repository
public interface CustomerRelationshipMangementRepo extends JpaRepository<CustomerRelationshipMangement, Long> {

	@Query(value = "SELECT crm_id, customer_name,\r\n"
			+ "email,moblie_number  FROM loancraft.lms_crm", nativeQuery = true)
	Page<CustomerRelationshipMangement> getcrminfo(Pageable pageable);

	@Query(value = "SELECT  crm_id, customer_name,email,moblie_number " + " FROM lms_crm n "

			+ " WHERE LOWER(customer_name) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ " OR LOWER(email) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ " OR LOWER(moblie_number) LIKE LOWER(CONCAT('%', :keyword, '%')) ", nativeQuery = true)
	
	         Page<CustomerRelationshipMangement> getAllCrmWithSearching(String keyword, Pageable pageable);

	boolean existsByMobileNumber(String moblieNumber);

	boolean existsByEmailIgnoreCase(String email);

	

}
