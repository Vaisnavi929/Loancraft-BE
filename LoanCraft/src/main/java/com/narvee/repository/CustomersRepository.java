package com.narvee.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.Customers;
import com.narvee.request.dto.CustomerResponseDTO;

public interface CustomersRepository extends JpaRepository<Customers, Long> {

	Optional<Customers> findByEmail(String email);

	@Query(value = "SELECT email,account_number,first_name,last_name,mobile_no,highest_qualification FROM lms_customers ", nativeQuery = true)
	Page<CustomerResponseDTO> findAllApplicants(Pageable pageable);

	@Query(value = "SELECT email,account_number,first_name,last_name,mobile_no,highest_qualification FROM lms_customers where email LIKE CONCAT('%', :keyword, '%') OR " + "first_name LIKE CONCAT('%', :keyword, '%') OR "
			+ "last_name LIKE CONCAT('%', :keyword, '%') OR " + "mobile_no LIKE CONCAT('%', :keyword, '%') OR "
			+ "highest_qualification LIKE CONCAT('%', :keyword, '%') OR " + "account_number LIKE CONCAT('%', :keyword, '%') ", nativeQuery = true)
	Page<CustomerResponseDTO> findApplicantsByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
