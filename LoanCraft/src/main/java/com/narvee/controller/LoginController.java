package com.narvee.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.IlogoutService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.entity.Privilege;
import com.narvee.entity.Roles;
import com.narvee.entity.UserInfo;
import com.narvee.exception.UserDoesNotExist;
import com.narvee.repository.IRolesRepository;
import com.narvee.repository.UserRepository;
import com.narvee.request.dto.AuthRequest;
import com.narvee.request.dto.AuthResponse;
import com.narvee.request.dto.LoginRequest;
import com.narvee.response.Response;
import com.narvee.serviceimpl.AuthenticationServiceImpl;
import com.narvee.util.JwtUtil;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth/")
public class LoginController {
	public static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserRepository repository;

	@Autowired
	private IRolesRepository Rolesrepository;

	@Autowired
	private AuthenticationServiceImpl authenticationService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private IlogoutService ilogoutService;

	@PostMapping("/generateToken")
	public ResponseEntity<RestAPIResponse> authenticateAndGetToken(@RequestBody AuthRequest authRequest)
			throws BadCredentialsException {
		logger.info("!!! inside class : LoginController, !! method : authenticateAndGetToken");

		UserInfo userDetails = repository.findByEmail(authRequest.getUsername()).orElseThrow(
				() -> new UserDoesNotExist("User does not exist with email: " + authRequest.getUsername()));

		Authentication authentication = null;

		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

			logger.info("!!! inside class : LoginController, !! method : authenticateAndGetToken inside try block");

		} catch (BadCredentialsException e) {
			logger.info("!!! inside class: LoginController, !! method: generateJwtToken => Invalid Credentials");
			return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("failed", "Invalid Credentials"),
					HttpStatus.UNAUTHORIZED);
		} catch (UserDoesNotExist e) {
			logger.info("!!! inside class: LoginController, !! method: generateJwtToken => UserDoesNotExist");
			return new ResponseEntity<RestAPIResponse>(
					new RestAPIResponse("failed", "User does not exist with email: " + authRequest.getUsername()),
					HttpStatus.NOT_FOUND);
		} catch (UsernameNotFoundException e) {
			logger.error("Username not found: {}", authRequest.getUsername());
			return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "User with email ", e),
					HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			logger.error("An error occurred: {}", e.getMessage());
			return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "An error occurred: ", e),
					HttpStatus.UNAUTHORIZED);

		}

		Response response = new Response();
		String token = jwtUtil.generateToken(authRequest.getUsername());
		response.setToken(token);

		User user = (User) authentication.getPrincipal();

//			Set<String> roles = user.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());
//			roles.forEach(rl -> {
//				response.setRoles(rl);
//			});

		Set<String> roles = user.getAuthorities().stream().map(r -> r.getAuthority().replace("ROLE_", "")) // Remove
																											// "ROLE_"
																											// if
																											// present
				.collect(Collectors.toSet());

		roles.forEach(rl -> {
			response.setRoles(rl);
		});

		List<String> rolePrivileges = new ArrayList<>();
		String sb = new String();
		sb = roles.toString();
		sb = sb.replace("[", "");
		sb = sb.replace("]", "");
		Set<Roles> userRole = userDetails.getRoles();
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		Roles existRole = Rolesrepository.findByRoleNameIgnoreCase(sb).get();
		response.setUserid(userDetails.getUserId());
		response.setFullname(userDetails.getName());
		response.setRoleno(existRole.getRoleId());
		if (null != existRole) {
			for (Privilege privilege : existRole.getPrivileges()) {
				authorities.add(new SimpleGrantedAuthority(privilege.getName()));
//					rolePrivileges.add(privilege.getName());
				rolePrivileges.add(privilege.getName().replaceAll("\\d", ""));
			}
		}

		response.setRolePrivileges(rolePrivileges);
		;

		return new ResponseEntity<>(new RestAPIResponse("success", "User logged in successful", response),
				HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<RestAPIResponse> login(@RequestBody LoginRequest request) {
		logger.info("Login attempt for: {}", request.getUsernameOrEmail());

		Authentication authentication = null;

		try {
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					request.getUsernameOrEmail() + "|Customer", request.getPassword()));

			logger.info("!!! inside class : LoginController, !! method : authenticateAndGetToken inside try block");

		} catch (BadCredentialsException e) {
			logger.info("!!! inside class: LoginController, !! method: generateJwtToken => Invalid Credentials");
			return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("failed", "Invalid Credentials"),
					HttpStatus.UNAUTHORIZED);
		} catch (UserDoesNotExist e) {
			logger.info("!!! inside class: LoginController, !! method: generateJwtToken => UserDoesNotExist");
			return new ResponseEntity<RestAPIResponse>(
					new RestAPIResponse("failed", "User does not exist with email: " + request.getUsernameOrEmail()),
					HttpStatus.NOT_FOUND);
		} catch (UsernameNotFoundException e) {
			logger.error("Username not found: {}", request.getUsernameOrEmail());
			return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "User with email ", e),
					HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			logger.error("An error occurred: {}", e.getMessage());
			return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "An error occurred: ", e),
					HttpStatus.UNAUTHORIZED);

		}

		AuthResponse auth = authenticationService.authenticate(request.getUsernameOrEmail());

		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("success", "fetched all loan application details ", auth), HttpStatus.OK);

	}

	@PostMapping("/logout")
	public ResponseEntity<RestAPIResponse> logout(@RequestHeader("Authorization") String token) {
		logger.info("Logout attempt received");

		if (token == null || !token.startsWith("Bearer ")) {
			logger.warn("Invalid token format received");
			return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("failed", "Invalid token format"),
					HttpStatus.BAD_REQUEST);
		}

		String jwtToken = token.substring(7);
		logger.info("Extracted Token: {}", jwtToken);

		boolean isLoggedOut = ilogoutService.logout(jwtToken);

		if (!isLoggedOut) {
			logger.warn("Token already blacklisted: {}", jwtToken);
			return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("failed", "Token already blacklisted"),
					HttpStatus.BAD_REQUEST);
		}

		logger.info("Logout successful for token: {}", jwtToken);

		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "Token already blacklisted"),
				HttpStatus.BAD_REQUEST);
	} 
	
	
//	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST, produces = "application/json")
//	public ResponseEntity<RestAPIResponse> forgotPassword(@RequestBody ResetPassword user) {
//		logger.info("!!! inside class: LoginController, !! method: processForgotPassword ");
//		UserInfo userDetails = repository.findByEmail(user.getEmail());
//		if (userDetails != null) {
//			long expirationTime = System.currentTimeMillis() + OTP.OTPEXP;
//			OTP otp = new OTP();
//			String genOtp = OTPGenerator.generateRandomPassword(4);
//			otp.setExpirationTime(expirationTime);
//			otp.setUserid(userDetails.getUserid());
//			otp.setOtp(genOtp);
//			user.setUsername(userDetails.getFullname());
//			user.setOtp(genOtp);
//			OTP otpRes = otpRepo.save(otp);
//			//emailClientService.resetlinkmail(user);
//			emailLoginService.sendAtsForgotPassword(user);
//			return new ResponseEntity<>(new RestAPIResponse("success", "OTP sent Successfully", otpRes), HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>(
//					new RestAPIResponse("failed", "User does not exist. Please check the username and try again."),
//					HttpStatus.OK);
//		}
//
//	}
//	
	

	@GetMapping("/get")
	public UserInfo test()

	{
		Optional<UserInfo> u = repository.findByEmail("shanpasha@narveetech.com");
		return u.get();

	}
}
