package com.narvee.request.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {

	private Long userId;

	@NotEmpty
	@Size(min = 4, message = "Name must be min of 4 characters !!")
	private String name;

	@Email(message = "Email address is not valid !!")
	@NotEmpty(message = "Email is required !!")
	private String email;

	@NotEmpty
//	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$", message = "Password at least 6 characters long and include a letter, a digit, and a special character")
	private String password;
	
	private Long roleId;
	
	
	private String mobileNumber;
	
	
	
	private Long addedBy;
	
	private Long updatedBy;

	

}
