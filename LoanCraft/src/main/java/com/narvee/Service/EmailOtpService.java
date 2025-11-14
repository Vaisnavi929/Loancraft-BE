package com.narvee.Service;

import com.narvee.request.dto.ApplicantDto;

import jakarta.mail.MessagingException;

public interface EmailOtpService {

	String sendOtp(ApplicantDto applicant) throws MessagingException;

	boolean verifyOtp(String otp);
	
	 public void sendLoanApplicationNotification( String applicationReferenceId, String status, String firstName, String lastName, String email);
	

	    //new added
//	    public void sendLoanAppicationNotification(String applicationReferenceId,String status, String firstName, String lastName, String email);
	    boolean verifyOtpForEmail(String email, String otp);

}
