package com.narvee.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.narvee.Service.IUserInfoService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.request.dto.SortingDTO;
import com.narvee.request.dto.UserInfoDto;
import com.narvee.request.dto.UserUpdateDto;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
public class UserController {

	public static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private IUserInfoService iUserInfoService;

	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> registerUser(@RequestBody UserInfoDto userInfoDTO) {
		logger.info("Registering user with email in UserConttoller: {}", userInfoDTO.getEmail());
		try {
			UserInfoDto savedUser = iUserInfoService.saveUser(userInfoDTO);
			logger.info("User registered successfully: {}", userInfoDTO.getEmail());
			return ResponseEntity.ok(new RestAPIResponse("success", "User registered successfully", savedUser));
		} catch (Exception e) {
			logger.error("Error registering user: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new RestAPIResponse("fail", "Error registering user"));
		}
	}

	// Get User by ID
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getUserById(@PathVariable Integer userId) {
		logger.info("Fetching user by ID: {}", userId);
		try {
			UserInfoDto user = iUserInfoService.getUserById(userId);
			if (user != null) {
				logger.info("User found: {}", user.getEmail());
				return ResponseEntity.ok(new RestAPIResponse("success", "User fetched successfully", user));
			} else {
				logger.warn("User not found for ID: {}", userId);
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new RestAPIResponse("fail", "User not found for ID " + userId));
			}
		} catch (Exception e) {
			logger.error("Error fetching user by ID: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new RestAPIResponse("fail", "Error fetching user"));
		}
	}

	// Get All Users with Pagination and Sorting
	@RequestMapping(value = "/users", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllUsers(@RequestBody SortingDTO sortingDTO) {
		logger.info("Fetching all users with pagination and sorting");
		try {
			Page<UserInfoDto> users = iUserInfoService.getAllUser(sortingDTO);
			return ResponseEntity.ok(new RestAPIResponse("success", "Users fetched successfully", users));
		} catch (Exception e) {
			logger.error("Error fetching all users: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new RestAPIResponse("fail", "Error fetching users"));
		}
	}

	// Update User
	@RequestMapping(value = "/update", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<RestAPIResponse> updateUser(@RequestBody UserUpdateDto userUpdateDto) {
		logger.info("Updating user with ID: {}");
		try {
			UserUpdateDto updatedUser = iUserInfoService.updateUser( userUpdateDto);
			return ResponseEntity.ok(new RestAPIResponse("success", "User updated successfully", updatedUser));
		} catch (Exception e) {
			logger.error("Error updating user with ID: {}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new RestAPIResponse("fail", "Error updating user"));
		}
	}

	// Delete User
	@RequestMapping(value = "/delete/{userId}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deleteUser(@PathVariable Integer userId) {
		logger.info("Deleting user with ID: {}", userId);
		try {
			boolean isDeleted = iUserInfoService.deleteUserById(userId);
			if (isDeleted) {
				return ResponseEntity.ok(new RestAPIResponse("success", "User deleted successfully", null));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new RestAPIResponse("fail", "User not found with ID " + userId));
			}
		} catch (Exception e) {
			logger.error("Error deleting user with ID: {}", userId, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new RestAPIResponse("fail", "Error deleting user"));
		}
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