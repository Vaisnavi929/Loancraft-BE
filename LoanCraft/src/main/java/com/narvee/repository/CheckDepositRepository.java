package com.narvee.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.CheckDeposit;
import com.narvee.request.dto.CheckDepositResponseDTO;

import jakarta.transaction.Transactional;


public interface CheckDepositRepository extends JpaRepository<CheckDeposit, Long> {
	
@Query(value ="SELECT c.amount,c.bounce_charges,c.bounce_date,c.createddate,c.updateddate,c.status,c.cheque_number,c.reason,c.remarks,ld.disbursment_id  FROM check_bounce_entry c JOIN loancraft.lms_disbursment ld \r\n"
		+ "on ld.disbursment_id=c.disbursment_id",nativeQuery =true)
public Page<CheckDepositResponseDTO> getAllCheckDepositWithPagination(Pageable pageable);
	
@Query(value ="SELECT c.amount,c.bounce_charges,c.bounce_date,c.createddate,c.updateddate,c.status,c.cheque_number,c.reason,c.remarks,ld.disbursment_id  FROM check_bounce_entry c JOIN loancraft.lms_disbursment ld \r\n"
		+ "on ld.disbursment_id=c.disbursment_id "
		+ "WHERE "
		+ "  CAST(c.amount AS TEXT) ILIKE CONCAT('%',:keyword, '%') OR "
		+ "  CAST(c.bounce_charges AS TEXT) ILIKE CONCAT('%', :keyword, '%') OR "
		+ "  CAST(c.bounce_date AS TEXT) ILIKE CONCAT('%', :keyword, '%') OR "
		+ "c.status ILIKE CONCAT('%','INSUFFICIENT_FUNDS', '%') OR"
		+ "  c.cheque_number ILIKE CONCAT('%',:keyword, '%') OR "
		+ "  c.reason ILIKE CONCAT('%',:keyword, '%') OR "
		+ "  c.remarks ILIKE CONCAT('%',:keyword, '%')" ,nativeQuery =true)
public Page<CheckDepositResponseDTO> getAllCheckDepositWithKeyword(Pageable pageable, @Param("keyword") String keyword);

@Modifying
@Transactional
@Query(value ="UPDATE check_bounce_entry SET status =:value,Reason=:reason WHERE id =:id",nativeQuery = true)
int updateStatus(@Param("id") Long id ,@Param("value") String value,@Param("reason") String reason) ;


}
