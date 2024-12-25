package com.narvee.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.LoanSanction;
import com.narvee.request.dto.LoanApplicationDTOS;
import com.narvee.request.dto.LoanSanctionDTOS;

public interface LoanSanctionRepository extends JpaRepository<LoanSanction, Long> {
	
	
	@Query(value ="select loan_requested,amount_disbursed,cash_limit,emi_amount,number_of_emis,rate_of_interest,emi_due_date,emi_end_date,emi_start_date,currency,interest_type,repayment_mode,status\r\n"
			+ "currency,interest_type,payment_freqmuency,repayment_mode,status from lms_loan_saction ",nativeQuery = true)
	public Page<LoanSanctionDTOS> getAllLoansactionWithPagination(Pageable pageable);
	
	@Query(value = "select loan_requested,amount_disbursed,cash_limit,emi_amount,number_of_emis,rate_of_interest,emi_due_date,emi_end_date,emi_start_date,currency,interest_type,repayment_mode,status\r\n"
			+ "currency,interest_type,payment_freqmuency,repayment_mode,status from lms_loan_saction where loan_requested LIKE CONCAT('%',:keyword, '%') OR amount_disbursed LIKE CONCAT('%',:keyword, '%')\r\n"
			+ "OR cash_limit LIKE CONCAT('%',:keyword, '%') OR emi_amount LIKE CONCAT('%',:keyword, '%') OR number_of_emis LIKE CONCAT('%',:keyword, '%') OR rate_of_interest LIKE CONCAT('%',:keyword, '%') \r\n"
			+ "OR emi_due_date LIKE CONCAT('%',:keyword, '%') OR emi_end_date LIKE CONCAT('%',:keyword, '%') OR currency LIKE CONCAT('%',:keyword, '%') OR emi_start_date LIKE CONCAT('%',:keyword, '%')\r\n"
			+ "OR interest_type LIKE CONCAT('%',:keyword, '%') OR repayment_mode LIKE CONCAT('%',:keyword, '%')  OR status LIKE CONCAT('%',:keyword, '%')",nativeQuery =true)
	public Page<LoanSanctionDTOS> getAllLoansactionWithSearching(Pageable pageable,@Param("keyword")String keyword);

}
