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

import org.springframework.data.domain.PageImpl;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
		if (!userRepo.findByMobileNumber(userInfodto.getMobileNumber()).isEmpty()) {
			throw new IllegalArgumentException("Mobile Number Alredy exists..!");
		}

		Roles uRole = iRolesRepository.findById(userInfodto.getRoleId()).get();
		Set<Roles> lRoles = new HashSet<>();
		lRoles.add(uRole);

		UserInfo user = modelMapper.map(userInfodto, UserInfo.class);
		user.setRoles(lRoles);

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
		
		//return responseDto;
		return modelMapper.map(savedUser, UserInfoDto.class);
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
	// Abhinay Added
	@Override
	public Page<UserResponseDTO> getAllUser(SortingDTO sortingDTO) {
	    logger.info("!!! inside class: UserInfoServiceImpl, method: getAllUser");

	    String sortField   = sortingDTO.getSortField();
	    String sortOrder   = sortingDTO.getSortOrder();
	    String keyword     = sortingDTO.getKeyword();
	    Integer pageNumber = sortingDTO.getPageNumber();
	    Integer pageSize   = sortingDTO.getPageSize();

	    int page = (pageNumber == null || pageNumber < 1) ? 0 : pageNumber - 1;
	    int size = (pageSize == null || pageSize < 1) ? 10 : pageSize;

	    String sortFieldNormalized = (sortField == null) ? "" : sortField.trim().toLowerCase();

	    // We still build Pageable, but sort in Java later
	    Pageable pageable = PageRequest.of(page, size);

	    boolean noFilter = (keyword == null
	            || keyword.trim().isEmpty()
	            || keyword.equalsIgnoreCase("empty"));

	    Page<UserResponseDTO> userInfoPage;

	    if (noFilter) {
	        logger.info("!!! inside class: UserInfoServiceImpl, method: all (no filter)");
	        userInfoPage = userRepo.findAllUsers(pageable);
	    } else {
	        logger.info("!!! inside class: UserInfoServiceImpl, method: filter, keyword: {}", keyword);
	        userInfoPage = userRepo.findUsersByKeyword(keyword.trim(), pageable);
	    }

	    // 🔥 Manual sorting in Java based on sortField + sortOrder
	    List<UserResponseDTO> content = new ArrayList<>(userInfoPage.getContent());

	    Comparator<UserResponseDTO> comparator = null;

	    switch (sortFieldNormalized) {
	        case "name":
	            comparator = Comparator.comparing(
	                    dto -> nullSafeString(dto.getName()),
	                    String.CASE_INSENSITIVE_ORDER
	            );
	            break;

	        case "email":
	            comparator = Comparator.comparing(
	                    dto -> nullSafeString(dto.getEmail()),
	                    String.CASE_INSENSITIVE_ORDER
	            );
	            break;

	        case "mobile_number":
	        case "mobilenumber":
	            comparator = Comparator.comparing(
	                    dto -> nullSafeString(dto.getMobile_number()),
	                    String.CASE_INSENSITIVE_ORDER
	            );
	            break;

	        case "updatedby":
	        case "updateby":
	            // 👇 THIS is what you want
	            comparator = Comparator.comparing(
	                    dto -> nullSafeString(dto.getUpdatedby()),
	                    String.CASE_INSENSITIVE_ORDER
	            );
	            break;

	        case "updateddate":
	            comparator = Comparator.comparing(
	                    UserResponseDTO::getUpdateddate,
	                    Comparator.nullsLast(Comparator.naturalOrder())
	            );
	            break;

	        case "createddate":
	            comparator = Comparator.comparing(
	                    UserResponseDTO::getCreateddate,
	                    Comparator.nullsLast(Comparator.naturalOrder())
	            );
	            break;

	        default:
	            // default to updateddate
	            comparator = Comparator.comparing(
	                    UserResponseDTO::getUpdateddate,
	                    Comparator.nullsLast(Comparator.naturalOrder())
	            );
	            break;
	    }

	    if (comparator != null) {
	        if ("desc".equalsIgnoreCase(sortOrder)) {
	            comparator = comparator.reversed();
	        }
	        content.sort(comparator);
	    }

	    // 🔁 Wrap sorted list into new Page object
	    return new PageImpl<>(content, pageable, userInfoPage.getTotalElements());
	}

	private String nullSafeString(String value) {
	    return value == null ? "" : value.trim();
	}
	// Abhinay Added

	/*
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
		else if (sortField.equalsIgnoreCase("addedBy"))
			sortField = "addedby";
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
	*/
	
	@Override
	public UserInfoDto updateUser(UserInfoDto userUpdateDto) {
	    logger.info("!!! inside class: UserInfoServiceImpl, method: updateUser");

	    // 1️⃣ Fetch existing user
	    UserInfo existingUser = userRepo.findById(userUpdateDto.getUserId())
	            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userUpdateDto.getUserId()));

	    // 2️⃣ Email duplicate check (other users only)
	    if (userUpdateDto.getEmail() != null &&
	        userRepo.existsByEmailAndUserIdNot(userUpdateDto.getEmail(), userUpdateDto.getUserId())) {
	        throw new RuntimeException("Email already in use: " + userUpdateDto.getEmail());
	    }

	    // 3️⃣ Mobile duplicate check (other users only)
	    if (userUpdateDto.getMobileNumber() != null &&
	        userRepo.existsByMobileNumberAndUserIdNot(userUpdateDto.getMobileNumber(), userUpdateDto.getUserId())) {
	        throw new RuntimeException("Mobile Number already in use: " + userUpdateDto.getMobileNumber());
	    }

	    // 4️⃣ Role mapping
	    Roles uRole = iRolesRepository.findById(userUpdateDto.getRoleId())
	            .orElseThrow(() -> new IllegalArgumentException("Invalid Role ID: " + userUpdateDto.getRoleId()));
	    Set<Roles> lRoles = new HashSet<>();
	    lRoles.add(uRole);

	    // 5️⃣ Update fields
	    existingUser.setName(userUpdateDto.getName());
	    existingUser.setEmail(userUpdateDto.getEmail());
	    existingUser.setMobileNumber(userUpdateDto.getMobileNumber());
	    existingUser.setRoles(lRoles);

	    // 👇 If you added location
	    existingUser.setLocation(userUpdateDto.getLocation());

	    UserInfo updatedUser = userRepo.save(existingUser);

	    return modelMapper.map(updatedUser, UserInfoDto.class);
	}


	/*@Override
	public UserInfoDto updateUser(UserInfoDto userUpdateDto) {
		logger.info("!!! inside class: UserInfoServiceImpl, method: updateUser");

		Roles uRole = iRolesRepository.findById(userUpdateDto.getRoleId()).get();
		Set<Roles> lRoles = new HashSet<>();
		lRoles.add(uRole);
		UserInfo usersInfo = modelMapper.map(userUpdateDto, UserInfo.class);
		if (userUpdateDto.getEmail() != null && !userUpdateDto.getEmail().equalsIgnoreCase(usersInfo.getEmail())) {
	        if (userRepo.findByEmail(userUpdateDto.getEmail()).isPresent()) {
	            throw new RuntimeException("Email already in use: " + userUpdateDto.getEmail());
	        }
	    }
		usersInfo.setRoles(lRoles);
		userRepo.save(usersInfo);
		
		return userUpdateDto;
	}*/
	
//	@Override
//	public UserInfoDto updateUser(UserInfoDto userUpdateDto) {
//	    logger.info("!!! inside class: UserInfoServiceImpl, method: updateUser");
//
//	    // Fetch the existing user
//	    UserInfo existingUser = userRepo.findById(userUpdateDto.getUserId())
//	            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userUpdateDto.getUserId()));
//
//	    userRepo.findByEmail(userUpdateDto.getEmail()).ifPresent(userWithEmail -> {
//	        if (!userWithEmail.getUserId().equals(userUpdateDto.getUserId())) {
//	            throw new IllegalArgumentException("Email already exists! Please use another email.");
//	        }
//	    });
//
//	    if (!userRepo.findByMobileNumber(userUpdateDto.getMobileNumber()).isEmpty()) {
//	        boolean mobileExists = userRepo.findByMobileNumber(userUpdateDto.getMobileNumber())
//	                .stream()
//	                .anyMatch(userWithMobile -> !userWithMobile.getUserId().equals(userUpdateDto.getUserId()));
//	        if (mobileExists) {
//	            throw new IllegalArgumentException("Mobile Number already exists! Please use another number.");
//	        }
//	    }
//
//	    Roles uRole = iRolesRepository.findById(userUpdateDto.getRoleId())
//	            .orElseThrow(() -> new IllegalArgumentException("Invalid Role ID: " + userUpdateDto.getRoleId()));
//	    Set<Roles> lRoles = new HashSet<>();
//	    lRoles.add(uRole);
//
//	    existingUser.setName(userUpdateDto.getName());
//	    existingUser.setEmail(userUpdateDto.getEmail());
//	    existingUser.setMobileNumber(userUpdateDto.getMobileNumber());
//
//	    existingUser.setRoles(lRoles);
//
//	    UserInfo updatedUser = userRepo.save(existingUser);
//	    return modelMapper.map(updatedUser, UserInfoDto.class);
//	}

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

	@Override
	public boolean DuplicateCheckEmail(String email) {
		if (userRepo.findByEmail(email).isPresent()) {
		return true;
	    }
		return false;
	}
	
	@Override
	public boolean DuplicateCheckMobileNum(String num) {
		if (userRepo.findByMobileNumber(num).isPresent()) {
		return true;
	    }
		return false;
	}
}
