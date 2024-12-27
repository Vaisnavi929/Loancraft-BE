package com.narvee.Service;

import java.util.List;

import com.narvee.entity.Privilege;
import com.narvee.request.dto.PrivilegedDTO;
import com.narvee.vo.PrivilegeVO;
import com.narvee.vo.RoleToPrivilegesVO;

public interface IPrivilegeService {

	public Privilege savePrevileges(PrivilegedDTO privilege);
	
	public List<Privilege> allprev();
	
	public PrivilegeVO getAllPrivileges();
	
	public void addPrivilegeToRole(RoleToPrivilegesVO rolesPrivileges);
	
	public PrivilegeVO getPrivilegesById(Long roleId);
	
}
