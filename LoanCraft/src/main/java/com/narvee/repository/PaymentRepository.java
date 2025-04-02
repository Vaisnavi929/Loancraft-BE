
package com.narvee.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.LmsPayment;
import com.narvee.request.dto.PaymentResponseDTO;
import com.narvee.request.dto.PaymentStatementResponseDTO;

public interface PaymentRepository extends JpaRepository<LmsPayment, Long> {

	@Query(value = "SELECT p.disbursment_id, p.payment_id,p.due_date, p.amount_paid,p.amount_status,p.due_amount FROM lms_payment p", nativeQuery = true)
	Page<PaymentResponseDTO> getAllPaymentListWithPagination(Pageable pageable);

	@Query(value = "SELECT p.disbursment_id, p.payment_id,p.due_date, p.amount_paid ,p.amount_status,p.due_amount FROM lms_payment p where p.amount_paid  LIKE CONCAT('%',:keyword, '%') \r\n"
			+ "OR p.due_date LIKE CONCAT('%',:keyword, '%') OR p.due_amount  LIKE CONCAT('%',:keyword, '%') OR p.amount_status LIKE CONCAT('%',:keyword, '%') ", nativeQuery = true)
	Page<PaymentResponseDTO> getAllPaymentListWithKeyword(Pageable pageable, @Param("keyword") String keyword);

	@Query("SELECT p FROM LmsPayment p WHERE p.disbursment.disbursmentId = :disbursmentId "
			+ "AND p.dueDate BETWEEN :startDate AND :endDate " + "AND p.amountStatus <> 'pending'")
	public List<LmsPayment> findPaymentsByDisbursment(@Param("disbursmentId") Long disbursmentId,
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	@Query(value = "SELECT p.payment_id, p.amount_paid, c.first_name, la.application_reference_id,\r\n"
			+ "			       c.mobile_number, p.amount_status, p.due_amount, p.due_date, p.disbursment_id\r\n"
			+ "			FROM lms_payment p\r\n"
			+ "			JOIN lms_disbursment d ON d.disbursment_id = p.disbursment_id\r\n"
			+ "			JOIN lms_loan_saction ls ON d.loan_saction_id = ls.loan_saction_id\r\n"
			+ "			JOIN lms_applications la ON ls.loan_id = la.loan_id\r\n"
			+ "			JOIN lms_customers_info c ON c.customer_id = la.customer_id\r\n"
			+ "			WHERE p.disbursment_id = :disbursmentId\r\n"
			+ "			ORDER BY p.due_date ASC", nativeQuery = true)
	List<PaymentStatementResponseDTO> findByDisbursmentId(@Param("disbursmentId") Long disbursmentId);
  
	//comented by Laxman
//    @Query(value = "SELECT p.payment_id,p.payment_type,p.amount_paid, c.mobile_number,c.first_name,ls.loan_account_number,p.amount_status,p.due_amount,p.due_date "
//  		+ "FROM lms_payment p JOIN lms_disbursment d ON d.disbursment_id = p.disbursment_id JOIN lms_loan_saction ls ON d.loan_saction_id = ls.loan_saction_id "
//  		+ "JOIN lms_applications la ON ls.loan_id = la.loan_id JOIN lms_customers_info c ON c.customer_id = la.customer_id WHERE "
//  		+ "EXTRACT(YEAR FROM p.due_date) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM p.due_date) = EXTRACT(MONTH FROM CURRENT_DATE)",nativeQuery =true)
//  public List<PaymentStatementResponseDTO> getMonthlyDueList();

	  @Query(value = "SELECT p.payment_id,c.first_name as firstname,p.payment_type,p.amount_paid, c.mobile_number as mobilenumber,p.amount_status, ls.loan_account_number AS loanAccountNumber,p.due_amount,p.due_date "
              + "FROM lms_payment p JOIN lms_disbursment d ON d.disbursment_id = p.disbursment_id JOIN lms_loan_saction ls ON d.loan_saction_id = ls.loan_saction_id "
              + "JOIN lms_applications la ON ls.loan_id = la.loan_id JOIN lms_customers_info c ON c.customer_id = la.customer_id WHERE "
              + " EXTRACT(YEAR FROM p.due_date) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM p.due_date) = EXTRACT(MONTH FROM CURRENT_DATE)",nativeQuery =true)
public Page<PaymentStatementResponseDTO> getMonthlyDueList(Pageable pageable);

@Query(value = "SELECT p.payment_id,c.first_name as firstname,p.payment_type,p.amount_paid, c.mobile_number as mobilenumber,p.amount_status, ls.loan_account_number AS loanAccountNumber, p.due_amount,p.due_date "
+ "FROM lms_payment p JOIN lms_disbursment d ON d.disbursment_id = p.disbursment_id JOIN lms_loan_saction ls ON d.loan_saction_id = ls.loan_saction_id "
+ "JOIN lms_applications la ON ls.loan_id = la.loan_id JOIN lms_customers_info c ON c.customer_id = la.customer_id WHERE "
+ " EXTRACT(YEAR FROM p.due_date) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM p.due_date) = EXTRACT(MONTH FROM CURRENT_DATE) AND "
+ "( p.payment_type LIKE CONCAT('%', :keyword, '%') OR p.amount_paid LIKE CONCAT('%', :keyword, '%')  OR c.mobile_number LIKE CONCAT('%', :keyword, '%') "
+ "OR c.first_name LIKE CONCAT('%', :keyword, '%') OR ls.loan_account_number LIKE CONCAT('%', :keyword, '%') OR p.amount_status LIKE CONCAT('%', :keyword, '%')"
+ "OR p.due_amount LIKE CONCAT('%', :keyword, '%') OR p.due_date  LIKE CONCAT('%', :keyword, '%'))",nativeQuery =true)
public Page<PaymentStatementResponseDTO> getMonthlyDueListWithKeyword(Pageable pageable,@Param("keyword")String keyword);

	
}

