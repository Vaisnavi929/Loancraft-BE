package com.narvee.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
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

import com.narvee.entity.Customers;
import com.narvee.entity.Roles;
import com.narvee.entity.UserInfo;
import com.narvee.repository.CustomersRepository;
import com.narvee.repository.UserRepository;

@Service
public class UserAuthService implements UserDetailsService {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private CustomersRepository customerRepository;

	public static final Logger logger = LoggerFactory.getLogger(UserAuthService.class);


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	    logger.info("Inside UserAuthService: loadUserByUsername for email: {}", email);

	    // Check if email belongs to a customer
	    Optional<Customers> customerOptional = customerRepository.findByEmail(email);
	    if (customerOptional.isPresent()) {
	        Customers customer = customerOptional.get();
	        logger.info("Customer found with email: {}", email);
	        return new org.springframework.security.core.userdetails.User(
	                customer.getEmail(),
	                customer.getPassword(),
	                Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
	        );
	    }

	    // Check if email belongs to a user
	    UserInfo userInfo = repository.findByEmail(email)
	            .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
	    logger.info("User found with email: {}", email);

	    // Convert roles to authorities
	    Set<GrantedAuthority> authorities = userInfo.getRoles().stream()
	            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName().toUpperCase()))
	            .collect(Collectors.toSet());

	    return new org.springframework.security.core.userdetails.User(
	            userInfo.getEmail(),
	            userInfo.getPassword(),
	            authorities
	    );
	}


}
	