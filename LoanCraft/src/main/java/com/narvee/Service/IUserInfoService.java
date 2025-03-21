package com.narvee.Service;

import org.springframework.data.domain.Page;

import com.narvee.entity.UserInfo;
import com.narvee.request.dto.SortingDTO;
import com.narvee.request.dto.UserInfoDto;
import com.narvee.response.UserResponseDTO;

public interface IUserInfoService {

	public UserInfoDto saveUser(UserInfoDto userinfodto);

	UserInfo getUserById(String encryptedId) throws Exception;

	Page<UserResponseDTO>  getAllUser(SortingDTO sortingDTO);

	boolean deleteUserById(Long userId);

	public UserInfo UserById(Long id) throws Exception;

	UserInfoDto updateUser(UserInfoDto userUpdateDto);

}