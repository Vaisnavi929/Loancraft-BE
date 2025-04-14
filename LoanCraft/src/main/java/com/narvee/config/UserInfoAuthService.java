package com.narvee.config;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

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

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private CustomersRepository customerRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// First, try to load from UserInfo
	    Optional<UserInfo> userOptional = repository.findByEmail(email);

	    if (userOptional.isPresent()) {
	        UserInfo userInfo = userOptional.get();
	        return new User(
	            userInfo.getEmail(),
	            userInfo.getPassword(),
	            userInfo.getRoles().stream()
	                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName().toUpperCase()))
	                .collect(Collectors.toList())
	        );
	    }

	    // If not found, try to load from Customer
	    Optional<CustomersInfo> customerOptional = customerRepository.findByEmailOrMobileNumber(email,email);

	    if (customerOptional.isPresent()) {
	        CustomersInfo customer = customerOptional.get();
	        return new User(
	            customer.getEmail(),
	            customer.getPassword(),
	            Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
	        );
	    }

	    // If neither found, throw exception
	    throw new UsernameNotFoundException("User not found with email: " + email);


		
	}
}
