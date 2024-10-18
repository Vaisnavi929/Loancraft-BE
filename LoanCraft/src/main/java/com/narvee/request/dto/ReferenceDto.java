package com.narvee.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReferenceDto {
	
	private String initial;
	private String fullName;
	private int knownForYears;  
    private String relationship;
    private Long pincode;
    private String mobilenumber;


}
