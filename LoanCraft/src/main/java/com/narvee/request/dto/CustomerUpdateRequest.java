package com.narvee.request.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CustomerUpdateRequest {

	private String customerId;
	private String firstName;
	private String middleName;
	private String lastName;
	private String mobileNo;
	private String email;
	private String gender;
	private String fathersOrSpousesName;
	private LocalDate dob;
	private String highestQualification;
	private String adhaarNumber;
	
	
	
	

}
