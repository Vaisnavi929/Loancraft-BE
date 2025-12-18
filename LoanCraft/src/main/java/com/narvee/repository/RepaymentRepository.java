
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
			c.mobile_number, p.amount_status, p.due_amount, p.due_date, p.disbursment_id, p.interest, p.principal_amount,p.last_paid_date
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

	@Query(value = "SELECT "

			+ "p.repayment_id,CONCAT(c.first_name, ' ', c.last_name) AS full_name,p.amount_paid, c.mobile_number as mobilenumber,p.amount_status, ls.loan_account_number AS loanAccountNumber, p.due_amount,p.due_date ,la.application_reference_id AS application_reference_id  "

			+ "FROM lms_repayment p JOIN lms_disbursment d ON d.disbursment_id = p.disbursment_id JOIN lms_loan_saction ls ON d.loan_saction_id = ls.loan_saction_id \r\n"
			+ "JOIN lms_applications la ON ls.loan_id = la.loan_id JOIN lms_customers_info c ON c.customer_id = la.customer_id WHERE \r\n"
			+ "EXTRACT(YEAR FROM p.due_date) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM p.due_date) = EXTRACT(MONTH FROM CURRENT_DATE)", nativeQuery = true)
	public Page<PaymentStatementResponseDTO> getMonthlyDueList(Pageable pageable);

	@Query(value = """
			SELECT
			    p.repayment_id,
			    CONCAT(c.first_name, ' ', c.last_name) AS full_name,
			    p.amount_paid,
			    c.mobile_number AS mobilenumber,
			    p.amount_status,
			    ls.loan_account_number AS loanAccountNumber,
			    p.due_amount,
			    p.due_date,
			    la.application_reference_id AS application_reference_id
			FROM
			    lms_repayment p
			JOIN
			    lms_disbursment d ON d.disbursment_id = p.disbursment_id
			JOIN
			    lms_loan_saction ls ON d.loan_saction_id = ls.loan_saction_id
			JOIN
			    lms_applications la ON ls.loan_id = la.loan_id
			JOIN
			    lms_customers_info c ON c.customer_id = la.customer_id
			WHERE
			    EXTRACT(YEAR FROM p.due_date) = EXTRACT(YEAR FROM CURRENT_DATE)
			    AND EXTRACT(MONTH FROM p.due_date) = EXTRACT(MONTH FROM CURRENT_DATE)
			    AND (
			        p.amount_paid::text ILIKE CONCAT('%', :keyword, '%')
			        OR c.mobile_number ILIKE CONCAT('%', :keyword, '%')
			        OR CONCAT(c.first_name, ' ', c.last_name) ILIKE CONCAT('%', :keyword, '%')
			        OR ls.loan_account_number ILIKE CONCAT('%', :keyword, '%')
			        OR la.application_reference_id ILIKE CONCAT('%', :keyword, '%')
			        OR p.amount_status ILIKE CONCAT('%', :keyword, '%')
			        OR p.due_amount::text ILIKE CONCAT('%', :keyword, '%')
			        OR p.due_date::text ILIKE CONCAT('%', :keyword, '%')
			    )
			""", nativeQuery = true)
	Page<PaymentStatementResponseDTO> getMonthlyDueListWithKeyword(Pageable pageable, @Param("keyword") String keyword);

	@Modifying
	@Transactional
	@Query(value = "UPDATE lms_repayment\r\n" + "SET amount_status = CASE \r\n"
			+ "    WHEN due_date = CURRENT_DATE AND amount_status NOT IN ('Paid') THEN 'Unpaid'\r\n"
			+ "    WHEN due_date = CURRENT_DATE - INTERVAL '1 day' AND amount_status = 'Unpaid' THEN 'Due'\r\n"
			+ "    WHEN due_date = CURRENT_DATE - INTERVAL '3 days' AND amount_status IN ('Unpaid', 'Due') THEN 'Overdue'\r\n"
			+ "    ELSE amount_status\r\n" + "END", nativeQuery = true)
	public int updatePaymentStatus();

	/*
	 * @Query(value = """ SELECT rt.payment_date, rt.createddate, rt.description,
	 * rt.payment_mode, rt.amount_paid, rt.principal_component,
	 * rt.interest_component, rt.penalty_component, rt.other_charges FROM
	 * loancraft.lms_repayment_transactions rt JOIN loancraft.lms_repayment r ON
	 * r.repayment_id = rt.repayment_id JOIN loancraft.lms_disbursment d ON
	 * d.disbursment_id = r.disbursment_id WHERE d.disbursment_id = :disbursmentId
	 * ORDER BY COALESCE(rt.payment_date, rt.createddate) ASC """, nativeQuery =
	 * true) List<Object[]> getAllRepaymentTrasactionsBydisbursmentid(
	 * 
	 * @Param("disbursmentId") Long disbursmentId );
	 */

	@Query(value = """
						SELECT
			    rt.id,
			    rt.amount_paid,
			    TO_CHAR(rt.createddate, 'YYYY-MM-DD HH24:MI:SS') AS createddate,
			    rt.description,
			    rt.interest_component,
			    rt.other_charges,
			    TO_CHAR(rt.payment_date, 'YYYY-MM-DD HH24:MI:SS') AS payment_date,
			    rt.payment_mode,
			    rt.penalty_component,
			    rt.principal_component,
			    d.disbursment_id,
			    rt.repayment_id,
			    TO_CHAR(rt.updateddate, 'YYYY-MM-DD HH24:MI:SS') AS updateddate,
			    r.interest,
			    r.principal_amount
			FROM
			    loancraft.lms_repayment_transactions rt
			    RIGHT JOIN loancraft.lms_repayment r ON r.repayment_id = rt.repayment_id
			    JOIN loancraft.lms_disbursment d ON d.disbursment_id = r.disbursment_id
			WHERE
			    d.disbursment_id = :disbursmentId
			ORDER BY
			    rt.payment_date ASC;
						""", nativeQuery = true)
	public List<Object[]> getAllRepaymentTrasactionsBydisbursmentid(@Param("disbursmentId") Long disbursmentId);
	// added by vaishnavi

//in RepaymentRepository interface

//1. Basic repayment schedule rows (principal + interest + due_date)

	@Query(value = " SELECT r.repayment_id, r.due_date, r.principal_amount, r.interest, r.due_amount\r\n"
			+ " FROM loancraft.lms_repayment r\r\n" + " WHERE r.disbursment_id = :disbursmentId\r\n"
			+ " ORDER BY r.due_date ASC", nativeQuery = true)
	List<Object[]> getRepaymentsByDisbursmentId(@Param("disbursmentId") Long disbursmentId);

//2. Existing transactions query (you already have one named getAllRepaymentTrasactionsBydisbursmentid).
// I'm re-using that same method — it returns rt + r fields. Keep it as is.

//3. Disbursement info (amount + date)
	@Query(value = """
			SELECT d.disbursed_amount, d.date
			FROM loancraft.lms_disbursment d
			WHERE d.disbursment_id = :disbursmentId
			""", nativeQuery = true)
	List<Object[]> getDisbursementInfo(@Param("disbursmentId") Long disbursmentId);

//4. Check deposits (cheque return charges)

	@Query(value = "SELECT " + "cd.posting_date, " + "cd.cheque_return_charges, " + "cd.amount, " + "cd.reason, "
			+ "cd.repayment_id " + "FROM loancraft.lms_check_deposit cd " + "WHERE cd.disbursment_id = :disbursmentId "
			+ "AND cd.cheque_return_charges IS NOT NULL " + "AND cd.cheque_return_charges > 0", nativeQuery = true)
	List<Object[]> getCheckDepositsByDisbursmentId(@Param("disbursmentId") Long disbursmentId);

	/*
	 * @Query(value = """
	 * 
	 * SELECT CONCAT(c.first_name, ' ', c.last_name) AS full_name,
	 * a.application_reference_id, ca.address, ca.city, ca.location, ca.pincode,
	 * st.state, c.email, c.mobile_number, s.status, s.loan_account_number,
	 * d.disbursed_amount, s.amount_sanctioned, s.rate_of_interest, s.emi_amount,
	 * s.number_of_emis, s.documentation_charges, s.loan_processing_charges, d.date,
	 * r.full_name as reference_full_name, r.known_for_years, r.mobilenumber,
	 * r.relationship FROM loancraft.lms_disbursment d JOIN
	 * loancraft.lms_loan_saction s ON s.loan_saction_id = d.loan_saction_id JOIN
	 * loancraft.lms_applications a ON a.loan_id = s.loan_id JOIN
	 * loancraft.lms_customers_info c ON c.customer_id = a.customer_id JOIN
	 * loancraft.lms_current_address ca ON ca.customer_id = c.customer_id JOIN
	 * loancraft.states st ON st.state_id = ca.state_id JOIN loancraft.lms_reference
	 * r ON a.loan_id = r.loan_id WHERE d.disbursment_id = :disbursmentId """,
	 * nativeQuery = true) public List<Object[]>
	 * getMemeberDetails(@Param("disbursmentId") Long disbursmentId);
	 * 
	 * }
	 */ @Query(value = """
			    SELECT
			        CONCAT(c.first_name, ' ', c.last_name) AS full_name,
			        CONCAT(ca.address, ', ', ca.location, ', ', ca.city, ', ', st.state, ' - ', ca.pincode) AS full_address,
			        c.mobile_number,
			        s.status,
			        s.emi_start_date AS first_installment_date,
			        s.rate_of_interest AS interest_rate,
			        s.emi_amount AS monthly_emi,
			        s.loan_account_number,
			        a.loan_amount,
			        a.loan_tenure,
			        s.emi_end_date AS last_installment_date,
			        r.repayment_id AS emiNumber,
			        r.due_date AS dueDate,
			        (CURRENT_DATE - r.due_date) AS dueDays,
			        r.due_amount AS dueAmount,
			        r.other_charges AS chequeReturnCharge,
			        r.interest AS penalty,
			        TO_CHAR(r.due_date, 'Mon-YYYY') AS emiMonth,
			        0 AS latePenalty,
			        (r.due_amount + r.interest + r.other_charges) AS total,
			        a.company_name,
			        a.company_address,
			        ref.full_name AS ref_full_name,
			        ref.known_for_years AS ref_known_for_years,
			        ref.mobilenumber AS ref_mobileNumber,
			        ref.relationship AS ref_relationship
			    FROM loancraft.lms_disbursment d
			    JOIN loancraft.lms_loan_saction s ON s.loan_saction_id = d.loan_saction_id
			    JOIN loancraft.lms_applications a ON a.loan_id = s.loan_id
			    JOIN loancraft.lms_customers_info c ON c.customer_id = a.customer_id
			    JOIN loancraft.lms_current_address ca ON ca.customer_id = c.customer_id
			    JOIN loancraft.states st ON st.state_id = ca.state_id
			    JOIN loancraft.lms_repayment r ON r.disbursment_id = d.disbursment_id
			    LEFT JOIN loancraft.lms_reference ref ON ref.loan_id = a.loan_id
			    WHERE d.disbursment_id = :disbursmentId
			""", nativeQuery = true)
	List<Object[]> getMemeberDetails(@Param("disbursmentId") Long disbursmentId);
}
