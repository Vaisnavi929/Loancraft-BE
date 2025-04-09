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
			+ "s.interest_type,s.payment_freqmuency,s.origination_fee,s.return_originationfee,s.repayment_mode,s.status,date(s.createddate) as createddate,date(s.updateddate) as updateddate from lms_loan_saction s join lms_applications a ON a.loan_id=s.loan_id\r\n"
			+ "join lms_customers_info c ON c.customer_id=a.customer_id where s.status='sanctioned' ", nativeQuery = true)
	public Page<LoanSanctionDTOS> getAllLoansactionWithPagination(Pageable pageable);

	@Query(value = "select c.first_name , c.last_name, a.application_reference_id as referenceid, s.loan_saction_id as sanctionid,s.documentation_charges,s.loan_processing_charges, s.amount_sanctioned,s.number_of_emis,s.rate_of_interest,s.interest_type,s.repayment_mode,s.emi_amount,\r\n"
			+ "s.interest_type,s.payment_freqmuency, s.origination_fee,s.return_originationfee,s.repayment_mode,s.status,date(s.createddate) as createddate,date(s.updateddate) as updateddate  from lms_loan_saction s join lms_applications a ON a.loan_id=s.loan_id\r\n"
			+ "join lms_customers_info c ON c.customer_id=a.customer_id where   s.status='sanctioned' AND( s.amount_sanctioned LIKE CONCAT('%',:keyword, '%')  OR s.number_of_emis LIKE CONCAT('%',:keyword, '%') "
			+ " OR s.rate_of_interest LIKE CONCAT('%',:keyword, '%') OR s.interest_type LIKE CONCAT('%',:keyword, '%') OR c.first_name LIKE CONCAT('%',:keyword, '%') \r\n"
			+ "OR s.repayment_mode LIKE CONCAT('%',:keyword, '%') OR s.emi_amount LIKE CONCAT('%',:keyword, '%')  OR s.status LIKE CONCAT('%',:keyword, '%')"
			+ " OR date(s.createddate) LIKE CONCAT('%',:keyword, '%') OR c.last_name LIKE CONCAT('%',:keyword, '%') OR a.application_reference_id LIKE CONCAT('%',:keyword, '%')) ", nativeQuery = true)
	public Page<LoanSanctionDTOS> getAllLoansactionWithSearching(Pageable pageable, @Param("keyword") String keyword);

}
