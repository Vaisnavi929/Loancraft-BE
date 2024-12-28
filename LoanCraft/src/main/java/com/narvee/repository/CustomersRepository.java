package com.narvee.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.Customers;

public interface CustomersRepository extends JpaRepository<Customers, Long> {

	Optional<Customers> findByEmail(String email);

	@Query(value = "SELECT email,first_name,last_name,mobile_no,dob,fathers_or_spouses_name,middle_name,highest_qualification FROM applicant ", nativeQuery = true)
	Page<Customers> findAllApplicants(Pageable pageable);

	@Query(value = "SELECT email,first_name,last_name,mobile_no,dob,fathers_or_spouses_name,middle_name,highest_qualification FROM applicant"
			+ "email LIKE CONCAT('%', :keyword, '%') OR " + "first_name LIKE CONCAT('%', :keyword, '%') OR "
			+ "last_name LIKE CONCAT('%', :keyword, '%') OR " + "mobile_no LIKE CONCAT('%', :keyword, '%') OR "
			+ "highest_qualification LIKE CONCAT('%', :keyword, '%') OR " + "dob LIKE CONCAT('%', :keyword, '%') OR "
			+ "fathers_or_spouses_name LIKE CONCAT('%', :keyword, '%') OR "
			+ "middle_name LIKE CONCAT('%', :keyword, '%') OR ", nativeQuery = true)
	Page<Customers> findApplicantsByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
