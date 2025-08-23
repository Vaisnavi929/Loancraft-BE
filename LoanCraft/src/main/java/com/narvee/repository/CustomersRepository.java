package com.narvee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.CustomersInfo;
import com.narvee.request.dto.ApplicationResponseDto;
import com.narvee.request.dto.CustomerResponseDTO;
import com.narvee.request.dto.SearchingDTO;

public interface CustomersRepository extends JpaRepository<CustomersInfo, Long> {

	Optional<CustomersInfo> findByEmailOrMobileNumber(String email, String mobileNumber);

	@Query(value = "SELECT DISTINCT "
			+ "    lc.customer_id,\r\n"
			+ "    lc.email,\r\n"
			+ "    lc.account_number,\r\n"
			+ "    CONCAT(lc.first_name, ' ', lc.last_name) AS full_name,\r\n"
			+ "    lc.mobile_number,\r\n"
			+ "    lc.highest_qualification,\r\n"
			+ "    lc.updateddate, ls.status\r\n"
			+ " FROM loancraft.lms_customers_info lc\r\n"
			+ " LEFT JOIN loancraft.lms_applications la \r\n"
			+ "    ON lc.customer_id = la.customer_id\r\n"
			+ " LEFT JOIN loancraft.lms_loan_saction ls\r\n"
			+ "    ON la.loan_id = ls.loan_id\r\n"
			+ " WHERE ls.loan_id IS NULL", nativeQuery = true)
	Page<CustomerResponseDTO> findAllPreSalesApplicants(Pageable pageable);

	@Query(value = """
		    SELECT DISTINCT
		        lc.customer_id,
		        lc.email,
		        lc.account_number,
		        CONCAT(lc.first_name, ' ', lc.last_name) AS full_name,
		        lc.mobile_number,
		        lc.highest_qualification,
		        lc.updateddate,
		        ls.status
		    FROM loancraft.lms_customers_info lc
		 LEFT   JOIN loancraft.lms_applications la
		        ON lc.customer_id = la.customer_id
		  LEFT  JOIN loancraft.lms_loan_saction ls
		        ON la.loan_id = ls.loan_id
		   WHERE ls.loan_id IS NULL
		      AND (
		            lc.email ILIKE CONCAT('%', :keyword, '%')
		            OR lc.mobile_number ILIKE CONCAT('%', :keyword, '%')
		            OR lc.highest_qualification ILIKE CONCAT('%', :keyword, '%')
		            OR lc.account_number ILIKE CONCAT('%', :keyword, '%')
		            OR CONCAT(lc.first_name, ' ', lc.last_name) ILIKE CONCAT('%', :keyword, '%')
		          )
		    
		    """, nativeQuery = true)
	Page<CustomerResponseDTO> findPreSalesApplicantsByKeyword(@Param("keyword") String keyword, Pageable pageable);

	@Query(value = "SELECT email,first_name,last_name,mobile_number,dob,fathers_or_spouses_name,middle_name,highest_qualification,account_number,adhaar_number FROM lms_customers_info ", nativeQuery = true)
	Page<SearchingDTO> findAllCustomers(Pageable pageable);

	@Query(value = "SELECT email, first_name, last_name, mobile_number, dob, fathers_or_spouses_name, middle_name, highest_qualification,adhaar_number,account_number"
			+ "FROM lms_customers_info " + "WHERE email LIKE CONCAT('%', :keyword, '%') OR "
			+ "first_name LIKE CONCAT('%', :keyword, '%') OR " + "last_name LIKE CONCAT('%', :keyword, '%') OR "
			+ "mobile_number LIKE CONCAT('%', :keyword, '%') OR "
			+ "highest_qualification LIKE CONCAT('%', :keyword, '%') OR " + "dob LIKE CONCAT('%', :keyword, '%') OR "
			+ "adhaar_number LIKE CONCAT('%', :keyword, '%') OR " + "account_number LIKE CONCAT('%', :keyword, '%') OR "
			+ "fathers_or_spouses_name LIKE CONCAT('%', :keyword, '%') OR "
			+ "middle_name LIKE CONCAT('%', :keyword, '%')  ", nativeQuery = true)
	Page<SearchingDTO> findCustomersByKeyword(@Param("keyword") String keyword, Pageable pageable);

	@Query(value = "SELECT CASE  WHEN c.account_number IS NOT NULL AND c.adhaar_number IS NOT NULL "
			+ "AND c.dob IS NOT NULL " + " AND c.email IS NOT NULL " + "AND c.fathers_or_spouses_name IS NOT NULL "
			+ "AND c.first_name IS NOT NULL " + " AND c.gender IS NOT NULL "
			+ "AND c.highest_qualification IS NOT NULL " + "AND c.last_name IS NOT NULL "
			+ "AND c.mobile_number IS NOT NULL " + "AND c.password IS NOT NULL " + "AND p.address IS NOT NULL "
			+ "AND p.city IS NOT NULL " + "AND p.state_id IS NOT NULL " + "AND p.location IS NOT NULL "
			+ "AND p.landmark IS NOT NULL " + "AND p.pincode IS NOT NULL " + "AND p.stayed_of_years IS NOT NULL "
			+ "AND ca.address IS NOT NULL " + "AND ca.city IS NOT NULL " + "AND ca.state_id IS NOT NULL "
			+ "AND ca.location IS NOT NULL " + "AND ca.landmark IS NOT NULL " + "AND ca.pincode IS NOT NULL "
			+ "AND ca.stayed_of_years IS NOT NULL " + "THEN 1 " + "ELSE 0 " + "END AS eligibility_status "
			+ "FROM lms_customers_info c " + "LEFT JOIN lms_permanent_address p ON c.customer_id = p.customer_id "
			+ "LEFT JOIN lms_current_address ca ON c.customer_id = ca.customer_id "
			+ "WHERE c.customer_id = :customerId", nativeQuery = true)
	Long checkEligibility(@Param("customerId") Long customerId);

	public boolean existsByEmail(String email);

	@Query(value = "SELECT a.updateddate as updateddate,a.loan_id,a.is_requested_file_upload,a.is_file_upload,a.application_reference_id AS referenceId,  a.createddate, c.first_name, c.last_name,c.email,a.status,a.remarks,c.mobile_number,\n"
			+ "	a.repayment_mode,a.loan_amount FROM lms_applications a JOIN lms_customers_info c ON a.customer_id = c.customer_id WHERE c.customer_id = :customerId", nativeQuery = true)
	List<ApplicationResponseDto> findApplicationsByCustomerId(@Param("customerId") Long customerId);
  
@Query(value ="SELECT sum(c.customer_id) FROM lms_customers_info c ",nativeQuery =true)
	List<Object[]> findAllCustomersCountList();
	
	@Query(value = "SELECT DISTINCT lc.customer_id,lc.email,lc.account_number,  CONCAT(lc.first_name, ' ', lc.last_name) AS full_name,lc.mobile_number,lc.highest_qualification,lc.updateddate,ls.status\r\n"
			+ "FROM loancraft.lms_customers_info lc JOIN loancraft.lms_applications la ON lc.customer_id = la.customer_id JOIN loancraft.lms_loan_saction ls ON la.loan_id = ls.loan_id\r\n"
			+ "WHERE ls.status = 'disbursed' ", nativeQuery = true)
	Page<CustomerResponseDTO> findAllApplicants(Pageable pageable);
	
//	@Query(value = """
//			SELECT lc.customer_id,lc.email,lc.account_number,  CONCAT(lc.first_name, ' ', lc.last_name) AS full_name,lc.mobile_number,lc.highest_qualification,lc.updateddate,ls.status "
//			FROM loancraft.lms_customers_info lc JOIN loancraft.lms_applications la ON lc.customer_id = la.customer_id JOIN loancraft.lms_loan_saction ls ON la.loan_id = ls.loan_id "
//			WHERE ls.status = 'disbursed'
//		   AND
//		   (  lc.email ILIKE CONCAT('%', :keyword, '%') OR
//		   lc.mobile_number ILIKE CONCAT('%', :keyword, '%') OR
//		    lc.highest_qualification ILIKE CONCAT('%', :keyword, '%') OR
//		    lc.account_number ILIKE CONCAT('%', :keyword, '%') OR
//		    CONCAT(lc.first_name, ' ', lc.last_name) ILIKE CONCAT('%', :keyword, '%') )
//					""", nativeQuery = true)
//Page<CustomerResponseDTO> findApplicantsByKeyword(@Param("keyword") String keyword, Pageable pageable);
	
	@Query(value = """
		    SELECT DISTINCT
		        lc.customer_id,
		        lc.email,
		        lc.account_number,
		        CONCAT(lc.first_name, ' ', lc.last_name) AS full_name,
		        lc.mobile_number,
		        lc.highest_qualification,
		        lc.updateddate,
		        ls.status
		    FROM loancraft.lms_customers_info lc
		    JOIN loancraft.lms_applications la
		        ON lc.customer_id = la.customer_id
		    JOIN loancraft.lms_loan_saction ls
		        ON la.loan_id = ls.loan_id
		    WHERE ls.status = 'disbursed'
		      AND (
		            lc.email ILIKE CONCAT('%', :keyword, '%')
		            OR lc.mobile_number ILIKE CONCAT('%', :keyword, '%')
		            OR lc.highest_qualification ILIKE CONCAT('%', :keyword, '%')
		            OR lc.account_number ILIKE CONCAT('%', :keyword, '%')
		            OR CONCAT(lc.first_name, ' ', lc.last_name) ILIKE CONCAT('%', :keyword, '%')
		          )
		    
		    """, nativeQuery = true)
	Page<CustomerResponseDTO> findApplicantsByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
