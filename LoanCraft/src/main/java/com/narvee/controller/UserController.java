package com.narvee.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.narvee.Service.IUserInfoService;
import com.narvee.request.dto.UserInfoDto;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth")
public class UserController {
	
	public static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private IUserInfoService iUserInfoService;

	@PostMapping("/register")
	public ResponseEntity<UserInfoDto> registerUser(@RequestBody UserInfoDto userInfoDTO) {
		logger.info("!!! inside class : UserController, !! method : registerUser");
		UserInfoDto savedUser = iUserInfoService.saveUser(userInfoDTO);
		return ResponseEntity.ok(savedUser);
	}


	@GetMapping("/user/userProfile")
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<String> userProfile() {
		logger.info("!!! inside class : UserController, !! method : userProfile");
		return new ResponseEntity<String>("Welcome to User Profile", HttpStatus.OK);
	}


	@GetMapping("/admin/adminProfile")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<String> adminProfile() {
		logger.info("!!! inside class : UserController, !! method : adminProfile");
		return new ResponseEntity<String>("Welcome to Admin Profile", HttpStatus.OK);
	}

	@GetMapping("/employee/employeeProfile")
	@PreAuthorize("hasAuthority('ROLE_EMPLOYEE')")
	public ResponseEntity<String> employeeProfile() {
		logger.info("!!! inside class : UserController, !! method : employeeProfile");
		return new ResponseEntity<String>("Welcome to employee Profile", HttpStatus.OK);
	}

}