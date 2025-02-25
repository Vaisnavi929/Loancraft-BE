package com.narvee.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.Roles;

public interface IRolesRepository extends JpaRepository<Roles, Serializable> {

	@Query(value = "select role_name from lms_roles WHERE role_name=:roleName", nativeQuery = true)
	public List<String> findRoleByRolName(@Param("roleName") String roleName);

	public Optional<Roles> findByRoleNameAndRoleIdNot(String rolename, Long id);

	public Optional<Roles> findByRoleNameIgnoreCase(String rolename);

	@Query(value = "SELECT r.role_Id,r.role_Name,r.description ,uf.name as addedby ,ufu.name as updatedby, date(r.createddate) as createddate ,date(r.updateddate) as updateddate  FROM lms_roles  r "
			+ "    left join lms_user_info  uf on  uf.user_id=r.addedby"
			+ "    left join lms_user_info  ufu on  ufu.user_id=r.updatedby", nativeQuery = true)
	public Page<RolesDTO> getAllRoles(Pageable pageable);
	
	@Query(value = "SELECT r.role_Id,r.role_Name,r.description ,uf.name as addedby ,ufu.name as updatedby, date(r.createddate) as createddate ,date(r.updateddate) as updateddate  FROM lms_roles  r "
			+ "    left join lms_user_info  uf on  uf.user_id=r.addedby "
			+ "    left join lms_user_info  ufu on  ufu.user_id=r.updatedby WHERE  r.role_Name LIKE CONCAT('%',:keyword, '%')  OR date(r.createddate) LIKE CONCAT('%',:keyword, '%')  OR r.description LIKE CONCAT('%',:keyword, '%') "
			+ "    OR uf.name LIKE CONCAT('%',:keyword, '%') OR ufu.name LIKE CONCAT('%',:keyword, '%')", nativeQuery = true)
	public Page<RolesDTO> getAllRolesWithFilter(Pageable pageable, String keyword );

}
