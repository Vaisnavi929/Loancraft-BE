package com.narvee.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.Customers;

public interface CustomersRepository extends JpaRepository<Customers, Long>{
	
	Optional<Customers> findByEmail(String email);
	
    @Query(value = "SELECT customer_id,email,first_name,last_name,mobile_no,password,account_number FROM applicant ", nativeQuery = true)
    Page<Customers> findAllApplicants(Pageable pageable);

	@Query(value = "SELECT customer_id,email,first_name,last_name,mobile_no,password,account_number FROM applicant"
			+ " WHERE customer_id LIKE CONCAT('%', :keyword, '%') OR "
			+ "email LIKE CONCAT('%', :keyword, '%') OR "
			+ "first_name LIKE CONCAT('%', :keyword, '%') OR " + "last_name LIKE CONCAT('%', :keyword, '%') OR "
			+ "mobile_no LIKE CONCAT('%', :keyword, '%') OR " + "password LIKE CONCAT('%', :keyword, '%') OR "
			+"account_number LIKE CONCAT('%', :keyword, '%')"
			, nativeQuery = true)
	Page<Customers> findApplicantsByKeyword(@Param("keyword") String keyword, Pageable pageable);


}
