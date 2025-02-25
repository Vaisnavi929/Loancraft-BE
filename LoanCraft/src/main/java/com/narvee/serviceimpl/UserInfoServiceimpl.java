package com.narvee.serviceimpl;

import java.util.HashSet;
import java.util.Set;

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
import com.narvee.entity.Roles;
import com.narvee.entity.UserInfo;
import com.narvee.repository.IRolesRepository;
import com.narvee.repository.UserRepository;
import com.narvee.request.dto.SortingDTO;
import com.narvee.request.dto.UserInfoDto;
import com.narvee.response.UserResponseDTO;
import com.narvee.util.EncryptionUtil;

@Service
public class UserInfoServiceimpl implements IUserInfoService {

	public static final Logger logger = LoggerFactory.getLogger(UserInfoServiceimpl.class);

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private IRolesRepository iRolesRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserInfoDto saveUser(UserInfoDto userInfodto) {
		logger.info("!!! inside class : UserInfoServiceimpl, !! method : saveUser");

		if (userRepo.findByEmail(userInfodto.getEmail()).isPresent()) {
			throw new IllegalArgumentException("Email already exists!");
		}
		Roles uRole = iRolesRepository.findById(userInfodto.getRoleId()).get();
		Set<Roles> lRoles = new HashSet<>();
		lRoles.add(uRole);

		UserInfo user = modelMapper.map(userInfodto, UserInfo.class);
		user.setRoles(lRoles);

		System.err.println("Response" + user);
		user.setPassword(passwordEncoder.encode("Lms@123$"));
		UserInfo savedUser = userRepo.save(user);
		logger.info("User saved successfully with ID: {}", savedUser.getUserId());

		UserInfoDto responseDto = modelMapper.map(savedUser, UserInfoDto.class);

//		try {
//			responseDto.setUserId(EncryptionUtil.encrypt(savedUser.getUserId()));
//		} catch (Exception e) {
//			logger.error("Error encrypting user ID: {}", e.getMessage(), e);
//			throw new RuntimeException("Failed to encrypt user ID", e);
//		}

		return responseDto;
	}

	@Override
	public UserInfo getUserById(String encryptedId) throws Exception {
		logger.info("!!! inside class: UserInfoServiceImpl, method: getUserById");
		long decryptUserId = EncryptionUtil.decrypt(encryptedId);
		logger.info("Decrypted User ID: {}", decryptUserId);

		UserInfo user = userRepo.findById(decryptUserId).orElse(null);
		if (user != null) {
			logger.info("User found: {}", user.getEmail());
		} else {
			logger.info("User not found for ID: {}", decryptUserId);
		}
		return user;
	}

	@Override
	public UserInfo UserById(Long id) throws Exception {
		logger.info("!!! inside class: UserInfoServiceImpl, method: getUserById");

		UserInfo user = userRepo.findById(id).orElse(null);
		if (user != null) {
			logger.info("User found: {}", user.getEmail());
		} else {
			logger.info("User not found for ID: {}", id);
		}
		return user;
	}

	@Override
	public Page<UserResponseDTO> getAllUser(SortingDTO sortingDTO) {
		logger.info("!!! inside class: UserInfoServiceImpl, method: getAllUser");

		String sortField = sortingDTO.getSortField();
		String sortOrder = sortingDTO.getSortOrder();
		String keyword = sortingDTO.getKeyword();
		Integer pageNumber = sortingDTO.getPageNumber();
		Integer pageSize = sortingDTO.getPageSize();

		if (sortField.equalsIgnoreCase("Name"))
			sortField = "name";
		else if (sortField.equalsIgnoreCase("Mobile_Number"))
			sortField = "mobile_number";
		else if (sortField.equalsIgnoreCase("Createddate"))
			sortField = "createddate";
		else if (sortField.equalsIgnoreCase("designation"))
			sortField = "designation";
		else if (sortField.equalsIgnoreCase("Email"))
			sortField = "email";
		else if (sortField.equalsIgnoreCase("status"))
			sortField = "status";
		else
			sortField = "updateddate";

		Sort.Direction sortDirection = Sort.Direction.ASC;
		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
		}

		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(sortDirection, sortField));

		Page<UserResponseDTO> userInfo;

		if (keyword.equalsIgnoreCase("empty")) {
			logger.info("!!! inside class: UserInfoServiceImpl, method: all");
			userInfo = userRepo.findAllUsers(pageable);
		} else {
			logger.info("!!! inside class: UserInfoServiceImpl, method: filter");
			userInfo = userRepo.findUsersByKeyword(keyword, pageable);
		}
		return userInfo;
	}

	@Override
	public UserInfoDto updateUser(UserInfoDto userUpdateDto) {
		logger.info("!!! inside class: UserInfoServiceImpl, method: updateUser");
		
		Roles uRole = iRolesRepository.findById(userUpdateDto.getRoleId()).get();
		Set<Roles> lRoles = new HashSet<>();
		lRoles.add(uRole);
		UserInfo usersInfo = modelMapper.map(userUpdateDto, UserInfo.class);
		usersInfo.setRoles(lRoles);
		userRepo.save(usersInfo);
		return userUpdateDto;
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
