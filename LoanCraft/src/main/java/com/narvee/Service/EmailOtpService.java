package com.narvee.Service;

import com.narvee.request.dto.ApplicantDto;

import jakarta.mail.MessagingException;

public interface EmailOtpService {

	String sendOtp(ApplicantDto applicant) throws MessagingException;

	boolean verifyOtp(String otp);
}
