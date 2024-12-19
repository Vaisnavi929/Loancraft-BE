package com.narvee.request.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationDto {

	private String title;
	private String fullname;
	private String firstName;
	private String middileName;	
	private String lastname;
	private String fatherName;
	private Date dateOfBirth;
	private String mailId;
	private String higherQualification;
	private String aadharNumber;
	private String mobileNumber;
	private String status;
    
}
