package com.narvee.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.Service.IRolesService;
import com.narvee.entity.Roles;
import com.narvee.entity.UserInfo;
import com.narvee.repository.IRolesRepository;
import com.narvee.repository.UserRepository;
import com.narvee.vo.GetRoles;

@Service
public class RolesServiceImpl implements IRolesService{

	@Autowired
	private IRolesRepository iRoleRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Override
	public boolean saveRole(Roles role) {
		List<String> finaAllRolByRolName = iRoleRepo.findRoleByRolName(role.getRoleName().toLowerCase());
		if ((finaAllRolByRolName == null || finaAllRolByRolName.isEmpty())) {
			Roles saveRole = iRoleRepo.save(role);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public List<Roles> getAllRoles() {
		return iRoleRepo.findAll();
	}
	
	@Override
	public Roles getRole(Long id) {
		return iRoleRepo.findById(id).get();
	}
	
	public boolean updateRole(Roles role) {
		
		List<String> finaAllRolByRolName = iRoleRepo.findRoleByRolName(role.getRoleName().toLowerCase());
		Optional<Roles> roles = iRoleRepo.findByRoleNameAndRoleIdNot(role.getRoleName(), role.getRoleId());
		if ((roles == null || !roles.isPresent())) {
			Roles saveRole = iRoleRepo.save(role);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean deleteRole(Long id) {
		Roles role = iRoleRepo.findById(id).get();
		if (!(role == null)) {
			iRoleRepo.deleteById(id);
			return true;
		} else {
			return false;
		}
	}
	
}
