package com.narvee.serviceimpl;

import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.narvee.request.dto.UserInfoDto;
import com.narvee.Service.IUserInfoService;
import com.narvee.entity.Applicants;
import com.narvee.entity.UserInfo;
import com.narvee.repository.UserRepository;

@Service
public class UserInfoServiceimpl implements IUserInfoService {

	public static final Logger logger = LoggerFactory.getLogger(UserInfoServiceimpl.class);
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserInfoDto saveUser(UserInfoDto userInfodto) {
		logger.info("!!! inside class : UserAuthService, !! method : saveUser");
		
		Optional<UserInfo> existingUser = userRepo.findByEmail(userInfodto.getEmail());
	    if (existingUser.isPresent()) {
	        throw new IllegalArgumentException("Email already exists!");
	    }

		UserInfo user = modelMapper.map(userInfodto, UserInfo.class);

		// Encode password
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// Save user
		UserInfo savedUser = userRepo.save(user);

		// Map saved entity back to DTO
		return modelMapper.map(savedUser, UserInfoDto.class);
	}
}
