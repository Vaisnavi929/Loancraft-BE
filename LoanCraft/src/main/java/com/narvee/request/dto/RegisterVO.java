package com.narvee.request.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterVO {

	@NotEmpty
	@Size(min = 4, message = "FirstName must be min of 4 characters !!")
	private String firstName;

	@NotEmpty
	@Size(min = 4, message = "FirstName must be min of 4 characters !!")
	private String lastName;

	@NotEmpty(message = "Mobile number is required")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
	private String mobileNo;

	@Email(message = "Email address is not valid !!")
	@NotEmpty(message = "Email is required !!")
	private String email;

	@NotEmpty
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$", message = "Password at least 6 characters long and include a letter, a digit, and a special character")
	private String password;

}
