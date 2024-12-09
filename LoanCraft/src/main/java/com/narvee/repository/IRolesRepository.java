package com.narvee.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.CustomerDetails;
import com.narvee.entity.Roles;
import com.narvee.vo.GetRoles;

public interface IRolesRepository extends JpaRepository<Roles, Serializable>{

	@Query(value = "select role_name from lms_roles WHERE role_name=:roleName", nativeQuery = true)
	public List<String> findRoleByRolName(@Param("roleName") String roleName);
	
	public Optional<Roles> findByRoleNameAndRoleIdNot(String rolename, Long id);
	
}
