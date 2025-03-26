package com.narvee.repository;

import org.springframework.data.domain.Pageable;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.Disbursment;
import com.narvee.request.dto.DisbursmentIdDTO;
import com.narvee.request.dto.DisbursmentResponseDTO;

public interface DisbursmentRepository extends JpaRepository<Disbursment, Long>{
	
	
	@Query(value ="SELECT c.mobile_number as mobilenumber, d.disbursed_amount, d.disbursment_id,d.account_no,la.application_reference_id as referenceid,  d.branch, d.date, \r\n"
			+ "  d.createddate, d.isfc, d.remarks,c.first_name as firstname, d.updateddate "
			+ "FROM lms_disbursment d\r\n"
			+ "JOIN lms_loan_saction ls ON d.loan_saction_id= ls.loan_saction_id\r\n"
			+ "JOIN lms_applications la ON ls.loan_id = la.loan_id\r\n"
			+ "join lms_customers_info c ON c.customer_id=la.customer_id",nativeQuery =true)
	public Page<DisbursmentResponseDTO> getAllDisbursmentWithPagination(Pageable pageable);

	@Query(value="SELECT c.mobile_number as mobilenumber, d.disbursed_amount, d.disbursment_id,d.account_no,la.application_reference_id as referenceid,  d.branch, d.date, "
			+ "	d.createddate, d.isfc, d.remarks,c.first_name as firstname, d.updateddate "
			+ "FROM lms_disbursment d\r\n"
			+ "JOIN lms_loan_saction ls ON d.loan_saction_id= ls.loan_saction_id\r\n"
			+ "JOIN lms_applications la ON ls.loan_id = la.loan_id\r\n"
			+ "join lms_customers_info c ON c.customer_id=la.customer_id where d.account_no LIKE CONCAT('%',:keyword, '%') OR d.disbursed_amount LIKE CONCAT('%',:keyword, '%') OR\r\n"
			+ "d.branch LIKE CONCAT('%',:keyword, '%') OR d.date LIKE CONCAT('%',:keyword, '%') OR d.createddate LIKE CONCAT('%',:keyword, '%') OR d.isfc LIKE CONCAT('%',:keyword, '%')\r\n"
			+ "OR d.remarks LIKE CONCAT('%',:keyword, '%') OR la.application_reference_id LIKE CONCAT('%',:keyword, '%')  OR c.mobile_number LIKE CONCAT('%',:keyword, '%')  OR c.first_name LIKE CONCAT('%',:keyword, '%')",nativeQuery =true)
	public Page<DisbursmentResponseDTO> getAllDisbursmentWithkeyword(Pageable pageable,@Param("keyword")String keyword);
	
	
	@Query(value ="select d.disbursment_id,ls.loan_account_number,c.mobile_number,c.first_name from lms_disbursment d JOIN lms_loan_saction ls ON d.loan_saction_id= ls.loan_saction_id "
			+ "JOIN lms_applications la ON ls.loan_id = la.loan_id "
			+ "join lms_customers_info c ON c.customer_id=la.customer_id",nativeQuery =true)
	public List<DisbursmentIdDTO> getCustomerDetailsWithDisbursmentId();
	
}
