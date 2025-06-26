
package com.narvee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.LowCheckDepositApproval;
import com.narvee.request.dto.FetchLowCheckDepositDTO;

import jakarta.transaction.Transactional;

public interface LowCheckDepositApprovalRespository extends JpaRepository<LowCheckDepositApproval, Long> {

	@Query(value = "SELECT  cd.recievable_amount,u.name AS approved_by_name,cd.id,cd.remarks,ru.name AS requested_by_name,cd.is_approved,CONCAT(c.first_name, ' ', c.last_name) AS full_name,a.loan_id FROM lms_low_check_deposit_approval cd join \r\n"
			+ "lms_user_info u on u.user_id=cd.approved_by_id join lms_repayment r on r.repayment_id= cd.repayment_id join lms_disbursment d \r\n"
			+ "on d.disbursment_id=r.disbursment_id join lms_loan_saction s on s.loan_saction_id=d.loan_saction_id join lms_applications a on a.loan_id=s.loan_id \r\n"
			+ "join lms_customers_info c on c.customer_id=a.customer_id JOIN  lms_user_info ru ON ru.user_id = cd.request_by_id ", nativeQuery = true)
	public List<FetchLowCheckDepositDTO> fetchLowCheckDepositDetails();

	@Modifying
	@Transactional
	@Query(value = "UPDATE lms_low_check_deposit_approval SET is_approved = :value, remarks = :remarks WHERE id = :id", nativeQuery = true)
	int updateIsApprovedAndRemarks(@Param("id") Long id, @Param("value") Boolean value, @Param("remarks") String remarks);



}


package com.narvee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.LowCheckDepositApproval;
import com.narvee.request.dto.FetchLowCheckDepositDTO;

import jakarta.transaction.Transactional;

public interface LowCheckDepositApprovalRespository extends JpaRepository<LowCheckDepositApproval, Long> {

	@Query(value = "SELECT  cd.recievable_amount,u.name AS approved_by_name,cd.id,cd.remarks,ru.name AS requested_by_name,cd.is_approved,CONCAT(c.first_name, ' ', c.last_name) AS full_name,a.loan_id FROM lms_low_check_deposit_approval cd join \r\n"
			+ "lms_user_info u on u.user_id=cd.approved_by_id join lms_repayment r on r.repayment_id= cd.repayment_id join lms_disbursment d \r\n"
			+ "on d.disbursment_id=r.disbursment_id join lms_loan_saction s on s.loan_saction_id=d.loan_saction_id join lms_applications a on a.loan_id=s.loan_id \r\n"
			+ "join lms_customers_info c on c.customer_id=a.customer_id JOIN  lms_user_info ru ON ru.user_id = cd.request_by_id ", nativeQuery = true)
	public List<FetchLowCheckDepositDTO> fetchLowCheckDepositDetails();

	@Modifying
	@Transactional
	@Query(value = "UPDATE lms_low_check_deposit_approval SET is_approved=:value WHERE id =:id", nativeQuery = true)
	int updateIsApproved(@Param("id") Long id, @Param("value") Boolean value);

	@Modifying
	@Transactional
	@Query(value = "UPDATE lms_low_check_deposit_approval SET remarks=:value WHERE id =:id", nativeQuery = true)
	int updateRemarks(@Param("id") Long id, @Param("value") String value);

	public LowCheckDepositApproval findByLmsRepaymentRepaymentId(Long repaymentId);


