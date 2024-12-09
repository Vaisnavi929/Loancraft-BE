package com.narvee.request.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdentityDto {

	private String proof;
	private String document;
	private String number;
	private Date issueDate;
	private Date expDate;
}
