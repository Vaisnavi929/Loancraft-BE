package com.narvee.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.narvee.Service.IAuthenticationService;
import com.narvee.entity.Applicants;
import com.narvee.repository.ApplicantsRepository;
import com.narvee.request.dto.AuthResponse;
import com.narvee.util.JwtUtil;

@Service
public class AuthenticationServiceImpl implements IAuthenticationService {

	@Autowired
	private ApplicantsRepository applicantRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public Object authenticate(String usernameOrEmail, String password) {
		// Fetch the applicant from the repository using email
		Applicants applicant = applicantRepository.findByEmail(usernameOrEmail)
				.orElseThrow(() -> new BadCredentialsException("Applicant not found"));

		if (passwordEncoder.matches(password, applicant.getPassword())) {
			
			String token = jwtUtil.generateToken(usernameOrEmail);

			// Map the applicant information to the ResponseDto
			AuthResponse responseDto = new AuthResponse();
			responseDto.setFirstName(applicant.getFirstName());
			responseDto.setLastName(applicant.getLastName());
			responseDto.setMobileNo(applicant.getMobileNo());
			responseDto.setEmail(applicant.getEmail());
			responseDto.setToken(token); // Set the generated token

			return responseDto; // Return ResponseDto with applicant details and token
		} else {
			throw new BadCredentialsException("Invalid credentials");
		}
	}

}
