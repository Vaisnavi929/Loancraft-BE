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
import com.narvee.exception.RoleAssignedException;
import com.narvee.repository.IRolesRepository;
import com.narvee.repository.UserRepository;
import com.narvee.request.dto.RequestDTO;
import com.narvee.request.dto.RolesDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RolesServiceImpl implements IRolesService {
	public static final Logger logger = LoggerFactory.getLogger(RolesServiceImpl.class);
	@Autowired
	private IRolesRepository iRoleRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@PersistenceContext
    private EntityManager entityManager;

	
	
	//------Abhinay Added
	
	@Transactional
    @Override
    public boolean saveRole(Roles role) {
        logger.info("!!! inside class: RolesServiceImpl, method: saveRole");

        String incoming = role.getRoleName() == null ? "" : role.getRoleName().trim();

        if (incoming.isEmpty()) {
            return false; // or throw a validation exception
        }

        // Use case-insensitive exists check
        if (iRoleRepo.existsByRoleNameIgnoreCase(incoming)) {
            return false;
        }

        // normalize to chosen format (optional). I recommend storing as-is or trimmed.
        role.setRoleName(incoming);

        try {
            iRoleRepo.save(role);
            return true;
        } catch (DataIntegrityViolationException ex) {
            // Race condition — someone inserted same role concurrently
            logger.error("DataIntegrityViolation while saving role: " + incoming, ex);
            return false;
        }
//        Long loggedInUserId = role.getAddedBy();  // or supply from controller
//
//        role.setCreateddate(LocalDateTime.now());
//        role.setAddedBy(loggedInUserId);
//
//        // also set updated fields on first creation
//        role.setUpdatedBy(loggedInUserId);
//        role.setUpdatedDate(LocalDateTime.now());
//
//        iRoleRepo.save(role);
//        return true;
    }

    @Transactional
    @Override
    public boolean updateRole(Roles role) {
        Optional<Roles> existingRoleOpt = iRoleRepo.findById(role.getRoleId());

        if (!existingRoleOpt.isPresent()) {
            return false; // or throw ResourceNotFoundException
        }

        String incoming = role.getRoleName() == null ? "" : role.getRoleName().trim();
        if (incoming.isEmpty()) {
            return false; // invalid name
        }

        // Check if some other role (different id) already uses this name (case-insensitive)
        Optional<Roles> duplicate = iRoleRepo.findByRoleNameIgnoreCaseAndRoleIdNot(incoming, role.getRoleId());
        if (duplicate.isPresent()) {
            return false; // duplicate name exists
        }

        Roles existingRole = existingRoleOpt.get();
        existingRole.setRoleName(incoming);
        existingRole.setDescription(role.getDescription());
        
        existingRole.setUpdatedBy(role.getUpdatedBy()); 
        //existingRole.setUpdatedDate(LocalDateTime.now());

        try {
            iRoleRepo.save(existingRole);
            return true;
        } catch (DataIntegrityViolationException ex) {
            logger.error("DataIntegrityViolation while updating role: " + incoming, ex);
            return false;
        }
    }
    //-----------------
	
    /*
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

	*/
	
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

	
//	@Override
//	public boolean updateRole(Roles role) {
//
//		iRoleRepo.findRoleByRolName(role.getRoleName().toLowerCase());
//		
//		Optional<Roles> roles = iRoleRepo.findByRoleNameAndRoleIdNot(role.getRoleName(), role.getRoleId());
//		//System.err.println("roles-------------" + roles);
//		if ((roles == null || !roles.isPresent())) {
//			iRoleRepo.save(role);
//			return true;
//		} else {
//			return false;
//		}
//	}
	
//	@Override
//	public boolean updateRole(Roles role) {
//		Optional<Roles> existingRoleOpt = iRoleRepo.findById(role.getRoleId());
//		
//		if (!existingRoleOpt.isPresent()) {
//			return false; // or throw an exception
//		}
//
//		Roles existingRole = existingRoleOpt.get();
//
//		// Check if name is being updated and if it's unique
//		Optional<Roles> duplicateCheck = iRoleRepo.findByRoleNameAndRoleIdNot(role.getRoleName(), role.getRoleId());
//
//		if (duplicateCheck.isPresent()) {
//			return false; // Duplicate name
//		}
//
//		// Update only what needs to be updated, don't touch privileges
//		existingRole.setRoleName(role.getRoleName());
//		existingRole.setDescription(role.getDescription());
//
//		iRoleRepo.save(existingRole);
//		return true;
//	}
	
	//abhinay added
	
	@Override
	@Transactional
	public boolean deleteRole(Long id) {
	    if (id == null) {
	        return false;
	    }

	    Optional<Roles> opt = iRoleRepo.findById(id);
	    if (!opt.isPresent()) {
	        return false; 
	    }

	    // Check if any user has this role (many-to-many: users.roles)
	    boolean assigned = userRepo.existsByRoles_RoleId(id);
	    if (assigned) {
	        // preferred: throw so controller/global handler can produce 409 CONFLICT + message
	        throw new RoleAssignedException("Role name is assigned to user(s) so can't be deleted");
	        // If you prefer boolean-only flow, use: return false;
	    }

	    // safe to delete
	    iRoleRepo.deleteById(id);
	    return true;
	}
	
	
//	@Override
//	public boolean deleteRole(Long id) {
//		Roles role = iRoleRepo.findById(id).get();	
//		if (!(role == null)) {
//			iRoleRepo.deleteById(id);
//			return true;
//		} else {
//			return false;
//		}
//		
//	}

	@Override
	public List<Roles> getAllRoles() {
		logger.info("!!! inside class: RolesServiceImpl, method: List<Roles> getAllRoles() ");
		return iRoleRepo.findAll();
	}

}
