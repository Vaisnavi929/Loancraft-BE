package com.narvee.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.narvee.entity.CustomerCallTracking;
import com.narvee.request.dto.CctResponsesDTO;


@Repository
public interface CustomerCallTrackingRepo extends JpaRepository<CustomerCallTracking, Long> {

	@Query(value = "SELECT cct_id,agent_name,customer_name,loan_account_number,date,feed_back,status\r\n"
			+ "from  loancraft.lms_customer_call_tracking"
			+ "", nativeQuery = true)
	Page<CctResponsesDTO> getcctinfo(Pageable pageable);

	@Query(value = "SELECT  cct_id,agent_name,customer_name,loan_account_number,date,feed_back,status\r\n"
			+ " from  loancraft.lms_customer_call_tracking n "
			+ "WHERE CAST(cct_id AS TEXT) LIKE CONCAT('%', :keyword, '%')\r\n"
			+ "   OR LOWER(agent_name) LIKE LOWER(CONCAT('%', :keyword, '%'))\r\n"
			+ "   OR LOWER(customer_name) LIKE LOWER(CONCAT('%', :keyword, '%'))\r\n"
			+ "   OR CAST(date AS TEXT) LIKE CONCAT('%', :keyword, '%')\r\n"
			+ "   OR LOWER(feed_back) LIKE LOWER(CONCAT('%', :keyword, '%'))\r\n"
			+ "   OR CAST(loan_account_number AS TEXT) LIKE CONCAT('%', :keyword, '%')\r\n"
			+ "   OR LOWER(status) LIKE LOWER(CONCAT('%', :keyword, '%'))", nativeQuery = true)
	Page<CctResponsesDTO> getAllCctWithSearching(String keyword, Pageable pageable);

//	@Query(value = "SELECT crm_id,c_ctid,createddate,updateddate,end_time,feed_back,start_time,status,duration,date FROM  lms_customer_call_tracking WHERE crm_id = :crmId "
//			+ "", nativeQuery = true)
//	Page<CctResponsesDTO> getAllCctByCrmId(Long crmId, Pageable pageable);
//
//	@Query(value = """
//		    SELECT * FROM loancraft.lms_customer_call_tracking
//		    WHERE crm_id = :crmId
//		      AND (
//		        CAST(start_time AS TEXT) ILIKE %:keyword% OR
//		        CAST(end_time AS TEXT) ILIKE %:keyword% OR
//		        CAST(duration AS TEXT) ILIKE %:keyword% OR
//		        CAST(date AS TEXT) ILIKE %:keyword% OR
//		        LOWER(feed_back) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
//		        LOWER(status) LIKE LOWER(CONCAT('%', :keyword, '%'))
//		      )""", nativeQuery = true)
//	Page<CctResponsesDTO> getAllCctByCrmIdWithSearching(String keyword, Pageable pageable, Long crmId);

}
