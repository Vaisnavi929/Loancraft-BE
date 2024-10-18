package com.narvee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narvee.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

}
