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
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE lms_check_deposit SET status =:status,reason=:reason,updatedby=:userid,updateddate=:updateddate,cheque_return_charges=:charges WHERE id =:id", nativeQuery = true)
	int updateStatus(@Param("id") Long id, @Param("status") String status, String reason, Long userid,Double charges, LocalDateTime updateddate);

	@Query(value = "SELECT c.id,c.amount,c.posting_date,c.createddate,c.updateddate,c.status,c.cheque_number,c.reason,c.remarks,ld.disbursment_id  FROM lms_check_deposit c JOIN lms_disbursment ld "
			+ "on ld.disbursment_id=c.disbursment_id", nativeQuery = true)
	public Page<CheckDepositResponseDTO> getAllCheckDepositWithPagination(Pageable pageable);

	@Query(value = "SELECT c.id,c.amount,c.posting_date,c.createddate,c.updateddate,c.status,c.cheque_number,c.reason,c.remarks,ld.disbursment_id  FROM lms_check_deposit c JOIN lms_disbursment ld "
			+ "on ld.disbursment_id=c.disbursment_id " + "WHERE "
			+ "  CAST(c.amount AS TEXT) ILIKE CONCAT('%',:keyword, '%') OR "
			+ "  CAST(c.posting_date AS TEXT) ILIKE CONCAT('%', :keyword, '%') OR "
			+ "c.status ILIKE CONCAT('%','INSUFFICIENT_FUNDS', '%') OR"
			+ "  c.cheque_number ILIKE CONCAT('%',:keyword, '%') OR " + "  c.reason ILIKE CONCAT('%',:keyword, '%') OR "
			+ "  c.remarks ILIKE CONCAT('%',:keyword, '%')", nativeQuery = true)
	public Page<CheckDepositResponseDTO> getAllCheckDepositWithKeyword(Pageable pageable,
			@Param("keyword") String keyword);

	@Modifying
	@Transactional
	@Query(value = "UPDATE lms_check_deposit SET status =:value,Reason=:reason WHERE id =:id", nativeQuery = true)
	int updateStatus(@Param("id") Long id, @Param("value") String value, @Param("reason") String reason);

	public List<CheckDeposit> findByStatusAndPostingDate(String status, LocalDate postingDate);

	@Query(value = "SELECT r.repayment_id, TO_CHAR(r.due_date, 'FMMonth') || ' ' || TO_CHAR(r.due_date, 'YYYY') AS due_month_year "
			+ "FROM lms_repayment r " + "JOIN lms_disbursment ld ON r.disbursment_id = ld.disbursment_id "
			+ "WHERE ld.disbursment_id = :disbursmentId ORDER BY r.due_date ASC", nativeQuery = true)
	public List<RepaymentDropdown> getRepaymentMonthAndYearByDisbursmentId(@Param("disbursmentId") Long disbursmentId);

	boolean existsByLmsRepaymentRepaymentId(Long repaymentId);
	
	public CheckDeposit  LmsRepaymentRepaymentId(Long lmsRepayment);

}
