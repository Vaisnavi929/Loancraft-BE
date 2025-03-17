package com.narvee.repository;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.Disbursment;
import com.narvee.request.dto.DisbursmentResponseDTO;

public interface DisbursmentRepository extends JpaRepository<Disbursment, Long>{
	
	
	@Query(value ="select d.disbursment_id,d.account_no,d.disbursed_amount,d.branch,d.date,d.createddate,d.isfc,d.remarks,d.updateddate from lms_disbursment d ",nativeQuery =true)
	public Page<DisbursmentResponseDTO> getAllDisbursmentWithPagination(Pageable pageable);

	@Query(value="select d.disbursment_id,d.account_no,d.disbursed_amount,d.branch,d.date,d.createddate,d.isfc,d.remarks,d.updateddate from lms_disbursment d where d.account_no LIKE CONCAT('%',:keyword, '%') OR d.disbursed_amount LIKE CONCAT('%',:keyword, '%') OR\r\n"
			+ "d.branch LIKE CONCAT('%',:keyword, '%') OR d.date LIKE CONCAT('%',:keyword, '%') OR d.createddate LIKE CONCAT('%',:keyword, '%') OR d.isfc LIKE CONCAT('%',:keyword, '%')\r\n"
			+ "OR d.remarks LIKE CONCAT('%',:keyword, '%')",nativeQuery =true)
	public Page<DisbursmentResponseDTO> getAllDisbursmentWithkeyword(Pageable pageable,@Param("keyword")String keyword);
	
}
