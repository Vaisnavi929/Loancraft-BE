package com.narvee.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermanentAddressDto {
	
	private String address;
	private String city;
	private String state;
	private String country;
	private String location;
	private String landmark;
	private Long stayedOfYears;
	private Long pincode;
	private Long statecode;

}
