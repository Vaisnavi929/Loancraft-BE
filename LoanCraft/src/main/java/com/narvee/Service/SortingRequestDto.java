package com.narvee.Service;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortingRequestDto {
		
private int pageNumber;
private int pageSize;
private String sortField;
private String sortOrder;
private String keyword;
private String status;

}
