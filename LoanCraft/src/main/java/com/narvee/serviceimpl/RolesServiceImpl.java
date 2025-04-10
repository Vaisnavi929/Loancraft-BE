package com.narvee.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.narvee.Service.IRolesService;
import com.narvee.entity.Roles;
import com.narvee.repository.IRolesRepository;
import com.narvee.request.dto.RequestDTO;
import com.narvee.request.dto.RolesDTO;

@Service
public class RolesServiceImpl implements IRolesService {
	public static final Logger logger = LoggerFactory.getLogger(RolesServiceImpl.class);
	@Autowired
	private IRolesRepository iRoleRepo;

	@Override
	public boolean saveRole(Roles role) {
		logger.info("!!! inside class: RolesServiceImpl, method: saveRole");
		List<String> finaAllRolByRolName = iRoleRepo.findRoleByRolName(role.getRoleName().toLowerCase());
		if ((finaAllRolByRolName == null || finaAllRolByRolName.isEmpty())) {
			iRoleRepo.save(role);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Page<RolesDTO> getAllRoles(RequestDTO request) {
		logger.info("!!! inside class: RolesServiceImpl, method: getAllRoles");
		String sortField = request.getSortField();
		String sortOrder = request.getSortOrder();
		String keyword = request.getKeyword();
		Integer pageNumber = request.getPageNumber();
		Integer pageSize = request.getPageSize();

		if (sortField.equalsIgnoreCase("roleName"))
			sortField = "role_Name";
		else if (sortField.equalsIgnoreCase("description"))
			sortField = "description";
		else if (sortField.equalsIgnoreCase("addedby"))
			sortField = "addedby";
		else if (sortField.equalsIgnoreCase("updatedby"))
			sortField = "updatedby";
		else if (sortField.equalsIgnoreCase("createdDate"))
			sortField = "createddate";
		else
			sortField = "updateddate";

		Sort.Direction sortDirection = Sort.Direction.ASC;
		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
		}

		Sort sort = Sort.by(sortDirection, sortField);
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

		if (keyword.equalsIgnoreCase("empty")) {
			logger.info("!!! inside class: RolesServiceImpl, method: getAllRoles, empty ");
			return iRoleRepo.getAllRoles(pageable);
		} else {
			logger.info("!!! inside class: RolesServiceImpl, method: getAllRoles, filter ");
			return iRoleRepo.getAllRolesWithFilter(pageable, keyword);

		}

	}

	@Override
	public Roles getRole(Long id) {
		return iRoleRepo.findById(id).get();
	}

	public boolean updateRole(Roles role) {

		iRoleRepo.findRoleByRolName(role.getRoleName().toLowerCase());
		Optional<Roles> roles = iRoleRepo.findByRoleNameAndRoleIdNot(role.getRoleName(), role.getRoleId());
		if ((roles == null || !roles.isPresent())) {
			iRoleRepo.save(role);
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

	@Override
	public List<Roles> getAllRoles() {
		logger.info("!!! inside class: RolesServiceImpl, method: List<Roles> getAllRoles() ");
		return iRoleRepo.findAll();
	}

}
