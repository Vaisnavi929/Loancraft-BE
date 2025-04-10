package com.narvee.filter;

import java.io.IOException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.narvee.config.UserInfoAuthService;
import com.narvee.repository.BlacklistedTokenRepository;
import com.narvee.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	public static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

	@Autowired
	private JwtUtil jwtutil;

	@Autowired
	private UserInfoAuthService userAuthService;

	@Autowired
	private BlacklistedTokenRepository blacklistedTokenRepository;


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.info("Inside JwtAuthFilter -> doFilterInternal()");
		
		String authHeader1 = request.getHeader("Authorization");
		System.out.println("DDDDD"+authHeader1);
		
		String requestURI = request.getRequestURI();

		// Define API paths that do not require authentication
		Set<String> allowedPaths = Set.of("/api/app/get", "/api/app/save", "/auth/customer/register",
				"/auth/emp/register", "/api/Emp/login", "/api/Cust/login", "/api/auth/logout", "/api/forgot-password",
				"/swagger-ui/index.html", "/api/ucrf/convertUrcfFormat");

		if (allowedPaths.contains(requestURI) || requestURI.startsWith("/api/")) {
			filterChain.doFilter(request, response);
			return;
		}
		// Extract token from Authorization header
		String authHeader = request.getHeader("Authorization");
		System.out.println("yy"+authHeader);
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authHeader.substring(7); // Remove "Bearer " prefix

		try {
			String username = jwtutil.extractUsername(token);

			// Check if token is blacklisted (logout scenario)
			if (blacklistedTokenRepository.existsByToken(token)) {
				sendErrorResponse(response, "Your session has expired or is invalid. Please log in again.");
				return;
			}

			// Validate the token and set authentication
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userAuthService.loadUserByUsername(username);

				if (jwtutil.validateToken(token)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
		} catch (ExpiredJwtException e) {
			sendErrorResponse(response, "Token has expired. Please log in again.");
			return;
		} catch (JwtException e) {
			sendErrorResponse(response, "Invalid token. Please provide a valid token.");
			return;
		} catch (Exception e) {
			logger.error("Unexpected error in JwtAuthFilter: ", e);
			sendErrorResponse(response, "An error occurred during authentication.");
			return;
		}

		filterChain.doFilter(request, response);
	}

	// Helper method to send JSON error response
	private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.getWriter().write("{\"error\": \"" + message + "\"}");
	}

}