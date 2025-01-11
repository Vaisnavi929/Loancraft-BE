package com.narvee.request.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

	@NotBlank(message = "Role name cannot be empty!")
	private String roleName;;

}
