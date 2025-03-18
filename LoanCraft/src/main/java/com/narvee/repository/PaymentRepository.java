package com.narvee.repository;



import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.LmsPayment;
import com.narvee.request.dto.PaymentResponseDTO;

public interface PaymentRepository extends JpaRepository<LmsPayment, Long>{
	
	
	@Query(value ="SELECT p.disbursment_id, p.payment_id,p.due_date, p.amount_paid,p.amount_status,p.due_amount FROM lms_payment p",nativeQuery =true)
	Page<PaymentResponseDTO> getAllPaymentListWithPagination(Pageable pageable);
	
	@Query(value ="SELECT p.disbursment_id, p.payment_id,p.due_date, p.amount_paid ,p.amount_status,p.due_amount FROM lms_payment p where p.amount_paid  LIKE CONCAT('%',:keyword, '%') \r\n"
			+ "OR p.due_date LIKE CONCAT('%',:keyword, '%') OR p.due_amount  LIKE CONCAT('%',:keyword, '%') OR p.amount_status LIKE CONCAT('%',:keyword, '%') ",nativeQuery =true)
	Page<PaymentResponseDTO> getAllPaymentListWithKeyword(Pageable pageable,@Param("keyword")String keyword);
	
	   @Query("SELECT p FROM LmsPayment p WHERE p.disbursment.disbursmentId = :disbursmentId " +
	           "AND p.dueDate BETWEEN :startDate AND :endDate " +
	           "AND p.amountStatus <> 'pending'")
	 public List<LmsPayment> findPaymentsByDisbursment(@Param("disbursmentId") Long disbursmentId,@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

}
