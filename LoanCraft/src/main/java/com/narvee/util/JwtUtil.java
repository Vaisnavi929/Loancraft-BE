package com.narvee.util;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {


	@Value("${jwt.secret}")
	private String jwtSecret;

	private SecretKey getSigningKey() {
		byte[] decodedKey = Base64.getDecoder().decode(jwtSecret);
		return Keys.hmacShaKeyFor(decodedKey);
	}

	public String generateToken(String username) {
		return Jwts.builder().setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5))
				.signWith(getSigningKey(), SignatureAlgorithm.HS512).compact();
	}



	public String extractUsername(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException e) {
			throw new RuntimeException("Token has expired");
		} catch (JwtException e) {
			throw new RuntimeException("Invalid token");
		}
	}
}