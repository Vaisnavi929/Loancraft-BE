package com.narvee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.Document;
import com.narvee.request.dto.CustomerDocumentDto;

import jakarta.transaction.Transactional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
	
	@Modifying
    @Transactional
	@Query(value ="UPDATE lms_document  SET doc_verified = :value WHERE id = :id",nativeQuery = true)
	int updateReviewed(@Param("id") Long id, @Param("value") Boolean value);
	
	
	public List<Document> findByLoanApplicationLoanId(Long id);

	public List<Document> findByLoanApplicationLoanIdAndFileType(Long loanId, String fileType);
	
	@Query(value="SELECT ld.id,ld.file_name,ld.file_type,ld.loan_id,c.customer_id FROM lms_document ld join lms_applications la "
			+ "ON la.loan_id  = ld.loan_id join lms_customers_info c on c.customer_id=la.customer_id where c.customer_id=:customerId",nativeQuery = true)
	public List<CustomerDocumentDto> getAllDocumentsByCustomerId(@Param("customerId") Long customerId);
	
}




















