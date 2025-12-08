package com.narvee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narvee.entity.OtpVerification;

public interface OtpRepository extends JpaRepository<OtpVerification, Long> {
	
    Optional<OtpVerification> findByOtp(String otp);
    //added
    
	Optional<OtpVerification> findByApplicantEmail(String email);
	// Get latest OTP for an email
    Optional<OtpVerification> findTopByApplicantEmailOrderByExpiryTimeDesc(String email);
    
  

}