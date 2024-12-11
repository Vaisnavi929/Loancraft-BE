package com.narvee.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.Applicants;

public interface ApplicantsRepository extends JpaRepository<Applicants, Long>{

	Optional<Applicants> findByEmail(String email);
	
    @Query("SELECT a FROM Applicants a")
    Page<Applicants> findAllApplicants(Pageable pageable);

    @Query("SELECT a FROM Applicants a WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "LOWER(a.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.mobileNo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(a.password) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Applicants> findApplicantsByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
