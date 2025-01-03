package com.narvee.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {

	private String address;
	private String city;
	private String country;
	private String location;
	private String landmark;
	private Long stayedOfYears;
	private Long pincode;
	private long pAddressId;
	private long cAddressId;
	private Long stateId;

}

