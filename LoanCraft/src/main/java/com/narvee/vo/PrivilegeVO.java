package com.narvee.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class PrivilegeVO {

		private List<DropdownVO> user; //
		
		private List<DropdownVO> role;

		private List<DropdownVO> dashboard;
		
}
