package com.narvee.config;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.narvee.entity.CustomersInfo;
import com.narvee.entity.UserInfo;
import com.narvee.repository.CustomersRepository;
import com.narvee.repository.UserRepository;

@Service("userAuthService")
public class UserInfoAuthService implements UserDetailsService {
	public static final Logger logger = LoggerFactory.getLogger(UserInfoAuthService.class);

	@Autowired
	private UserRepository repository;

	@Autowired
	private CustomersRepository customerRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Load user by username called for email: {}", username);

		String[] parts = username.split("\\|");
		String email = parts[0];
		String loginType = null;

		if (parts.length > 1) {
			loginType = parts[1];
		}
		if (loginType == null) {

			logger.info("********************EMPLOYEE lOGIN**********************", username);

			// First, try to load from UserInfo
			Optional<UserInfo> userOptional = repository.findByEmail(email);
			if (userOptional.isPresent()) {
				logger.info("Found user in UserRepository");
				UserInfo userInfo = userOptional.get();
				return new User(userInfo.getEmail(), userInfo.getPassword(),
						userInfo.getRoles().stream()
								.map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName().toUpperCase()))
								.collect(Collectors.toList()));
			}

		} else {
			logger.info("********************CUSTOMER lOGIN**********************", username);
			// If not found, try to load from Customer
			Optional<CustomersInfo> customerOptional = customerRepository.findByEmailOrMobileNumber(email, email);
			if (customerOptional.isPresent()) {
				logger.info("Found customer in CustomersRepository");
				CustomersInfo customer = customerOptional.get();
				return new User(customer.getEmail(), customer.getPassword(),
						Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER")));
			}

		}

		// If neither found, throw exception
		logger.warn("User not found with email: {}", email);
		throw new UsernameNotFoundException("User not found with email: " + email);
	}

}
