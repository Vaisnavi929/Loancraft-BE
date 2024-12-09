package com.narvee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.EmailOtpService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.request.dto.ApplicantDto;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/otp")
@CrossOrigin
public class EmailOtpController {

	@Autowired
	private EmailOtpService emailOtpService;

	@RequestMapping(value = "/send-otp", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> sendEmail(@RequestBody ApplicantDto applicant) {
		try {
			emailOtpService.sendOtp(applicant);
			return new ResponseEntity<>(new RestAPIResponse("Success", "OTP has been sent to your email."),
					HttpStatus.OK);
		} catch (MessagingException e) {	
			// Handle error if there's an issue with sending the email
			return new ResponseEntity<>(new RestAPIResponse("Failed", "Something went wrong: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IllegalArgumentException e) {
			// Handle error if no applicant is found for the given email
			return new ResponseEntity<>(new RestAPIResponse("Failed", e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/verify-otp/{otp}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> verifyOtp(@PathVariable String otp) {
		try {
			boolean isValid = emailOtpService.verifyOtp(otp);
			if (isValid) {
				return new ResponseEntity<>(new RestAPIResponse("Success", "OTP is valid."), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new RestAPIResponse("Failed", "Invalid OTP."), HttpStatus.BAD_REQUEST);
			}
		} catch (IllegalArgumentException e) {
			// Handle error if OTP is invalid or expired
			return new ResponseEntity<>(new RestAPIResponse("Failed", e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

}
