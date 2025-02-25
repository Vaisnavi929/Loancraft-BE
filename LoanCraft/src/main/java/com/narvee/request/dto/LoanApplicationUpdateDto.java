package com.narvee.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationUpdateDto {

//	@NotBlank(message = "loanId cannot be blank")
	private Long loanId;

	@NotBlank(message = "Status cannot be blank")
	private String status;

	@NotBlank(message = "Employment type cannot be blank")
	private String employmentType;

	@NotNull(message = "Salary is required")
	@Positive(message = "Salary must be a positive value")
	private Double salary;

	@NotNull(message = "Years in employment is required")
	@PositiveOrZero(message = "Years in employment cannot be negative")
	private Long yearsinEmployment;

	@NotBlank(message = "Company name cannot be blank")
	private String companyName;

	@NotBlank(message = "Company address cannot be blank")
	private String companyAddress;

	@NotBlank(message = "Pin code cannot be blank")
	@Pattern(regexp = "\\d{6}", message = "Pin code must be a 6-digit number")
	private String pinCode;

	private String customerId;

}
