package com.narvee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.narvee.entity.LoanApplication;
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
	
	@Query(value = "select max(amaxnum) as max from lms_loan_application", nativeQuery = true)
	public Long appmaxNumber();
	
	public boolean existsByAadharNumber(String aadharNumber);

}
