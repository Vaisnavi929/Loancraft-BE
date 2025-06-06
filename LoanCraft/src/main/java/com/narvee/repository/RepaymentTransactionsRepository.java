package com.narvee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narvee.entity.RepaymentTransactions;

public interface RepaymentTransactionsRepository extends JpaRepository<RepaymentTransactions, Long> {

}
