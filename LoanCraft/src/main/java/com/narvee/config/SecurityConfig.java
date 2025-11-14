package com.narvee.config;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.narvee.exception.ErrorResponse;
import com.narvee.filter.JwtAuthFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	public static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

//	private static final String[] WHITE_LIST_URL = { "/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs",
//			"/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
//			"/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html", "/api/auth/**",
//			"/api/test/**", "/authenticate", "/api/auth/welcome", "/auth/users/register", "/api/auth/generateToken",
//			"/api/auth/login", "/api/auth/logout", "/api/excel/upload", "/api/app/save", "/api/app/get",
//			"/api/app/save", "/api/**", "/auth/customer/check-email/{email}", "/auth/otp/send-otp",
//			"/auth/otp/verify-otp/{otp}", "/auth/customer/save" };
//	
	private static final String[] WHITE_LIST_URL = { "/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs",
			"/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
			"/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html", "/api/auth/**",
			"/api/test/**", "/authenticate", "/api/auth/welcome", "/auth/users/register", "/api/auth/generateToken",
			"/api/auth/login", "/api/auth/logout", "/api/excel/upload", "/api/app/save", "/api/app/get",
			"/api/app/save", "/api/**", "/auth/customer/check-email/{email}", "/auth/otp/send-otp",
			"/auth/otp/verify-otp/{otp}", "/auth/customer/save" , "/api/auth/otp-login", "/api/auth/send-otp"};

	@Autowired
	private JwtAuthFilter authFilter;

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserInfoAuthService(); // Ensure UserInfoService implements UserDetailsService
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors() // <--- Enable CORS here
				.and().csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						auth -> auth.requestMatchers(WHITE_LIST_URL).permitAll().requestMatchers("/api/auth/**")
								.hasAnyAuthority("ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER_ADMIN", "ROLE_EMPLOYEE")
								.anyRequest().authenticated())
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.setContentType("application/json");
					ObjectMapper objectMapper = new ObjectMapper();
					ErrorResponse errorResponse = new ErrorResponse(
							"Authentication failed: " + authException.getMessage());
					objectMapper.writeValue(response.getWriter(), errorResponse);
				}).accessDeniedHandler((request, response, accessDeniedException) -> {
					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					response.setContentType("application/json");
					ObjectMapper objectMapper = new ObjectMapper();
					ErrorResponse errorResponse = new ErrorResponse(
							"Access denied: " + accessDeniedException.getMessage());
					objectMapper.writeValue(response.getWriter(), errorResponse);
				}));

		return http.build();
	}

    
//  @Bean
//  public CorsWebFilter corsWebFilter() {
//    CorsConfiguration config = new CorsConfiguration();
// config.setAllowCredentials(true);
// config.setAllowedOriginPatterns(Arrays.asList(
// "http://localhost:4200",
//"http://76.234.146.243",
//"http://10.10.0.200"
//));
//config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
//config.setAllowedHeaders(Arrays.asList("*"));
////
//UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//source.registerCorsConfiguration("/**", config);
//return new CorsWebFilter(source);
//}

	//need to added new one 
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins( "http://10.10.0.200") // Angular app
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                        .allowedHeaders("*");
//            }
//        };
//    }
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*");
			}
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // Password encoder used during registration and login
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;

	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}
