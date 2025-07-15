package com.narvee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narvee.entity.RepaymentTransactions;
import java.util.List;


public interface RepaymentTransactionsRepository extends JpaRepository<RepaymentTransactions, Long> {
	
	List<RepaymentTransactions> findByRepaymentRepaymentId(Long repaymentId);

}
