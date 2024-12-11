package com.narvee.serviceimpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.narvee.Service.IPrivilegeService;
import com.narvee.entity.Privilege;
import com.narvee.entity.Roles;
import com.narvee.repository.IPrivilegeRepository;
import com.narvee.repository.IRolesRepository;
import com.narvee.vo.DropdownVO;
import com.narvee.vo.PrivilegeVO;
import com.narvee.vo.RoleToPrivilegesVO;

@Service
public class PrivilegeSeviceImpl implements IPrivilegeService{

	@Autowired
	private IRolesRepository roleRepo;
	
	@Autowired
	private IPrivilegeRepository privRepo;
	
	@Override
	public Privilege savePrevileges(Privilege privilege) {
		return privRepo.save(privilege);
	}
	
	@Override
	public List<Privilege> allprev() {
		return privRepo.findAll();
	}

	@Override
	public PrivilegeVO getAllPrivileges() {

		PrivilegeVO privilegeVO = new PrivilegeVO();

		List<DropdownVO> users = new ArrayList<DropdownVO>();

		List<DropdownVO> roles = new ArrayList<DropdownVO>();

		List<DropdownVO> dashboard = new ArrayList<DropdownVO>();


		List<Privilege> privileges = privRepo.findAll();

		for (Privilege singlePrivilege : privileges) {

			// USER
			if (singlePrivilege.getType()
					.equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.USER.name())) {
				users.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setUser(users);
			}

			//Role
			if (singlePrivilege.getType()
					.equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.ROLE.name())) {
				roles.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setRole(roles);
			}

			//Dashboard
			if (singlePrivilege.getType()
					.equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.DASHBOARD.name())) {
				dashboard.add(new DropdownVO(singlePrivilege.getId(), singlePrivilege.getName(),
						singlePrivilege.getCardType()));
				privilegeVO.setDashboard(dashboard);
			}
		}

		return privilegeVO;
	}

	@Override
	public void addPrivilegeToRole(RoleToPrivilegesVO rolesPrivileges) {
		
		Optional<Roles> opt = roleRepo.findById(rolesPrivileges.getRoleId());
		Roles role = opt.get();
		System.err.println("Role details:"+role.getRoleName()+" "+role.getRoleId()+" "+role.getPrivileges());
		Set<Privilege> allPrevPrivileges = new HashSet<Privilege>();
		Privilege privlig = null;
		for (Long privilegeId : rolesPrivileges.getPrivilegeIds()) {
			Optional<Privilege> priv = privRepo.findById(privilegeId);
			privlig = priv.get();
			System.err.println("privleges data:"+privlig.getCardType()+" "+privlig.getName()+" "+privlig.getType());
			allPrevPrivileges.add(privlig);
		}
		role.setPrivileges(allPrevPrivileges);
		System.err.println("after setting priv to roles:"+role.getPrivileges());
		roleRepo.save(role);
	}
	
	@Override
	public PrivilegeVO getPrivilegesById(Long roleId) {

		PrivilegeVO privilegeVO = new PrivilegeVO();
		Optional<Roles> opt = roleRepo.findById(roleId);
		Roles roles = opt.get();

		List<DropdownVO> users = new ArrayList<DropdownVO>();

		List<DropdownVO> role = new ArrayList<DropdownVO>();

		List<DropdownVO> dashboard = new ArrayList<DropdownVO>();

		Set<Privilege> privileges = roles.getPrivileges();

		boolean flg = false;
		for (Privilege single : privileges) {

			if (single.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.ROLE.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				role.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

			if (single.getType().equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.USER.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				users.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

			if (single.getType()
					.equalsIgnoreCase(com.narvee.entity.Privilege.privilegeType.DASHBOARD.name())) {
				if (single.getId() != null || single.getId() != 0) {
					flg = true;
				}
				dashboard.add(new DropdownVO(single.getId(), single.getName(), flg, single.getCardType()));
			}

		}
		
		privilegeVO.setUser(users);

		privilegeVO.setRole(role);
		
		privilegeVO.setDashboard(dashboard);

		return privilegeVO;
	}
	
}
