package com.narvee.serviceimpl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.narvee.Service.IUserInfoService;
import com.narvee.entity.UserInfo;
import com.narvee.repository.UserRepository;
import com.narvee.request.dto.SortingDTO;
import com.narvee.request.dto.UserInfoDto;
import com.narvee.request.dto.UserUpdateDto;
import com.narvee.util.EncryptionUtil;

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
		logger.info("!!! inside class : UserInfoServiceimpl, !! method : saveUser");

		if (userRepo.findByEmail(userInfodto.getEmail()).isPresent()) {
			throw new IllegalArgumentException("Email already exists!");
		}

		UserInfo user = modelMapper.map(userInfodto, UserInfo.class);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		UserInfo savedUser = userRepo.save(user);
		logger.info("User saved successfully with ID: {}", savedUser.getUserId());
//		return modelMapper.map(savedUser, UserInfoDto.class);
		// Map saved entity back to DTO and encrypt userId
	    UserInfoDto responseDto = modelMapper.map(savedUser, UserInfoDto.class);
	   
	    try {
	    	responseDto.setUserId(EncryptionUtil.encrypt(savedUser.getUserId()));
	    } catch (Exception e) {
	        logger.error("Error encrypting user ID: {}", e.getMessage(), e);
	        throw new RuntimeException("Failed to encrypt user ID", e);
	    }

	    return responseDto;
	}

	@Override
	public UserInfoDto getUserById(Long userId) {
		logger.info("!!! inside class: UserInfoServiceImpl, method: getUserById");
		return userRepo.findById(userId).map(user -> modelMapper.map(user, UserInfoDto.class)).orElseThrow(null);
	}

	@Override
	public Page<UserInfoDto> getAllUser(SortingDTO sortingDTO) {
		logger.info("!!! inside class: UserInfoServiceImpl, method: getAllUser");

		String sortField = sortingDTO.getSortField();
		String sortOrder = sortingDTO.getSortOrder();
		String keyword = sortingDTO.getKeyword();
		Integer pageNumber = sortingDTO.getPageNumber();
		Integer pageSize = sortingDTO.getPageSize();

		if (sortField.equalsIgnoreCase("Name")) {
			sortField = "name";
		} else if (sortField.equalsIgnoreCase("Email")) {
			sortField = "email";
		} else if (sortField.equalsIgnoreCase("Password")) {
			sortField = "password";
		} else {
			sortField = "user_id";
		}

		Sort.Direction sortDirection = Sort.Direction.ASC;
		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
		}

		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(sortDirection, sortField));

		Page<UserInfo> userInfo;

		if (keyword == null || keyword.isEmpty()) {
			userInfo = userRepo.findAllUsers(pageable);
		} else {
			userInfo = userRepo.findUsersByKeyword(keyword, pageable);
		}
		return userInfo.map(user -> modelMapper.map(user, UserInfoDto.class));
	}

	@Override
	public UserUpdateDto updateUser(UserUpdateDto userUpdateDto) {
		logger.info("!!! inside class: UserInfoServiceImpl, method: updateUser");
		UserInfo user = userRepo.findById(userUpdateDto.getUserId()).get();
		String password=user.getPassword();
		
		if (user != null) {
			user = modelMapper.map(userUpdateDto, UserInfo.class);
			user.setPassword(password);
			user = userRepo.save(user);
		}
		
		UserUpdateDto users = modelMapper.map(user, UserUpdateDto.class);
		return users;

	}

	@Override
	public boolean deleteUserById(Long userId) {
		logger.info("!!! inside class: UserInfoServiceimpl, method: deleteUserById()");
		if (userRepo.existsById(userId)) {
			userRepo.deleteById(userId);
			logger.info("User deleted successfully with ID: {}", userId);
			return true;
		}

		logger.warn("User not found with ID: {}", userId);
		return false;
	}

}
