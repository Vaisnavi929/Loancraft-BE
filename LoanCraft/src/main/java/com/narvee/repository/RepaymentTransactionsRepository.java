package com.narvee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.narvee.entity.RepaymentTransactions;

public interface RepaymentTransactionsRepository extends JpaRepository<RepaymentTransactions, Long> {

	@Query(value = "SELECT rt.id,rt.interest_component,rt.principal_component FROM lms_repayment_transactions rt", nativeQuery = true)
	public List<Object[]> findAlltransactionsIdAndInterestandprincipalList();

}
