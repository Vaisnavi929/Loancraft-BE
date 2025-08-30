package com.narvee.request.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterVO {

	@NotEmpty
	@Size(min = 1, message = "FirstName must be min of 1 characters !!")
	private String firstName;

	@NotEmpty
	@Size(min = 1, message = "FirstName must be min of 1 characters !!")
	private String lastName;


	@NotEmpty(message = "Mobile number is required")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
	private String mobileNumber;

	private String location;
	
	private String Occupation;
	
	private String loanRequired; 
	
	@Email(message = "Email address is not valid !!")
	@NotEmpty(message = "Email is required !!")
	private String email;

	
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$", message = "Password at least 6 characters long and include a letter, a digit, and a special character")
	private String password;
	
	private Long userId;


}
