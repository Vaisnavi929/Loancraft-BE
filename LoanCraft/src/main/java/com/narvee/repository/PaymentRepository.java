package com.narvee.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.narvee.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	@Query(value = "SELECT payament_id,amount,loan_saction_id,payment_date,bank_details,branch_name,description,perticulars,vocher_number FROM lms_payment ", nativeQuery = true)
	Page<Payment> findAllPayment(Pageable pageable);

	@Query(value = "SELECT payament_id, amount, loan_saction_id, payment_date, bank_details, branch_name, description, perticulars, vocher_number "
			+ "FROM lms_payment WHERE payament_id LIKE CONCAT('%', :keyword, '%') OR "
			+ "amount LIKE CONCAT('%', :keyword, '%') OR " + "loan_saction_id LIKE CONCAT('%', :keyword, '%') OR "
			+ "payment_date LIKE CONCAT('%', :keyword, '%') OR " + "bank_details LIKE CONCAT('%', :keyword, '%') OR "
			+ "branch_name LIKE CONCAT('%', :keyword, '%') OR " + "description LIKE CONCAT('%', :keyword, '%') OR "
			+ "perticulars LIKE CONCAT('%', :keyword, '%') OR "
			+ "vocher_number LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
	Page<Payment> findPaymentByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
