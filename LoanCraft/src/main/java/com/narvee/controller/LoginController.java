package com.narvee.controller;

import java.time.LocalDateTime;
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

import com.narvee.Service.EmailOtpService;
import com.narvee.Service.IlogoutService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.entity.Privilege;
import com.narvee.entity.Roles;
import com.narvee.entity.UserInfo;
import com.narvee.exception.UserDoesNotExist;
import com.narvee.repository.IRolesRepository;
import com.narvee.repository.UserRepository;
import com.narvee.request.dto.ApplicantDto;
import com.narvee.request.dto.AuthRequest;
import com.narvee.request.dto.AuthResponse;
import com.narvee.request.dto.LoginRequest;
import com.narvee.request.dto.OtpLoginRequest;
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
	
	@Autowired
	private EmailOtpService emailOtpService;
	

	

	@PostMapping("/generateToken")
	public ResponseEntity<RestAPIResponse> authenticateAndGetToken(@RequestBody AuthRequest authRequest)
	        throws BadCredentialsException {
	    logger.info("!!! inside class : LoginController, !! method : authenticateAndGetToken");

	    UserInfo userDetails = repository.findByEmail(authRequest.getUsername()).orElseThrow(
	            () -> new UserDoesNotExist("User does not exist with email: " + authRequest.getUsername()));
	    
	    
	   //  1. Check if SuperAdmin/Admin (never locked)
	    boolean isAdmin = userDetails.getRoles().stream()
	            .map(Roles::getRoleName)
	            .anyMatch(role -> role.equalsIgnoreCase("SUPERADMIN") || role.equalsIgnoreCase("ADMIN"));

	    
	    
	    //  If already locked, stop immediately
	    if ("InActive".equalsIgnoreCase(userDetails.getStatus())) {
	        logger.warn("Account is locked for user: {}", authRequest.getUsername());
	        return new ResponseEntity<>(
	                new RestAPIResponse("failed", "Account Locked. Please contact admin."),
	                HttpStatus.FORBIDDEN);
	    }
	    
	 //  Check inactivity AFTER successful authentication
	    if (!isAdmin && userDetails.getLastLogin() != null && "Active".equalsIgnoreCase(userDetails.getStatus())) {
	        LocalDateTime lastLogin = userDetails.getLastLogin();
	        if (lastLogin.isBefore(LocalDateTime.now().minusDays(3))) {
	            userDetails.setStatus("InActive");
	            repository.save(userDetails);
	            logger.warn("User {} locked due to inactivity of 3+ days", authRequest.getUsername());
	            return new ResponseEntity<>(
	                    new RestAPIResponse("failed", "Account Locked due to inactivity (3+ days)."),
	                    HttpStatus.FORBIDDEN);
	        }
	    }
	    

	    Authentication authentication = null;
	    try {
	        authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
	        logger.info("!!! inside class : LoginController, !! method : authenticateAndGetToken inside try block");
	    } catch (BadCredentialsException e) {
	        logger.info("!!! inside class: LoginController, !! method: generateJwtToken => Invalid Credentials");
	        return new ResponseEntity<RestAPIResponse>(
	                new RestAPIResponse("failed", "Invalid Credentials"),
	                HttpStatus.UNAUTHORIZED);
	    } catch (UserDoesNotExist e) {
	        logger.info("!!! inside class: LoginController, !! method: generateJwtToken => UserDoesNotExist");
	        return new ResponseEntity<RestAPIResponse>(
	                new RestAPIResponse("failed", "User does not exist with email: " + authRequest.getUsername()),
	                HttpStatus.NOT_FOUND);
	    } catch (UsernameNotFoundException e) {
	        logger.error("Username not found: {}", authRequest.getUsername());
	        return new ResponseEntity<RestAPIResponse>(
	                new RestAPIResponse("success", "User with email ", e),
	                HttpStatus.UNAUTHORIZED);
	    } catch (Exception e) {
	        logger.error("An error occurred: {}", e.getMessage());
	        return new ResponseEntity<RestAPIResponse>(
	                new RestAPIResponse("success", "An error occurred: ", e),
	                HttpStatus.UNAUTHORIZED);
	    }

	    Response response = new Response();
	    String token = jwtUtil.generateToken(authRequest.getUsername());
	    response.setToken(token);

	    User user = (User) authentication.getPrincipal();
	    Set<String> roles = user.getAuthorities().stream()
	            .map(r -> r.getAuthority().replace("ROLE_", "")) // Remove ROLE_
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
	            rolePrivileges.add(privilege.getName().replaceAll("\\d", ""));
	        }
	    }

	    response.setRolePrivileges(rolePrivileges);
	    
	    userDetails.setLastLogin(LocalDateTime.now());
	    if("UnLock".equalsIgnoreCase(userDetails.getStatus())) {
	    	userDetails.setStatus("Active");
	    }
	    repository.save(userDetails);

	    return new ResponseEntity<>(
	            new RestAPIResponse("success", "User logged in successful", response),
	            HttpStatus.OK);
	}



	@PostMapping("/login")
	public ResponseEntity<RestAPIResponse> login(@RequestBody LoginRequest request) {
	    logger.info("Login attempt for: {}", request.getUsernameOrEmail());

	    Authentication authentication = null;
	    try {
	        authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(
	                        request.getUsernameOrEmail() + "|Customer",
	                        request.getPassword()));
	        logger.info("!!! inside class : LoginController, !! method : authenticateAndGetToken inside try block");
	    } catch (BadCredentialsException e) {
	        logger.info("!!! inside class: LoginController, !! method: generateJwtToken => Invalid Credentials");
	        return new ResponseEntity<RestAPIResponse>(
	                new RestAPIResponse("failed", "Invalid Credentials"),
	                HttpStatus.UNAUTHORIZED);
	    } catch (UserDoesNotExist e) {
	        logger.info("!!! inside class: LoginController, !! method: generateJwtToken => UserDoesNotExist");
	        return new ResponseEntity<RestAPIResponse>(
	                new RestAPIResponse("failed", "User does not exist with email: " + request.getUsernameOrEmail()),
	                HttpStatus.NOT_FOUND);
	    } catch (UsernameNotFoundException e) {
	        logger.error("Username not found: {}", request.getUsernameOrEmail());
	        return new ResponseEntity<RestAPIResponse>(
	                new RestAPIResponse("success", "User with email ", e),
	                HttpStatus.UNAUTHORIZED);
	    } catch (Exception e) {
	        logger.error("An error occurred: {}", e.getMessage());
	        return new ResponseEntity<RestAPIResponse>(
	                new RestAPIResponse("success", "An error occurred: ", e),
	                HttpStatus.UNAUTHORIZED);
	    }

	    AuthResponse auth = authenticationService.authenticate(request.getUsernameOrEmail());
	    return new ResponseEntity<RestAPIResponse>(
	            new RestAPIResponse("success", "fetched all loan application details ", auth),
	            HttpStatus.OK);
	}
	

	@PostMapping("/otp-login")
	public ResponseEntity<RestAPIResponse> loginWithOtp(@RequestBody OtpLoginRequest request) {
	logger.info("OTP login attempt for: {}", request.getEmail());

	// 1. Verify OTP
	boolean isValid = emailOtpService.verifyOtpForEmail(request.getEmail(), request.getOtp());
	if (!isValid) {
	return new ResponseEntity<>(
	new RestAPIResponse("failed", "Invalid or expired OTP"),
	HttpStatus.UNAUTHORIZED
	);
	}

	// 2. Fetch employee user
	UserInfo user = repository.findByEmail(request.getEmail())
	.orElseThrow(() -> new UserDoesNotExist("User does not exist with email: " + request.getEmail()));

	// 3. Generate JWT token
	String token = jwtUtil.generateToken(user.getEmail());

	Response response = new Response();
	response.setToken(token);
	response.setUserid(user.getUserId());
	response.setFullname(user.getName());


	/// ✅ Extract roles
	Set<String> roles = user.getRoles().stream()
	.map(r -> r.getRoleName())
	.collect(Collectors.toSet());
	response.setRoles(String.join(",", roles));

	// 6️⃣ Extract Privileges (as List<String>)
	List<String> privileges = user.getRoles().stream()
	.flatMap(role -> role.getPrivileges().stream())
	.map(Privilege::getName)
	.distinct()
	.collect(Collectors.toList());
	response.setRolePrivileges(privileges);


	// 7️⃣ Return success response
	return new ResponseEntity<>(
	new RestAPIResponse("success", "User logged in with OTP successfully", response),
	HttpStatus.OK
	);
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

	@PostMapping("/send-otp")
	public ResponseEntity<RestAPIResponse> sendOtp(@RequestBody ApplicantDto request) {
	    try {
	        String result = emailOtpService.sendOtp(request);
	        return new ResponseEntity<>(
	                new RestAPIResponse("success", result),
	                HttpStatus.OK
	        );
	    } catch (Exception e) {
	        return new ResponseEntity<>(
	                new RestAPIResponse("failed", "Error sending OTP: " + e.getMessage()),
	                HttpStatus.valueOf("Invalid OTP. Please try again.")
	        );
	    }
	}
	@GetMapping("/get")
	public UserInfo test()

	{
		Optional<UserInfo> u = repository.findByEmail("ravi@narveetech.com");
		return u.get();

	}
}
