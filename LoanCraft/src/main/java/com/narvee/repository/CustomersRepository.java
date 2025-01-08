package com.narvee.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.Customers;
import com.narvee.request.dto.SearchingDTO;

public interface CustomersRepository extends JpaRepository<Customers, Long> {

	Optional<Customers> findByEmail(String email);

	@Query(value = "SELECT email,first_name,last_name,mobile_no,dob,fathers_or_spouses_name,middle_name,highest_qualification,account_number,adhaar_number FROM lms_customers ", nativeQuery = true)
	public Page<SearchingDTO> findAllCustomers(Pageable pageable);

	@Query(value = "SELECT email, first_name, last_name, mobile_no, dob, fathers_or_spouses_name, middle_name, highest_qualification,adhaar_number,account_number"
			+ "FROM lms_customers " + "WHERE email LIKE CONCAT('%', :keyword, '%') OR "
			+ "first_name LIKE CONCAT('%', :keyword, '%') OR " + "last_name LIKE CONCAT('%', :keyword, '%') OR "
			+ "mobile_no LIKE CONCAT('%', :keyword, '%') OR "
			+ "highest_qualification LIKE CONCAT('%', :keyword, '%') OR " + "dob LIKE CONCAT('%', :keyword, '%') OR "
			+ "adhaar_number LIKE CONCAT('%', :keyword, '%') OR " + "account_number LIKE CONCAT('%', :keyword, '%') OR "
			+ "fathers_or_spouses_name LIKE CONCAT('%', :keyword, '%') OR "
			+ "middle_name LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
	public Page<SearchingDTO> findCustomersByKeyword(@Param("keyword") String keyword, Pageable pageable);

	@Query(value = "SELECT CASE  WHEN c.account_number IS NOT NULL AND c.adhaar_number IS NOT NULL "
			+ "AND c.dob IS NOT NULL " + " AND c.email IS NOT NULL " + "AND c.fathers_or_spouses_name IS NOT NULL "
			+ "AND c.first_name IS NOT NULL " + " AND c.gender IS NOT NULL "
			+ "AND c.highest_qualification IS NOT NULL " + "AND c.last_name IS NOT NULL "
			+ "AND c.middle_name IS NOT NULL " + "AND c.mobile_no IS NOT NULL " + "AND c.password IS NOT NULL "
			+ "AND p.address IS NOT NULL " + "AND p.city IS NOT NULL " + "AND p.state_id IS NOT NULL "
			+ "AND p.country IS NOT NULL " + "AND p.location IS NOT NULL " + "AND p.landmark IS NOT NULL "
			+ "AND p.pincode IS NOT NULL " + "AND p.stayed_of_years IS NOT NULL " + "AND ca.address IS NOT NULL "
			+ "AND ca.city IS NOT NULL " + "AND ca.state_id IS NOT NULL " + "AND ca.country IS NOT NULL "
			+ "AND ca.location IS NOT NULL " + "AND ca.landmark IS NOT NULL " + "AND ca.pincode IS NOT NULL "
			+ "AND ca.stayed_of_years IS NOT NULL " + "THEN 1 " + "ELSE 0 " + "END AS eligibility_status "
			+ "FROM lms_customers c " + "LEFT JOIN lms_permanent_adress p ON c.customer_id = p.customer_id "
			+ "LEFT JOIN lms_current_adress ca ON c.customer_id = ca.customer_id "
			+ "WHERE c.customer_id = :customerId", nativeQuery = true)
	Long checkEligibility(@Param("customerId") Long customerId);

}
