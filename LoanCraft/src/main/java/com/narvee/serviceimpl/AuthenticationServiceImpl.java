package com.narvee.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.narvee.Service.IAuthenticationService;
import com.narvee.entity.CustomersInfo;
import com.narvee.repository.CustomersRepository;
import com.narvee.request.dto.AuthResponse;
import com.narvee.util.EncryptionUtil;
import com.narvee.util.JwtUtil;

@Service
public class AuthenticationServiceImpl implements IAuthenticationService {

	@Autowired
	private CustomersRepository customersRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public Object authenticate(String usernameOrEmail, String password) {
		// Fetch the applicant from the repository using email
		CustomersInfo applicant = customersRepository.findByEmailOrMobileNumber(usernameOrEmail,usernameOrEmail)
				.orElseThrow(() -> new BadCredentialsException("Applicant not found"));
		

		if (passwordEncoder.matches(password, applicant.getPassword())) {
			
			String token = jwtUtil.generateToken(usernameOrEmail);

			// Map the applicant information to the ResponseDto
			AuthResponse responseDto = new AuthResponse();
			try {
				responseDto.setCustomerId(EncryptionUtil.encrypt(applicant.getCustomerId()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			responseDto.setFirstName(applicant.getFirstName());
			responseDto.setLastName(applicant.getLastName());
			responseDto.setMobileNumber(applicant.getMobileNumber());
			responseDto.setEmail(applicant.getEmail());
			responseDto.setToken(token); // Set the generated token

			return responseDto; // Return ResponseDto with applicant details and token
		} else {
			throw new BadCredentialsException("Invalid credentials");
		}
	}

}
