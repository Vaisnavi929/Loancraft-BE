package com.narvee.serviceimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.narvee.Service.IlogoutService;
import com.narvee.entity.BlacklistedToken;
import com.narvee.repository.BlacklistedTokenRepository;
import com.narvee.util.JwtUtil;

@Service
public class LogoutServiceImpl implements IlogoutService {

	private static final Logger logger = LoggerFactory.getLogger(LogoutServiceImpl.class);

	@Value("${jwt.token.validity}")
	private long tokenValidity;

	@Autowired
	private BlacklistedTokenRepository blacklistedTokenRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public boolean logout(String token) {
		logger.info("Initiating logout process.");

		String email = jwtUtil.extractUsername(token);
		logger.info("Extracted email from token: {}", email);

		// Check if the token is already blacklisted
		boolean tokenExists = blacklistedTokenRepository.existsByEmailAndToken(email, token);
		if (tokenExists) {
			logger.warn("Token already blacklisted for email: {}", email);
			return false; // Token already exists
		}

		// Save new blacklisted token entry
		Long expiryDate = System.currentTimeMillis() + tokenValidity;

		BlacklistedToken blacklistedToken = new BlacklistedToken();
		blacklistedToken.setEmail(email);
		blacklistedToken.setToken(token);
		blacklistedToken.setExpiryDate(expiryDate);

		blacklistedTokenRepository.save(blacklistedToken);
		logger.info("Token successfully blacklisted for email: {}", email);
		return true;
	}

}