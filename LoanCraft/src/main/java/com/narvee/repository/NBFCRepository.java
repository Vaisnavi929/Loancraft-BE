package com.narvee.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.NBFC;

public interface NBFCRepository extends JpaRepository<NBFC, Long> {

	boolean existsByEmailIgnoreCase(String email);

	boolean existsByNbfcNameIgnoreCase(String nbfcName);
	
	boolean existsByCorporateIdentificationNoIgnoreCase(String Number);

	@Query(value = "SELECT id,nbfc_name,regional_office,classification,corporate_identification_no,layer,address, email, updateddate , products , partners , type_of_organization   FROM  lms_nbfc", nativeQuery = true)
	public Page<Object[]> getAllNBFC(Pageable pageable);

	@Query(value = """
			SELECT id,nbfc_name,regional_office,classification,corporate_identification_no,layer,address, email, updateddate, products, partners, type_of_organization FROM  lms_nbfc
			WHERE nbfc_name ILIKE CONCAT('%', :keyword, '%')
			   OR regional_office ILIKE CONCAT('%', :keyword, '%')
			   OR classification ILIKE CONCAT('%', :keyword, '%')
			   OR corporate_identification_no ILIKE CONCAT('%', :keyword, '%')
			   OR layer ILIKE CONCAT('%', :keyword, '%')
			   OR address ILIKE CONCAT('%', :keyword, '%')
			   OR email ILIKE CONCAT('%', :keyword, '%')
			   OR products ILIKE CONCAT('%', :keyword, '%')
			   OR partners ILIKE CONCAT('%', :keyword, '%')
			   OR type_of_organization ILIKE CONCAT('%', :keyword, '%')
			   OR CAST(updateddate::date AS TEXT) ILIKE CONCAT('%', :keyword, '%')
			""", nativeQuery = true)
	Page<Object[]> searchNBFC(@Param("keyword") String keyword, Pageable pageable);

}
