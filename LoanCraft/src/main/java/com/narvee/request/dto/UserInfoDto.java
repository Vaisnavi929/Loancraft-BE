package com.narvee.request.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto  {
	
	    private int id;
	    
	    private String name;
	    
	    private String email;
	   
	    private String password;
	    
	    private List<RoleDto> roles;

	}
