package com.narvee.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressUpdateRequest {
	
	private AddressDto permanentAddress;
	private AddressDto currentAddress;



}
