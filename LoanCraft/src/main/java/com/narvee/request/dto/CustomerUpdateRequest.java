package com.narvee.request.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUpdateRequest {

	private String customerId;

	@NotEmpty
	@Size(min = 2, message = "FirstName must be min of 2 characters !!")
	private String firstName;

	private String middleName;
	
	@NotEmpty
	@Size(min = 4, message = "LastName must be min of 4 characters !!")
	private String lastName;


	@NotEmpty(message = "Mobile number is required")
	@Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
	private String mobileNumber;

	@Email(message = "Email address is not valid !!")
	@NotEmpty(message = "Email is required !!")
	private String email;

	@NotEmpty
	@Size(min = 5, message = "Father's or Spouse's name is required")
	private String fathersOrSpousesName;

	@Past(message = "Date of birth must be in the past")
	private LocalDate dob;

	@NotEmpty
	@Size(min = 2, message = "Highest qualification is required")
	private String highestQualification;

	@NotEmpty
	@Pattern(regexp = "Male|Female|Other", message = "Gender must be Male, Female, or Other")
	private String gender;

	@NotEmpty
	@Pattern(regexp = "^\\d{12}$", message = "Aadhaar number must be 12 digits")
	private String adhaarNumber;
	private Long addedBy;
	private Long updatedBy;
	
	
	
	


}
