package com.narvee.request.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckDepositEmiDropdown {
	
	RepaymentDropdown dropdown;
	List<Map<String, Object>> requests ;

}
