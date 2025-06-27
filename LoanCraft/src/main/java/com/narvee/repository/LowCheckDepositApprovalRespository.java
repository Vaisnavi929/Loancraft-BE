package com.narvee.repository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.LowCheckDepositApproval;
import com.narvee.request.dto.FetchLowCheckDepositDTO;

import jakarta.transaction.Transactional;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.LowCheckDepositApproval;
import com.narvee.request.dto.FetchLowCheckDepositDTO;

import jakarta.transaction.Transactional;

public interface LowCheckDepositApprovalRespository extends JpaRepository<LowCheckDepositApproval, Long> {



	@Modifying
	@Transactional
	@Query(value = "UPDATE lms_low_check_deposit_approval SET is_approved = :value, remarks = :remarks WHERE id = :id", nativeQuery = true)
	int updateIsApprovedAndRemarks(@Param("id") Long id, @Param("value") Boolean value,
			@Param("remarks") String remarks);

	@Query(value = "SELECT   cd.id,cd.recievable_amount,cd.requested_amount,cd.remarks,cd.createddate,cd.updateddate,u.name AS approved_by_name,ru.name AS requested_by_name,cd.is_approved,\r\n"
			+ "CONCAT(c.first_name, ' ', c.last_name) AS full_name,a.loan_id FROM lms_low_check_deposit_approval cd JOIN lms_user_info u ON u.user_id = cd.approved_by_id JOIN lms_repayment r ON r.repayment_id = cd.repayment_id \r\n"
			+ "JOIN lms_disbursment d ON d.disbursment_id = r.disbursment_id JOIN lms_loan_saction s ON s.loan_saction_id = d.loan_saction_id JOIN lms_applications a ON a.loan_id = s.loan_id \r\n"
			+ "JOIN lms_customers_info c ON c.customer_id = a.customer_id JOIN lms_user_info ru ON ru.user_id = cd.request_by_id WHERE cd.recievable_amount::TEXT ILIKE CONCAT('%', :keyword, '%') \r\n"
			+ "    OR cd.requested_amount::TEXT ILIKE CONCAT('%', :keyword, '%') \r\n"
			+ "    OR cd.remarks ILIKE CONCAT('%', :keyword, '%') \r\n"
			+ "    OR cd.createddate::TEXT ILIKE CONCAT('%', :keyword, '%') \r\n"
			+ "    OR cd.updateddate::TEXT ILIKE CONCAT('%', :keyword, '%') \r\n"
			+ "    OR u.name ILIKE CONCAT('%', :keyword, '%') \r\n"
			+ "    OR ru.name ILIKE CONCAT('%', :keyword, '%') \r\n"
			+ "    OR CAST(cd.is_approved AS TEXT) ILIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
	public Page<FetchLowCheckDepositDTO> fetchLowCheckDepositDetailsWithKeyword(Pageable pageable,
			@Param("keyword") String keyword);

}

	

	@Query(value = "SELECT cd.requested_amount,cd.receivable_amount,u.name AS approved_by_name,cd.id,cd.remarks,ru.name AS requested_by_name,cd.is_approved,CONCAT(c.first_name, ' ', c.last_name) AS full_name,a.loan_id \r\n"
			+ "FROM loancraft.lms_low_check_deposit_approval cd \r\n"
			+ "LEFT join loancraft.lms_user_info u on u.user_id=cd.approved_by_id \r\n"
			+ "join loancraft.lms_repayment r on r.repayment_id= cd.repayment_id \r\n"
			+ "join loancraft.lms_disbursment d on d.disbursment_id=r.disbursment_id \r\n"
			+ "join loancraft.lms_loan_saction s on s.loan_saction_id=d.loan_saction_id\r\n"
			+ "join loancraft.lms_applications a on a.loan_id=s.loan_id\r\n"
			+ "join loancraft.lms_customers_info c on c.customer_id=a.customer_id \r\n"
			+ "JOIN  loancraft.lms_user_info ru ON ru.user_id = cd.request_by_id", nativeQuery = true)
	public Page<FetchLowCheckDepositDTO> fetchLowCheckDepositDetails(Pageable pageable);

	@Modifying
	@Transactional
	@Query(value = "UPDATE lms_low_check_deposit_approval SET is_approved=:value WHERE id =:id", nativeQuery = true)
	int updateIsApproved(@Param("id") Long id, @Param("value") Boolean value);

	@Modifying
	@Transactional
	@Query(value = "UPDATE lms_low_check_deposit_approval SET remarks=:value WHERE id =:id", nativeQuery = true)
	int updateRemarks(@Param("id") Long id, @Param("value") String value);

	public LowCheckDepositApproval findByLmsRepaymentRepaymentId(Long repaymentId);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE lms_low_check_deposit_approval SET is_approved = :value, remarks = :remarks WHERE id = :id", nativeQuery = true)
	int updateIsApprovedAndRemarks(@Param("id") Long id, @Param("value") Boolean value, @Param("remarks") String remarks);

}

