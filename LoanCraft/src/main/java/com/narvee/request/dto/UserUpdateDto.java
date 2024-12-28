package com.narvee.request.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {

	private String userId;
	private String name;
	private String email;

	private List<RoleDto> roles;

}
