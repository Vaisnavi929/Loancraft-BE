package com.narvee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.Document;

import jakarta.transaction.Transactional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
	
	@Modifying
    @Transactional
	@Query(value ="UPDATE lms_document  SET doc_verified = :value WHERE id = :id",nativeQuery = true)
	int updateReviewed(@Param("id") Long id, @Param("value") Boolean value);
	
	
	public List<Document> findByLoanApplicationLoanId(Long id);


}
