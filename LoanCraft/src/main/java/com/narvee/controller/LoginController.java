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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.commons.RestAPIResponse;
import com.narvee.entity.AuthRequest;
import com.narvee.entity.Privilege;
import com.narvee.entity.Roles;
import com.narvee.entity.UserInfo;
import com.narvee.exception.InvalidUserCredentialsException;
import com.narvee.repository.IRolesRepository;
import com.narvee.repository.UserRepository;
import com.narvee.response.Response;
import com.narvee.util.JwtUtil;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth")
public class LoginController {
	public static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private IRolesRepository Rolesrepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/generateToken")
	public ResponseEntity<RestAPIResponse> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		
		logger.info("!!! inside class : LoginController, !! method : authenticateAndGetToken");
		
	   UserInfo userDetails = repository.findByEmail(authRequest.getUsername()).get();
		Authentication authentication=null;
		try {
			 authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
			logger.info("!!! inside class : LoginController, !! method : authenticateAndGetToken inside try block");

		} catch (BadCredentialsException e) {
			logger.info("!!! inside class: LoginController, !! method: generateJwtToken => Invalid Credentials");
			throw new InvalidUserCredentialsException("Invalid Credentials");
		}
		 
		    Response response= new Response();
			String token = jwtUtil.generateToken(authRequest.getUsername());
			response.setToken(token);
			
			User user = (User) authentication.getPrincipal();
			
//			Set<String> roles = user.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());
//			roles.forEach(rl -> {
//				response.setRoles(rl);
//			});
			
			Set<String> roles = user.getAuthorities().stream()
				    .map(r -> r.getAuthority().replace("ROLE_", "")) // Remove "ROLE_" if present
				    .collect(Collectors.toSet());

				roles.forEach(rl -> {
				    response.setRoles(rl);
				});
				
			List<String> rolePrivileges = new ArrayList<>();
			
			String sb=new String();
			sb=roles.toString();
			sb=sb.replace("[", "");
			sb=sb.replace("]", "");
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
			
			response.setRolePrivileges(rolePrivileges);;
			
			return new ResponseEntity<>(new RestAPIResponse("success", "User logged in successful", response), HttpStatus.OK);
	}

}

