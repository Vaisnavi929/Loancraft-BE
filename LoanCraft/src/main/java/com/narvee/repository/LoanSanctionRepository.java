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
	
	
	@Query(value ="select amount_sanctioned,number_of_emis,rate_of_interest,interest_type,repayment_mode,emi_amount,status\r\n"
			+ "currency,interest_type,payment_freqmuency,repayment_mode,status from lms_loan_saction ",nativeQuery = true)
	public Page<LoanSanctionDTOS> getAllLoansactionWithPagination(Pageable pageable);
	
	@Query(value = "select amount_sanctioned,number_of_emis,rate_of_interest,interest_type,repayment_mode,emi_amount,status\r\n"
			+ "from lms_loan_saction where amount_sanctioned LIKE CONCAT('%',:keyword, '%')  OR number_of_emis LIKE CONCAT('%',:keyword, '%') "
			+ " OR rate_of_interest LIKE CONCAT('%',:keyword, '%') OR interest_type LIKE CONCAT('%',:keyword, '%') \r\n"
			+ "OR repayment_mode LIKE CONCAT('%',:keyword, '%') OR emi_amount LIKE CONCAT('%',:keyword, '%')  OR status LIKE CONCAT('%',:keyword, '%')",nativeQuery =true)
	public Page<LoanSanctionDTOS> getAllLoansactionWithSearching(Pageable pageable,@Param("keyword")String keyword);

}
