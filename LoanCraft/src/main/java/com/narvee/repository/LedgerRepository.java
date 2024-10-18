package com.narvee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narvee.entity.Ledger;

public interface LedgerRepository extends JpaRepository<Ledger, Long>{

}
