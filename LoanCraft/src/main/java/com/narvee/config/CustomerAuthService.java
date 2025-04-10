package com.narvee.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.narvee.entity.CustomersInfo;
import com.narvee.repository.CustomersRepository;

@Service("customerAuthService")
public class CustomerAuthService implements UserDetailsService {

    @Autowired
    private CustomersRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CustomersInfo customer = customerRepository.findByEmailOrMobileNumber(email, email)
            .orElseThrow(() -> new UsernameNotFoundException("Customer not found with email or mobile: " + email));

        return new User(customer.getEmail(), customer.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER")));
    }
}
