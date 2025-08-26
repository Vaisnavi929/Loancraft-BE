package com.narvee.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;
import org.springframework.stereotype.Repository;

import com.narvee.entity.CustomerCallTracking;

@Repository
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
public interface CustomerCallTrackingRepo extends JpaRepository<CustomerCallTracking, Long> {
	@Query(value = "SELECT  ct.cct_id, ct.customer_name,  ct.agent_name,  "
			+ "    ct.feed_back, ct.loan_account_number,   ct.status, "
			+ "    cr.reference_id,   cr.mobile_number,  cr.reference_feed_back, "
			+ "    cr.reference_name,   cr.reference_status FROM lms_customer_call_tracking ct  "
			+ " LEFT JOIN lms_reference_info cr   ON ct.cct_id = cr.cct_id ", nativeQuery = true)
	Page<Object[]> getcctinfo(Pageable pageable);

	@Query(value = """
			SELECT ct.cct_id AS cctId,
			       ct.customer_name AS customerName,
			       ct.agent_name AS agentName,
			       ct.feed_back AS feedBack,
			       ct.loan_account_number AS loanAccountNumber,
			       ct.status AS status,
			       cr.reference_id AS referenceId,
			       cr.mobile_number AS mobileNumber,
			       cr.reference_feed_back AS referenceFeedBack,
			       cr.reference_name AS referenceName,
			       cr.reference_status AS referenceStatus
			FROM lms_customer_call_tracking ct
		LEFT JOIN lms_reference_info cr ON ct.cct_id = cr.cct_id
			""", countQuery = """
			SELECT COUNT(DISTINCT ct.cct_id)
			FROM lms_customer_call_tracking ct
			LEFT JOIN lms_reference_info cr ON ct.cct_id = cr.cct_id
			""", nativeQuery = true)
	Page<Object[]> findAllWithReferences(Pageable pageable);

	@Query(value = "SELECT ct.cct_id, ct.customer_name, ct.agent_name, "
            + "       ct.feed_back, ct.loan_account_number, ct.status, "
            + "       cr.reference_id, cr.mobile_number, cr.reference_feed_back, "
            + "       cr.reference_name, cr.reference_status "
            + "FROM lms_customer_call_tracking ct "
            + " LEFT JOIN lms_reference_info cr ON ct.cct_id = cr.cct_id "
            + "WHERE CAST(ct.cct_id AS TEXT) LIKE CONCAT('%', :keyword, '%') "
            + "   OR LOWER(ct.agent_name) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "   OR LOWER(ct.customer_name) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "   OR CAST(ct.date AS TEXT) LIKE CONCAT('%', :keyword, '%') "
            + "   OR LOWER(ct.feed_back) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "   OR CAST(ct.loan_account_number AS TEXT) LIKE CONCAT('%', :keyword, '%') "
            + "   OR LOWER(ct.status) LIKE LOWER(CONCAT('%', :keyword, '%'))",
       nativeQuery = true)
Page<Object[]> getAllCctWithSearching(@Param("keyword") String keyword, Pageable pageable);

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
