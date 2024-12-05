package com.narvee.request.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankingDetailsDto {

	private String accountHolder;
	private String branchName;
	private Long accountNo;
	private String accountType;
	private Date dateOfOpening;
	private String ifscCode;

}
