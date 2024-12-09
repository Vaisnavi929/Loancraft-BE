package com.narvee.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoleToPrivilegesVO {

	private Long roleId;
	private List<Long> privilegeIds;
	
}
