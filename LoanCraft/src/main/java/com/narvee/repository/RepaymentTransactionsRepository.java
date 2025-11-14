package com.narvee.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.RepaymentTransactions;

public interface RepaymentTransactionsRepository extends JpaRepository<RepaymentTransactions, Long> {

	public List<RepaymentTransactions> findByRepaymentRepaymentId(Long repaymentId);

	@Query("SELECT rt FROM RepaymentTransactions rt " +
		       "WHERE rt.repayment.repaymentId = :repaymentId AND rt.paymentDate = " +
		       "(SELECT MAX(r.paymentDate) FROM RepaymentTransactions r WHERE r.repayment.repaymentId = :repaymentId)")
		RepaymentTransactions findByRepaymentIdAndMaxPaymentDate(@Param("repaymentId") Long repaymentId);



	@Query(value = "SELECT rt.id,rt.interest_component,rt.principal_component FROM lms_repayment_transactions rt", nativeQuery = true)
	public List<Object[]> findAlltransactionsIdAndInterestandprincipalList();

	@Query(value = "SELECT MAX(rt.payment_date) AS lastPaymentDate FROM lms_repayment_transactions rt\r\n"
			+ "JOIN lms_repayment r ON r.repayment_id = rt.repayment_id\r\n"
			+ "JOIN lms_disbursment d ON d.disbursment_id = r.disbursment_id\r\n"
			+ "WHERE d.disbursment_id = :disbursmentId ", nativeQuery = true)
	public Date findLastPaidDateByDisbursmentId(@Param("disbursmentId") Long disbursmentId);

}
