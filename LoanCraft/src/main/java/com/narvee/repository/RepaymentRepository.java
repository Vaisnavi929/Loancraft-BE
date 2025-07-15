
package com.narvee.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.LmsRepayment;
import com.narvee.request.dto.PaymentResponseDTO;
import com.narvee.request.dto.PaymentStatementResponseDTO;

import jakarta.transaction.Transactional;

public interface RepaymentRepository extends JpaRepository<LmsRepayment, Long> {

	@Query(value = "SELECT p.disbursment_id, p.repayment_id,p.due_date, p.amount_paid,p.amount_status,p.due_amount FROM lms_repayment p", nativeQuery = true)
	Page<PaymentResponseDTO> getAllPaymentListWithPagination(Pageable pageable);

	@Query(value = "SELECT p.disbursment_id, p.repayment_id,p.due_date, p.amount_paid ,p.amount_status,p.due_amount FROM lms_repayment p where p.amount_paid  LIKE CONCAT('%',:keyword, '%') \r\n"
			+ "OR p.due_date LIKE CONCAT('%',:keyword, '%') OR p.due_amount  LIKE CONCAT('%',:keyword, '%') OR p.amount_status LIKE CONCAT('%',:keyword, '%') ", nativeQuery = true)
	Page<PaymentResponseDTO> getAllPaymentListWithKeyword(Pageable pageable, @Param("keyword") String keyword);

	@Query("SELECT p FROM LmsRepayment p WHERE p.disbursment.disbursmentId = :disbursmentId "
			+ "AND p.dueDate BETWEEN :startDate AND :endDate " + "AND p.amountStatus <> 'pending'")
	public List<LmsRepayment> findPaymentsByDisbursment(@Param("disbursmentId") Long disbursmentId,
			@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	@Query(value = """

				SELECT p.other_charges,p.repayment_id, p.amount_paid,CONCAT(c.first_name, ' ', c.last_name) AS full_name, la.application_reference_id,ls.amount_sanctioned,
			c.mobile_number, p.amount_status, p.due_amount, p.due_date, p.disbursment_id, p.interest, p.principal_amount
					FROM lms_repayment p
					JOIN lms_disbursment d ON d.disbursment_id = p.disbursment_id
					JOIN lms_loan_saction ls ON d.loan_saction_id = ls.loan_saction_id
					JOIN lms_applications la ON ls.loan_id = la.loan_id
					JOIN lms_customers_info c ON c.customer_id = la.customer_id
					WHERE p.disbursment_id = :disbursmentId
					ORDER BY p.due_date ASC
					""", nativeQuery = true)
	List<PaymentStatementResponseDTO> findByDisbursmentId(@Param("disbursmentId") Long disbursmentId);

	// comented by Laxman
//    @Query(value = "SELECT p.repayment_id,p.payment_type,p.amount_paid, c.mobile_number,c.first_name,ls.loan_account_number,p.amount_status,p.due_amount,p.due_date "
//  		+ "FROM lms_repayment p JOIN lms_disbursment d ON d.disbursment_id = p.disbursment_id JOIN lms_loan_saction ls ON d.loan_saction_id = ls.loan_saction_id "
//  		+ "JOIN lms_applications la ON ls.loan_id = la.loan_id JOIN lms_customers_info c ON c.customer_id = la.customer_id WHERE "
//  		+ "EXTRACT(YEAR FROM p.due_date) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM p.due_date) = EXTRACT(MONTH FROM CURRENT_DATE)",nativeQuery =true)
//  public List<PaymentStatementResponseDTO> getMonthlyDueList();

	@Query(value = "SELECT p.repayment_id,CONCAT(c.first_name, ' ', c.last_name) AS full_name,p.amount_paid, c.mobile_number as mobilenumber,p.amount_status,la.application_reference_id, ls.loan_account_number AS loanAccountNumber, p.due_amount,p.due_date\r\n"
			+ "FROM lms_repayment p JOIN lms_disbursment d ON d.disbursment_id = p.disbursment_id JOIN lms_loan_saction ls ON d.loan_saction_id = ls.loan_saction_id \r\n"
			+ "JOIN lms_applications la ON ls.loan_id = la.loan_id JOIN lms_customers_info c ON c.customer_id = la.customer_id WHERE \r\n"
			+ "EXTRACT(YEAR FROM p.due_date) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM p.due_date) = EXTRACT(MONTH FROM CURRENT_DATE)", nativeQuery = true)
	public Page<PaymentStatementResponseDTO> getMonthlyDueList(Pageable pageable);

	@Query(value = "SELECT p.repayment_id,CONCAT(c.first_name, ' ', c.last_name) AS full_name,p.amount_paid, c.mobile_number as mobilenumber,p.amount_status,la.application_reference_id, ls.loan_account_number AS loanAccountNumber, p.due_amount,p.due_date\r\n"
			+ "FROM lms_repayment p JOIN lms_disbursment d ON d.disbursment_id = p.disbursment_id JOIN lms_loan_saction ls ON d.loan_saction_id = ls.loan_saction_id \r\n"
			+ "JOIN lms_applications la ON ls.loan_id = la.loan_id JOIN lms_customers_info c ON c.customer_id = la.customer_id WHERE \r\n"
			+ "EXTRACT(YEAR FROM p.due_date) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM p.due_date) = EXTRACT(MONTH FROM CURRENT_DATE) AND \r\n"
			+ "(p.amount_paid::text ILIKE '%' || :keyword || '%'\r\n"
			+ " OR c.mobile_number ILIKE '%' || :keyword || '%'\r\n"
			+ " OR CONCAT(c.first_name, ' ', c.last_name) ILIKE '%' || :keyword || '%'\r\n"
			+ " OR ls.loan_account_number ILIKE '%' || :keyword || '%'\r\n"
			+ " OR p.amount_status ILIKE '%' || :keyword || '%'\r\n"
			+ " OR p.due_amount::text ILIKE '%' || :keyword || '%'\r\n"
			+ " OR p.due_date::text ILIKE '%' || :keyword || '%')", nativeQuery = true)
	public Page<PaymentStatementResponseDTO> getMonthlyDueListWithKeyword(Pageable pageable,
			@Param("keyword") String keyword);

	@Modifying
	@Transactional
	@Query(value = "UPDATE lms_repayment\r\n" + "SET amount_status = CASE \r\n"
			+ "    WHEN due_date = CURRENT_DATE AND amount_status NOT IN ('Paid') THEN 'Unpaid'\r\n"
			+ "    WHEN due_date = CURRENT_DATE - INTERVAL '1 day' AND amount_status = 'Unpaid' THEN 'Due'\r\n"
			+ "    WHEN due_date = CURRENT_DATE - INTERVAL '3 days' AND amount_status IN ('Unpaid', 'Due') THEN 'Overdue'\r\n"
			+ "    ELSE amount_status\r\n" + "END", nativeQuery = true)
	public int updatePaymentStatus();


	@Query(value = "SELECT rt.id,rt.amount_paid,TO_CHAR(rt.createddate, 'YYYY-MM-DD HH24:MI:SS') AS createddate,rt.description,rt.interest_component,rt.other_charges, TO_CHAR(rt.payment_date, 'YYYY-MM-DD HH24:MI:SS') AS payment_date,rt.payment_mode,rt.penalty_component,rt.principal_component,d.disbursment_id,\r\n"
			+ "rt.remaining_balance,rt.repayment_id,  TO_CHAR(rt.updateddate, 'YYYY-MM-DD HH24:MI:SS') AS updateddate,r.interest,r.principal_amount FROM lms_repayment_transactions rt  join lms_repayment r on r.repayment_id=rt.repayment_id join lms_disbursment d \r\n"
			+ "on d.disbursment_id =r.disbursment_id where d.disbursment_id=:disbursmentId", nativeQuery = true)
	public List<Object[]> getAllRepaymentTrasactionsBydisbursmentid(@Param("disbursmentId") Long disbursmentId);

	@Query(value = "SELECT CONCAT(c.first_name, ' ', c.last_name) AS full_name,a.application_reference_id,ca.address,ca.city,ca.location,ca.pincode,st.state,c.email,c.mobile_number,a.status,s.loan_account_number\r\n"
			+ "FROM lms_disbursment d join lms_loan_saction s on s.loan_saction_id = d.loan_saction_id join lms_applications a ON a.loan_id = s.loan_id\r\n"
			+ "join lms_customers_info c on c.customer_id=a.customer_id join lms_current_address ca on ca.customer_id = c.customer_id join states st \r\n"
			+ "ON st.state_id = ca.state_id where d.disbursment_id=:disbursmentId", nativeQuery = true)
	public List<Object[]> getMemeberReport(@Param("disbursmentId") Long disbursmentId);

}
