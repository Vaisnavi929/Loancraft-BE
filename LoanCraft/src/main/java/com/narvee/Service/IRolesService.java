package com.narvee.Service;

import java.util.List;

import com.narvee.entity.Roles;
import com.narvee.vo.GetRoles;

public interface IRolesService {
	
	public boolean saveRole(Roles role);
	
	public List<Roles> getAllRoles();
	
	public Roles getRole(Long id);
	
	public boolean updateRole(Roles role);
	
	public boolean deleteRole(Long id);
	
//	public List<GetRoles> getRoles();

}
