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

import com.narvee.config.UserAuthService;
import com.narvee.repository.BlacklistedTokenRepository;
import com.narvee.util.JwtUtil;

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
	private UserAuthService userAuthService;

	@Autowired
	private BlacklistedTokenRepository blacklistedTokenRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		logger.info("!!! inside class : JwtAuthFilter, !! method : doFilterInternal");
		String requestURI = request.getRequestURI();
		// Define the allowed API paths
		Set<String> allowedPaths = Set.of("/api/app/get", "/api/app/save", "/api/register", "/api/auth/generateToken",
				"/api/auth/login", "/api/auth/logout", "/api/forgot-password", "/swagger-ui/index.html","/api/ucrf/convertUrcfFormat");

		// Check if the request path is in the allowed paths
		if (allowedPaths.contains(requestURI)) {
			filterChain.doFilter(request, response);
			return;
		}

		// Extract token from Authorization header
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7); // Extract token part

			try {
				username = jwtutil.extractUsername(token);
				// Check if token is blacklisted
				if (token != null && blacklistedTokenRepository.existsByToken(token)) {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.setContentType("application/json");
					String jsonResponse = "{\"error\":\"\"Your session has expired or is invalid. Please log in again to continue.\"}";
					response.getWriter().write(jsonResponse);
					return;
				}

				// Validate the token and set authentication
				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					UserDetails userDetails = userAuthService.loadUserByUsername(username);

					if (jwtutil.validateToken(token, userDetails)) {
						UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authToken);
					}
				}
			} catch (Exception e) {
				// Handle token parsing errors
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Invalid token.");
				return;
			}
		}

		filterChain.doFilter(request, response);
	}

}