package com.narvee.Service;

import jakarta.mail.MessagingException;

public interface EmailOtpService {

	String sendOtp(String email)throws MessagingException;

	boolean verifyOtp(String otp);
}
