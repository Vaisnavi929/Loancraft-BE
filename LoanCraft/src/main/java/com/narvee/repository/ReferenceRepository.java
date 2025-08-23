 package com.narvee.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.narvee.entity.Reference;
import com.narvee.entity.ReferenceInfo;
import com.narvee.request.dto.ReferenceRequestDto;
import com.narvee.response.ReferenceResponseDTO;

@Repository
public interface ReferenceRepository extends JpaRepository<ReferenceInfo, Long> {

	@Query(value = " select" + "    reference_id," + "    mobile_number," + "    reference_feed_back,"
			+ "    reference_name," + "    reference_status," + "    cct_id ," + " updateddate ," + " createddate"
			+ " from  loancraft.lms_reference_info" + "", nativeQuery = true)
	Page<ReferenceInfo> getReferenceInfo(Pageable pageable);

	@Query(value = " SELECT \n" + "        reference_id,\n" + "        mobile_number,\n"
			+ "        reference_feed_back,\n" + "        reference_name,\n" + "        reference_status,\n"
			+ "        cct_id,\n" + "        updateddate,\n" + "        createddate\n"
			+ "    FROM loancraft.lms_reference_info n\n" + "    WHERE \n"
			+ "        CAST(updateddate AS TEXT) ILIKE CONCAT('%', :keyword, '%')\n"
			+ "        OR CAST(createddate AS TEXT) ILIKE CONCAT('%', :keyword, '%')\n"
			+ "        OR CAST(mobile_number AS TEXT) ILIKE CONCAT('%', :keyword, '%')\n"
			+ "        OR LOWER(reference_feed_back) LIKE LOWER(CONCAT('%', :keyword, '%'))\n"
			+ "        OR LOWER(reference_name) LIKE LOWER(CONCAT('%', :keyword, '%'))\n"
			+ "        OR LOWER(reference_status) LIKE LOWER(CONCAT('%', :keyword, '%'))\n"
			+ "        OR CAST(cct_id AS TEXT) ILIKE CONCAT('%', :keyword, '%')\n"
			+ "        OR CAST(reference_id AS TEXT) ILIKE CONCAT('%', :keyword, '%')\n", nativeQuery = true)
	Page<ReferenceInfo> getAllReferenceWithSearching(String keyword, Pageable pageable);

	@Query(value = "SELECT reference_id,updateddate,createddate,cct_id,mobile_number,reference_name,reference_feed_back,reference_status FROM  loancraft.lms_reference_info WHERE cct_id = :cctId "
			+ "", nativeQuery = true)
	Page<ReferenceResponseDTO> getAllCctBycctId(Long cctId, Pageable pageable);

	@Query(value = """
			SELECT * FROM loancraft.lms_reference_info
			WHERE cct_id = :cctId
			  AND (
			    CAST (updateddate AS TEXT) LIKE CONCAT('%',:keyword, '%')"
			 CAST (createddate AS TEXT) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			    LOWER (mobile_number ) ILIKE %:keyword% OR
			    LOWER (reference_name AS TEXT) ILIKE %:keyword% OR
			    LOWER (reference_feed_back) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
			    LOWER (reference_status) LIKE LOWER(CONCAT('%', :keyword, '%'))
			  )""", nativeQuery = true)
	Page<ReferenceResponseDTO> getAllRiBycctIdWithSearching(String keyword, Pageable pageable, Long cctId);                 

}
