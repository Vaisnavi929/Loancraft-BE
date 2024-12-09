package com.narvee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narvee.entity.Applicants;

public interface ApplicantsRepository extends JpaRepository<Applicants, Long>{

	Optional<Applicants> findByEmail(String email);
	
}
