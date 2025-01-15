package com.narvee.request.dto;

import java.util.List;

import com.narvee.entity.IdentityProof;
import com.narvee.entity.Reference;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationDto {

	private String applicationReferenceId;
//	private Long amaxnum;

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

	@NotNull(message = "Loan amount is required")
	@Positive(message = "Loan amount must be a positive value")
	private Double loanAmount;

	@NotBlank(message = "Repayment mode cannot be blank")
	private String repaymentMode;

	@NotBlank(message = "Loan tenure cannot be blank")
	private String loanTenure;

	@NotBlank(message = "Customer ID cannot be blank")
	private String customerId;

	@NotNull(message = "Reference set is required")
	@Size(min = 1, message = "At least one reference is required")
	@Valid
	private List<Reference> reference;

	@NotNull(message = "Identity proof is required")
	@Valid
	private IdentityProof identityProof;

}
