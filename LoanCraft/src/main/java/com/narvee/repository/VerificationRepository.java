package com.narvee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.Verification;
import com.narvee.request.dto.VerificationDTO;

import jakarta.transaction.Transactional;

public interface VerificationRepository extends JpaRepository<Verification, Long> {

	@Modifying
	@Transactional
	@Query(value = "UPDATE lms_verification  SET reviewed = :value WHERE verified_id = :verifiedId", nativeQuery = true)
	int updateReviewed(@Param("verifiedId") Long verifiedId, @Param("value") Boolean value);

	@Modifying
	@Transactional
	@Query(value = "UPDATE lms_verification  SET cibil = :value WHERE verified_id = :verifiedId", nativeQuery = true)
	int updateCibil(@Param("verifiedId") Long verifiedId, @Param("value") Boolean value);

	@Modifying
	@Transactional
	@Query(value = "UPDATE lms_verification  SET physical_location_verified = :value WHERE verified_id = :verifiedId", nativeQuery = true)
	int updatePhysicalLocationVerified(@Param("verifiedId") Long verifiedId, @Param("value") Boolean value);

	@Modifying
	@Transactional
	@Query(value = "UPDATE lms_verification  SET sanction = :value WHERE verified_id = :verifiedId", nativeQuery = true)
	int updateSanction(@Param("verifiedId") Long verifiedId, @Param("value") Boolean value);

	@Modifying
	@Transactional
	@Query(value = "UPDATE lms_verification  SET disbursed = :value WHERE verified_id = :verifiedId", nativeQuery = true)
	int updateDisbursed(@Param("verifiedId") Long verifiedId, @Param("value") Boolean value);
  
	@Query(value="SELECT verified_id,cibil,disbursed,physical_location_verified,reviewed,sanction FROM lms_verification WHERE loan_id =:loanId",nativeQuery = true)
	public VerificationDTO getAllVericationDetailsByLoanid(Long loanId);
  
	@Modifying
	@Query("UPDATE LoanDocument ld SET ld.documentVerified = :documentVerified WHERE ld.id = :id")
	void updateDocumentVerifiedById(@Param("documentVerified") Boolean documentVerified, @Param("id") Long id);
	
}
