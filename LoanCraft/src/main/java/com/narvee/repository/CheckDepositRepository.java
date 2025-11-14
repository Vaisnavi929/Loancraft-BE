
package com.narvee.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.CheckDeposit;
import com.narvee.request.dto.CheckDepositResponseDTO;
import com.narvee.request.dto.RepaymentDropdown;

import jakarta.transaction.Transactional;

public interface CheckDepositRepository extends JpaRepository<CheckDeposit, Long> {


	@Query(value = "SELECT c.id,c.amount,c.posting_date,c.createddate,c.updateddate,CONCAT(ci.first_name, ' ', ci.last_name) AS full_name,a.application_reference_id as referenceId,c.status,c.cheque_number,c.reason,c.remarks,ld.disbursment_id,c.cheque_clearance_date,s.loan_account_number "

			+ " FROM lms_check_deposit c JOIN lms_disbursment ld\r\n"
			+ " on ld.disbursment_id=c.disbursment_id join lms_loan_saction s on s.loan_saction_id=ld.loan_saction_id join lms_applications a on a.loan_id=s.loan_id \r\n"
			+ " join lms_customers_info ci on ci.customer_id=a.customer_id ", nativeQuery = true)
	public Page<CheckDepositResponseDTO> getAllCheckDepositWithPagination(Pageable pageable);
	
  	@Modifying
	@Transactional
	@Query(value = "UPDATE lms_check_deposit SET status =:status,reason=:reason,updatedby=:userid,updateddate=:updateddate,cheque_return_charges=:charges WHERE id =:id", nativeQuery = true)
	int updateStatus(@Param("id") Long id, @Param("status") String status, String reason, Long userid, Double charges,
			LocalDateTime updateddate);

	@Modifying
	@Transactional
	@Query(value = "UPDATE lms_check_deposit SET status =:status,reason=:reason,updatedby=:userid,updateddate=:updateddate,cheque_return_charges=:charges ,cheque_clearance_date = :chequeClearanceDate   WHERE id =:id", nativeQuery = true)
	int updateStatus(@Param("id") Long id, @Param("status") String status, String reason, Long userid, Double charges,
			LocalDateTime updateddate, LocalDate chequeClearanceDate);

//	@Query(value = "SELECT c.id,c.amount,c.posting_date,c.createddate,c.updateddate,CONCAT(ci.first_name, ' ', ci.last_name) AS full_name,a.application_reference_id AS referenceId ,c.status,c.cheque_number,c.reason,c.remarks,ld.disbursment_id ,c.cheque_clearance_date "
//			+ "FROM lms_check_deposit c JOIN lms_disbursment ld on ld.disbursment_id=c.disbursment_id join lms_loan_saction s on s.loan_saction_id=ld.loan_saction_id join lms_applications a on a.loan_id=s.loan_id "
//			+ "join lms_customers_info ci on ci.customer_id=a.customer_id WHERE CAST(c.amount AS TEXT) ILIKE CONCAT('%',:keyword, '%') OR CAST(c.posting_date AS TEXT) ILIKE CONCAT('%', :keyword, '%') OR CAST(c.updateddate::date AS TEXT) ILIKE CONCAT('%', :keyword, '%')  OR "
//			+ "CONCAT(ci.first_name, ' ', ci.last_name) ILIKE  CONCAT('%', :keyword, '%') OR c.status ILIKE CONCAT('%',:keyword, '%') OR c.cheque_number ILIKE CONCAT('%',:keyword, '%') OR "
//			+ "c.reason ILIKE CONCAT('%',:keyword, '%') OR c.remarks ILIKE CONCAT('%',:keyword, '%') OR a.application_reference_id ILIKE CONCAT('%',:keyword, '%') OR CAST(c.cheque_clearance_date AS TEXT) ILIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
//	public Page<CheckDepositResponseDTO> getAllCheckDepositWithKeyword(Pageable pageable,
//			@Param("keyword") String keyword);

	@Query(value = """
		    SELECT 
		        c.id,
		        c.amount,
		        c.posting_date,
		        c.createddate,
		        c.updateddate,
		        CONCAT(ci.first_name, ' ', ci.last_name) AS full_name,
		        a.application_reference_id AS referenceId,
		        c.status,
		        c.cheque_number,
		        c.reason,
		        c.remarks,
		        ld.disbursment_id,
		        c.cheque_clearance_date
		    FROM lms_check_deposit c
		    JOIN lms_disbursment ld ON ld.disbursment_id = c.disbursment_id
		    JOIN lms_loan_saction s ON s.loan_saction_id = ld.loan_saction_id
		    JOIN lms_applications a ON a.loan_id = s.loan_id
		    JOIN lms_customers_info ci ON ci.customer_id = a.customer_id
		    WHERE 
		        CAST(c.amount AS TEXT) ILIKE CONCAT('%', :keyword, '%') OR
		        CAST(c.posting_date AS TEXT) ILIKE CONCAT('%', :keyword, '%') OR
		        CAST(c.updateddate AS TEXT) ILIKE CONCAT('%', :keyword, '%') OR
		        CONCAT(ci.first_name, ' ', ci.last_name) ILIKE CONCAT('%', :keyword, '%') OR
		        c.status ILIKE CONCAT('%', :keyword, '%') OR
		        c.cheque_number ILIKE CONCAT('%', :keyword, '%') OR
		        c.reason ILIKE CONCAT('%', :keyword, '%') OR
		        c.remarks ILIKE CONCAT('%', :keyword, '%') OR
		        a.application_reference_id ILIKE CONCAT('%', :keyword, '%') OR
		        CAST(c.cheque_clearance_date AS TEXT) ILIKE CONCAT('%', :keyword, '%')
		""",
		nativeQuery = true)
		Page<CheckDepositResponseDTO> getAllCheckDepositWithKeyword(
		    Pageable pageable,
		    @Param("keyword") String keyword
		);

	@Modifying
	@Transactional
	@Query(value = "UPDATE lms_check_deposit SET status =:value,Reason=:reason WHERE id =:id", nativeQuery = true)
	int updateStatus(@Param("id") Long id, @Param("value") String value, @Param("reason") String reason);

	public List<CheckDeposit> findByStatusAndPostingDate(String status, LocalDate postingDate);

	@Query(value = """
			SELECT r.repayment_id,
			     TO_CHAR(r.due_date, 'Month YYYY') AS due_month_year
			     FROM loancraft.lms_repayment r
			     JOIN loancraft.lms_disbursment ld ON r.disbursment_id = ld.disbursment_id
			     WHERE ld.disbursment_id =:disbursmentId
			     AND r.due_date <= CURRENT_DATE
			     ORDER BY r.due_date ASC
							""", nativeQuery = true)
	public List<RepaymentDropdown> getRepaymentMonthAndYearByDisbursmentId(@Param("disbursmentId") Long disbursmentId);

	@Query(value = """
			SELECT repayment_id, requested_amount,is_approved FROM lms_low_check_deposit_approval WHERE repayment_id=:repaymentId
					""", nativeQuery = true)
	public List<Object[]> getlowDepositRequests(@Param("repaymentId") Long repaymentId);

	boolean existsByLmsRepaymentRepaymentId(Long repaymentId);

	public List<CheckDeposit> LmsRepaymentRepaymentId(Long lmsRepayment);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE loancraft.lms_check_deposit "
			+ " SET status = :status "
			+ " WHERE posting_date = :postedDate"
			+ "  AND cheque_number = :chequeNum"
			+ "  AND amount = :amount "
			+ " ", nativeQuery = true)
	int updateStatusofRequestedCheque(@Param("postedDate") 
	
	//new added
	LocalDate postedDate , @Param("chequeNum") String chequeNum, @Param("amount") double amount,@Param("status") String status);
	   @Query(value = "UPDATE loancraft.lms_check_deposit "
	            + "SET status = 'posted'\r\n"
	            + "WHERE posting_date = :postedDate"
	            + "  AND cheque_number = :chequeNum"
	            + "  AND amount = :amount ", nativeQuery = true)
	    boolean updateStatusofRequestedCheque(@Param("postedDate") LocalDate postedDate, @Param("chequeNum") String chequeNum, @Param("amount") double amount);

}
