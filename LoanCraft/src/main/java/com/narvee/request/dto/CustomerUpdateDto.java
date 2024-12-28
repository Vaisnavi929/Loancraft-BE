package com.narvee.request.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateDto {
	
	private String  customerId;
	private String firstName;
	private String middleName;
	private String lastName;
	private String mobileNo;
	private String email;
	private String gender;
	private String fathersOrSpousesName;
	private Date dob;
	private String highestQualification;
	private String adhaarNumber;

}

