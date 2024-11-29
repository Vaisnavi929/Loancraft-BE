package com.narvee.config;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.narvee.exception.CustomAuthenticationException;
import com.narvee.exception.ErrorResponse;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			// Add authentication logic (e.g., token validation)
			filterChain.doFilter(request, response); // Continue the filter chain
		} catch (CustomAuthenticationException ex) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			ObjectMapper objectMapper = new ObjectMapper();
			ErrorResponse errorResponse = new ErrorResponse("Authentication error: " + ex.getMessage());
			objectMapper.writeValue(response.getWriter(), errorResponse);
		} catch (Exception ex) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setContentType("application/json");
			ObjectMapper objectMapper = new ObjectMapper();
			ErrorResponse errorResponse = new ErrorResponse("Internal server error: " + ex.getMessage());
			objectMapper.writeValue(response.getWriter(), errorResponse);
		}
	}
}