package com.narvee.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SortingDTO {

	private Integer pageNumber;
	private Integer pageSize;
	private String sortOrder;
	private String sortField;
	private String keyword;

}