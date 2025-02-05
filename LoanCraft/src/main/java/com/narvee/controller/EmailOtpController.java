package com.narvee.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.EmailOtpService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.entity.Customers;
import com.narvee.repository.CustomersRepository;
import com.narvee.request.dto.ApplicantDto;
import com.narvee.request.dto.CustomerDto;
import com.narvee.request.dto.CustomerEmailDTo;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/otp")
@CrossOrigin("*")
public class EmailOtpController {

	private static final Logger logger = LoggerFactory.getLogger(EmailOtpController.class);

	@Autowired
	private EmailOtpService emailOtpService;

	@Autowired
	private CustomersRepository customersRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping(value = "/send-otp", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> sendEmail(@RequestBody ApplicantDto applicant) {
		try {
			emailOtpService.sendOtp(applicant);
			logger.info("OTP sent successfully to email: {}", applicant.getEmail());
			return new ResponseEntity<>(new RestAPIResponse("Success", "OTP has been sent to your email."),
					HttpStatus.OK);
		} catch (MessagingException e) {
			logger.error("Failed to send OTP email: {}", e.getMessage());
			return new ResponseEntity<>(new RestAPIResponse("Failed", "Something went wrong: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IllegalArgumentException e) {
			logger.warn("Invalid request: {}", e.getMessage());
			return new ResponseEntity<>(new RestAPIResponse("Failed", e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/verify-otp/{otp}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> verifyOtp(@PathVariable String otp) {
		try {
			boolean isValid = emailOtpService.verifyOtp(otp);
			if (isValid) {
				logger.info("OTP verified successfully: {}", otp);
				return new ResponseEntity<>(new RestAPIResponse("Success", "OTP is valid."), HttpStatus.OK);
			} else {
				logger.warn("Invalid OTP: {}", otp);
				return new ResponseEntity<>(new RestAPIResponse("Failed", "Invalid OTP."), HttpStatus.BAD_REQUEST);
			}
		} catch (IllegalArgumentException e) {
			logger.error("OTP verification failed: {}", e.getMessage());
			return new ResponseEntity<>(new RestAPIResponse("Failed", e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/change_password", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> changePassword(@RequestBody ApplicantDto applicant)
			throws MessagingException {
		Optional<Customers> optionalApplicant = customersRepository.findByEmail(applicant.getEmail());

		if (!optionalApplicant.isPresent()) {
			return new ResponseEntity<>(new RestAPIResponse("mailerror", "Email not Registered"), HttpStatus.OK);
		} else {
			Customers existingApplicant = optionalApplicant.get();
			String encodedPassword = passwordEncoder.encode(applicant.getPassword());
			existingApplicant.setPassword(encodedPassword);

			// Save updated applicant
			customersRepository.save(existingApplicant);

			return new ResponseEntity<>(new RestAPIResponse("success", "Your Password has been changed"),
					HttpStatus.OK);
		}
	}
	@PostMapping("/notify")
    public ResponseEntity<String> notifyCustomer(@RequestBody CustomerEmailDTo customerEmailDTo) {
        try {
        	String applicationReferenceId = customerEmailDTo.getApplicationReferenceId();
            String status = customerEmailDTo.getStatus();
            CustomerDto customer = customerEmailDTo.getCustomer();

            emailOtpService.sendLoanApplicationNotification(applicationReferenceId, status, customer.getFirstName(),
                    customer.getLastName(),
                    customer.getEmail());

            return ResponseEntity.ok("Notification sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send notification: " + e.getMessage());
        }
    }
}

