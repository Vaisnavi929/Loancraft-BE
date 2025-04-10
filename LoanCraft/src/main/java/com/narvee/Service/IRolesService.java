package com.narvee.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.narvee.entity.Roles;
import com.narvee.request.dto.RequestDTO;
import com.narvee.request.dto.RolesDTO;

public interface IRolesService {
	
	public boolean saveRole(Roles role);
	
	public Roles getRole(Long id);
	
	public boolean updateRole(Roles role);
	
	public boolean deleteRole(Long id);

	public Page<RolesDTO> getAllRoles(RequestDTO request);
	
	public List<Roles> getAllRoles();
	
//	public List<GetRoles> getRoles();

}
