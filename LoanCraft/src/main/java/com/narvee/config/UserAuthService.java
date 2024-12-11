package com.narvee.config;

import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.narvee.entity.Roles;
import com.narvee.entity.UserInfo;
import com.narvee.repository.UserRepository;

@Service
public class UserAuthService implements UserDetailsService {
	
	@Autowired
	private UserRepository repository;

	public static final Logger logger = LoggerFactory.getLogger(UserAuthService.class);

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	    logger.info("!!! inside class : UserAuthService, !! method : loadUserByUsername");
	    // Find user by email
	    UserInfo userInfo = repository.findByEmail(email)
	            .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
            
	    // Check if roles are null and safely handle them
	    Set<Roles> roles = userInfo.getRoles();
	  
	    if (roles == null || roles.isEmpty()) {
	        throw new UsernameNotFoundException("User has no roles assigned");  // Or handle it in a different way
	    }

	    // Convert roles to GrantedAuthority
	    Set<GrantedAuthority> authorities = roles.stream()
	            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName().toUpperCase()))  // Assuming RoleEntity has getRoleName()
	            .collect(Collectors.toSet());

	    return new org.springframework.security.core.userdetails.User(
	            userInfo.getEmail(),  // Use email as the "username"
	            userInfo.getPassword(),  // Get the user's password
	            authorities  // Set the user's authorities (roles)
	    );
	}

	}
