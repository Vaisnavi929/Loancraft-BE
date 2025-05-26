package com.narvee.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.LoanSanction;
import com.narvee.request.dto.LoanSanctionDTOS;

public interface LoanSanctionRepository extends JpaRepository<LoanSanction, Long> {

	@Query(value = "select c.first_name as firstname,  c.last_name as lastname, a.application_reference_id as referenceid, s.loan_saction_id as sanctionid,s.documentation_charges,s.loan_processing_charges, s.amount_sanctioned,s.number_of_emis,s.rate_of_interest,s.interest_type,s.repayment_mode,s.emi_amount,\r\n"

			+ "s.interest_type,s.payment_freqmuency,s.origination_fee,s.return_originationfee,s.repayment_mode,s.status,date(s.createddate) as createddate,s.updateddate as updateddate from lms_loan_saction s join lms_applications a ON a.loan_id=s.loan_id\r\n"
			+ "join lms_customers_info c ON c.customer_id=a.customer_id where s.status='sanctioned' ", nativeQuery = true)
	public Page<LoanSanctionDTOS> getAllLoansactionWithPagination(Pageable pageable);


	@Query(value = """
									SELECT
			    DATE(s.updateddate) as updateddate,
			    c.first_name as firstname, c.last_name as  lastname,
			    a.application_reference_id as referenceid,
			    s.loan_saction_id AS sanctionid,
			    s.documentation_charges,
			    s.loan_processing_charges,
			    DATE(s.createddate) as createddate,
			    s.amount_sanctioned,
			    s.number_of_emis,
			    s.rate_of_interest,
			    s.interest_type,
			    s.repayment_mode,
			    s.emi_amount,
			    s.payment_freqmuency,
			    s.status
			FROM
			    lms_loan_saction s
			JOIN
			    lms_applications a ON a.loan_id = s.loan_id
			JOIN
			    lms_customers_info c ON c.customer_id = a.customer_id
			WHERE
			    (s.status = 'sanctioned')
			    AND (
			        a.application_reference_id ILIKE '%' || :keyword || '%'
			        OR s.interest_type  ILIKE '%' || :keyword || '%'
			        OR c.first_name ILIKE '%' || :keyword || '%'
			        OR c.last_name ILIKE '%' || :keyword || '%'
			        OR c.mobile_number ILIKE '%' || :keyword || '%'
			        OR s.loan_saction_id::text ILIKE '%' || :keyword || '%'
			        OR s.amount_sanctioned::text ILIKE '%' || :keyword || '%'
			        OR s.number_of_emis::text ILIKE '%' || :keyword || '%'
			        OR s.rate_of_interest::text ILIKE '%' || :keyword || '%'
			        OR s.repayment_mode ILIKE '%' || :keyword || '%'
			        OR s.emi_amount::text ILIKE '%' || :keyword || '%'
			        OR s.payment_freqmuency ILIKE '%' || :keyword || '%'
			        OR s.status ILIKE '%' || :keyword || '%'
			        OR TO_CHAR(s.updateddate, 'YYYY-MM-DD') ILIKE '%' || :keyword || '%'
			        OR TO_CHAR(s.createddate, 'YYYY-MM-DD') ILIKE '%' || :keyword || '%'
			    )
						""", nativeQuery = true)
	public Page<LoanSanctionDTOS> getAllLoansactionWithSearching(Pageable pageable, @Param("keyword") String keyword);

}
