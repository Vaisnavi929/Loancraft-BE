package com.narvee.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import com.narvee.request.dto.SortingDTO;
import com.narvee.request.dto.UserInfoDto;
import com.narvee.request.dto.UserUpdateDto;
public interface IUserInfoService {
	
	public static final Logger logger = LoggerFactory.getLogger(IUserInfoService.class);

	public UserInfoDto saveUser(UserInfoDto userinfodto);
	
	UserInfoDto getUserById(Integer userId);

	Page<UserInfoDto> getAllUser(SortingDTO sortingDTO);

	UserUpdateDto updateUser(UserUpdateDto userUpdateDto);

    boolean deleteUserById(Integer userId);
	
}