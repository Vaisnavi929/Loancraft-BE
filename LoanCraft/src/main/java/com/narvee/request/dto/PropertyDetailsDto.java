package com.narvee.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDetailsDto {
	
	private String ownerName;
	private String propertyAddress;
    private Double plotArea;
    private Double builtUpArea;
    private Double buildingAge;

}
