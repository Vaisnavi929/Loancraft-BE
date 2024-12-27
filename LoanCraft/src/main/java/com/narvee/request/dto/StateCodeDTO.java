package com.narvee.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateCodeDTO {
	private String stateCode;
	private String state;
	private Long prefixMin;
	private Long prefixMax;

}
