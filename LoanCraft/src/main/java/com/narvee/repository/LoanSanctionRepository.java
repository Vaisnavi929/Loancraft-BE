package com.narvee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narvee.entity.LoanSanction;

public interface LoanSanctionRepository extends JpaRepository<LoanSanction, Long> {

}
