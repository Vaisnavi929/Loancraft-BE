package com.narvee.request.dto;

import java.util.Set;

import com.narvee.entity.IdentityProof;
import com.narvee.entity.Reference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationDto {

	private String applicationReferenceId;
//	private Long amaxnum;
	private String status;
	private String employmentType;
	private Double salary;
	private Long yearsinEmployment;
	private String companyName;
	private String companyAddress;
	private String pinCode;
	private Double loanAmount;
	private String repaymentMode;
	private String loanTenure;
	private String customerId;
	private Set<Reference> reference;
	private IdentityProof identityProof;

}
