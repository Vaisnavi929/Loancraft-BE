package com.narvee.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.narvee.entity.CustomerCallTracking;
import com.narvee.request.dto.CctResponsesDTO;
import com.narvee.request.dto.CrmRquestDto;

@Repository
public interface CustomerCallTrackingRepo extends JpaRepository<CustomerCallTracking, Long> {

	@Query(value = "SELECT c_ctid, createddate,updateddate,end_time,feed_back,start_time,status,date FROM  loancraft.lms_customer_call_tracking"
			+ "", nativeQuery = true)
	Page<CctResponsesDTO> getcctinfo(Pageable pageable);

	@Query(value = "SELECT  c_ctid, createddate,updateddate,end_time,feed_back,start_time,status " + " FROM lms_crm n "
			+ " WHERE LOWER(createddate) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ " OR LOWER(updateddate) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ " OR LOWER(end_time) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ " OR LOWER(date) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ "OR LOWER(feed_back) LIKE LOWER(CONCAT('%', :keyword, '%'))"
			+ "OR LOWER(start_time) LIKE LOWER(CONCAT('%', :keyword, '%'))"
			+ "OR LOWER(duration) LIKE LOWER(CONCAT('%', :keyword, '%'))"
			+ "OR LOWER(status) LIKE LOWER(CONCAT('%', :keyword, '%'))", nativeQuery = true)
	Page<CctResponsesDTO> getAllCctWithSearching(String keyword, Pageable pageable);

	@Query(value = "SELECT crm_id,c_ctid,createddate,updateddate,end_time,feed_back,start_time,status,duration,date FROM  lms_customer_call_tracking WHERE crm_id = :crmId "
			+ "", nativeQuery = true)
	Page<CctResponsesDTO> getAllCctByCrmId(Long crmId, Pageable pageable);

	@Query(value = """
		    SELECT * FROM loancraft.lms_customer_call_tracking
		    WHERE crm_id = :crmId
		      AND (
		        CAST(start_time AS TEXT) ILIKE %:keyword% OR
		        CAST(end_time AS TEXT) ILIKE %:keyword% OR
		        CAST(duration AS TEXT) ILIKE %:keyword% OR
		        CAST(date AS TEXT) ILIKE %:keyword% OR
		        LOWER(feed_back) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
		        LOWER(status) LIKE LOWER(CONCAT('%', :keyword, '%'))
		      )""", nativeQuery = true)
	Page<CctResponsesDTO> getAllCctByCrmIdWithSearching(String keyword, Pageable pageable, Long crmId);

}
