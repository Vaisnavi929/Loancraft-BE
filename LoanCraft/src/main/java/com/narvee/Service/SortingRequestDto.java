package com.narvee.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortingRequestDto {
		
private Integer pageNumber;
private Integer pageSize;
private String sortField;
private String sortOrder;
private String keyword;
}
