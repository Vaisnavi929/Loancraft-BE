package com.narvee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narvee.entity.SkippedCheckDeposit;

public interface SkippedCheckDepositRepository extends JpaRepository<SkippedCheckDeposit, Long> {
	
}
