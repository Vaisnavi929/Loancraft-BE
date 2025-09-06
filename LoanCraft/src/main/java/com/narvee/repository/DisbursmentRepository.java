package com.narvee.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.Disbursment;
import com.narvee.request.dto.DisbursmentIdDTO;
import com.narvee.request.dto.DisbursmentResponseDTO;

public interface DisbursmentRepository extends JpaRepository<Disbursment, Long> {

	@Query(value = """
				SELECT c.mobile_number AS mobilenumber,
			    d.disbursed_amount,
			    d.disbursment_id,
			    d.disb_ref_no,
			    d.account_no,
			    la.application_reference_id AS referenceid,
			    ls.loan_account_number  AS loan_account_number ,
			    la.is_requested_file_upload,la.is_file_upload,	
			    d.branch,
			    d.date,
			    d.createddate,
			    d.isfc,
			    d.remarks,
			    CONCAT(c.first_name, ' ', c.last_name) AS full_name,
			    d.updateddate as updateddate
			   
			FROM
			    lms_disbursment d
			JOIN
			  lms_loan_saction ls ON d.loan_saction_id = ls.loan_saction_id
			JOIN
			    lms_applications la ON ls.loan_id = la.loan_id
			JOIN
			    lms_customers_info c ON c.customer_id = la.customer_id """, nativeQuery = true)
	public Page<DisbursmentResponseDTO> getAllDisbursmentWithPagination(Pageable pageable);

	@Query(value = """
				SELECT c.mobile_number AS mobilenumber,
			    d.disbursed_amount,
			    d.disbursment_id,
			    d.disb_ref_no,
			    d.account_no,
			    la.application_reference_id AS referenceid,
			    ls.loan_account_number  AS loan_account_number ,
			    la.is_requested_file_upload,la.is_file_upload,	
			    d.branch,
			    d.date,
			    d.createddate,
			    d.isfc,
			    d.remarks,
			    CONCAT(c.first_name, ' ', c.last_name) AS full_name,
			    d.updateddate as updateddate
			    
			FROM
			    lms_disbursment d
			JOIN
			  lms_loan_saction ls ON d.loan_saction_id = ls.loan_saction_id
			JOIN
			    lms_applications la ON ls.loan_id = la.loan_id
			JOIN
			    lms_customers_info c ON c.customer_id = la.customer_id
				WHERE
				    d.account_no ILIKE '%' || :keyword || '%'
				    OR d.disb_ref_no ILIKE '%' || :keyword || '%'
				    OR d.disbursed_amount::text ILIKE '%' || :keyword || '%'
				    OR d.branch ILIKE '%' || :keyword || '%'
				    OR TO_CHAR(d.date, 'YYYY-MM-DD') ILIKE '%' || :keyword || '%'
				    OR TO_CHAR(d.createddate, 'YYYY-MM-DD') ILIKE '%' || :keyword || '%'
				    OR d.isfc ILIKE '%' || :keyword || '%'
				    OR d.remarks ILIKE '%' || :keyword || '%'
				    OR la.application_reference_id ILIKE '%' || :keyword || '%'
				    OR ls.loan_account_number ILIKE '%' || :keyword || '%'
				    OR CONCAT(c.first_name, ' ', c.last_name) ILIKE '%' || :keyword || '%'

							""", nativeQuery = true)
	public Page<DisbursmentResponseDTO> getAllDisbursmentWithkeyword(Pageable pageable,
			@Param("keyword") String keyword);

	@Query(value = "select d.disbursment_id,ls.loan_account_number,c.mobile_number, CONCAT(c.first_name, ' ', c.last_name) AS full_name from lms_disbursment d JOIN lms_loan_saction ls ON d.loan_saction_id= ls.loan_saction_id "
			+ "JOIN lms_applications la ON ls.loan_id = la.loan_id "
			+ "join lms_customers_info c ON c.customer_id=la.customer_id", nativeQuery = true)
	public List<DisbursmentIdDTO> getCustomerDetailsWithDisbursmentId();

	@Query(value = "select max(amaxnum) as max from lms_disbursment", nativeQuery = true)
	public Long disbmaxNumber();

	@Query(value ="SELECT d.disbursment_id,d.disbursed_amount FROM lms_disbursment d",nativeQuery =true)
	List<Object[]> findAllDisbursedIdAndAmounts();
}
