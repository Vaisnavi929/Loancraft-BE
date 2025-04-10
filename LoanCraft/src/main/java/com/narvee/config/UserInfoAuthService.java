package com.narvee.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.narvee.entity.UserInfo;
import com.narvee.repository.UserRepository;

import java.util.stream.Collectors;

@Service("userAuthService")
public class UserInfoAuthService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserInfo userInfo = repository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

		return new User(userInfo.getEmail(), userInfo.getPassword(),
				userInfo.getRoles().stream()
						.map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName().toUpperCase()))
						.collect(Collectors.toList()));
	}
}
