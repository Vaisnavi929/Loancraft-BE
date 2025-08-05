package com.narvee.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.narvee.entity.CustomerCallTracking;

@Repository
public interface CustomerCallTrackingRepo extends JpaRepository<CustomerCallTracking, Long> {

	@Query(value = "SELECT c_ctid, createddate,updateddate,end_time,feed_back,start_time,status FROM  loancraft.lms_customer_call_tracking"
			+ "", nativeQuery = true)

	Page<CustomerCallTracking> getcctinfo(Pageable pageable);

	@Query(value = "SELECT  c_ctid, createddate,updateddate,end_time,feed_back,start_time,status " + " FROM lms_crm n "

			+ " WHERE LOWER(createddate) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ " OR LOWER(updateddate) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ " OR LOWER(end_time) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ "OR LOWER(feed_back) LIKE LOWER(CONCAT('%', :keyword, '%'))"
			+ "OR LOWER(start_time) LIKE LOWER(CONCAT('%', :keyword, '%'))"
			+ "OR LOWER(status) LIKE LOWER(CONCAT('%', :keyword, '%'))", nativeQuery = true)

	Page<CustomerCallTracking> getAllCctWithSearching(String keyword, Pageable pageable);

	@Query(value = "SELECT c_ctid, createddate,updateddate,end_time,feed_back,start_time,status FROM  loancraft.lms_customer_call_tracking WHERE crm_id = :crmId"
			+ "", nativeQuery = true)
	Page<CustomerCallTracking> getAllCctByCrmId(Long crmId, Pageable pageable);

	@Query(value = """
		    SELECT * FROM lms_customer_call_tracking
		    WHERE crm_id = :crmId
		      AND (
		        CAST(end_time AS TEXT) ILIKE %:keyword% OR
		        LOWER(feed_back) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
		        CAST(start_time AS TEXT) ILIKE %:keyword% OR
		        LOWER(status) LIKE LOWER(CONCAT('%', :keyword, '%'))
		      )
		""", nativeQuery = true)
	Page<CustomerCallTracking> getAllCctByCrmIdWithSearching(String keyword, Pageable pageable, Long crmId);

}
