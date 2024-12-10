package com.narvee.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.narvee.request.dto.UserInfoDto;

public interface IUserInfoService {
	
	public static final Logger logger = LoggerFactory.getLogger(IUserInfoService.class);

	public UserInfoDto saveUser(UserInfoDto userinfodto);
	
}