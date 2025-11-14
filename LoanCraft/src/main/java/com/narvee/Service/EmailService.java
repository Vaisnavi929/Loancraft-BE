package com.narvee.Service;

public interface EmailService {
    void sendEmail(String toEmail, String subject, String body);

    void sendForgotPasswordEmail(String toEmail, String otp);

    static void sendOtpEmail(String toEmail, String otpCode) {
		// TODO Auto-generated method stub
		
	}

}



