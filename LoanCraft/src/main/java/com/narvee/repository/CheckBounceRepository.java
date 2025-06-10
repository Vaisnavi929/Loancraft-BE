package com.narvee.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.CheckBounceEntry;
import com.narvee.request.dto.CheckBounceResponseDTO;


public interface CheckBounceRepository extends JpaRepository<CheckBounceEntry, Long> {
	
@Query(value ="SELECT c.amount,c.bounce_charges,c.bounce_date,c.status,c.cheque_number,c.reason,c.remarks FROM check_bounce_entry c",nativeQuery =true)
public Page<CheckBounceResponseDTO> getAllCheckBounceWithPagination(Pageable pageable);
	
@Query(value ="SELECT c.amount,c.bounce_charges,c.bounce_date,c.status,c.cheque_number,c.reason,c.remarks FROM check_bounce_entry c "
		+ "WHERE "
		+ "  CAST(c.amount AS TEXT) ILIKE CONCAT('%',:keyword, '%') OR "
		+ "  CAST(c.bounce_charges AS TEXT) ILIKE CONCAT('%', :keyword, '%') OR "
		+ "  CAST(c.bounce_date AS TEXT) ILIKE CONCAT('%', :keyword, '%') OR "
		+ "c.status ILIKE CONCAT('%','INSUFFICIENT_FUNDS', '%') OR"
		+ "  c.cheque_number ILIKE CONCAT('%',:keyword, '%') OR "
		+ "  c.reason ILIKE CONCAT('%',:keyword, '%') OR "
		+ "  c.remarks ILIKE CONCAT('%',:keyword, '%')" ,nativeQuery =true)
public Page<CheckBounceResponseDTO> getAllCheckBounceWithKeyword(Pageable pageable, @Param("keyword") String keyword);



}
