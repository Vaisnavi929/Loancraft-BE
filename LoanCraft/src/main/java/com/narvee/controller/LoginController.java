package com.narvee.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.entity.AuthRequest;

import com.narvee.request.dto.LoginRequest;
import com.narvee.serviceimpl.AuthenticationServiceImpl;

import com.narvee.repository.UserRepository;

import com.narvee.util.JwtUtil;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth")
public class LoginController {
	public static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationServiceImpl authenticationService;

	private UserRepository repository;

	@Autowired
	private AuthenticationManager authenticationManager;


	@PostMapping("/generateToken")
	public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		logger.info("!!! inside class : LoginController, !! method : authenticateAndGetToken");

		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
			if (authentication.isAuthenticated()) {
				String token = jwtUtil.generateToken(authRequest.getUsername());
				return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON)
						.body("Authentication failed!");
			}
		} catch (UsernameNotFoundException e) {
			logger.error("Username not found: {}", authRequest.getUsername());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON)
					.body("User with email " + authRequest.getUsername() + " not found");
		} catch (BadCredentialsException e) {
			logger.error("Invalid credentials provided for username: {}", authRequest.getUsername());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON)
					.body("Invalid credentials");
		} catch (Exception e) {
			logger.error("An error occurred: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
					.body("An error occurred: " + e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		try {
			// Authenticate user or applicant based on the provided details
			Object userOrApplicant = authenticationService.authenticate(request.getUsernameOrEmail(),
					request.getPassword());
			return ResponseEntity.ok(userOrApplicant); // Return authenticated details
		} catch (IllegalArgumentException e) {
			// Handle invalid userType error
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user type");
		} catch (BadCredentialsException e) {
			// Handle invalid credentials error
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username/email or password");
		} catch (Exception e) {
			// General error handling
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
		}
	}

}

