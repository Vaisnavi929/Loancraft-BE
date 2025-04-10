package com.narvee.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import com.narvee.entity.Roles;
import com.narvee.request.dto.RolesDTO;
@EnableJpaRepositories
public interface IRolesRepository extends JpaRepository<Roles, Serializable> {

	@Query(value = "select role_name from lms_roles WHERE role_name=:roleName", nativeQuery = true)
	public List<String> findRoleByRolName(@Param("roleName") String roleName);

	public Optional<Roles> findByRoleNameAndRoleIdNot(String rolename, Long id);

	public Optional<Roles> findByRoleNameIgnoreCase(String rolename);

	@Query(value = "SELECT r.role_id,r.role_name,r.description,uf.name AS addedby,ufu.name AS updatedby, DATE(r.createddate) AS createddate, \r\n"
			+ "DATE(r.updateddate) AS updateddate  FROM lms_roles r LEFT JOIN lms_user_info uf ON uf.user_id = r.addedby\r\n"
			+ "LEFT JOIN lms_user_info ufu ON ufu.user_id = r.updatedby", nativeQuery = true)
	public Page<RolesDTO> getAllRoles(Pageable pageable);
	
	@Query(value = "SELECT \r\n"
			+ "    r.role_Id,\r\n"
			+ "    r.role_Name,\r\n"
			+ "    r.description,\r\n"
			+ "    uf.name AS addedby,\r\n"
			+ "    ufu.name AS updatedby,\r\n"
			+ "    r.createddate::DATE AS createddate,\r\n"
			+ "    r.updateddate::DATE AS updateddate  \r\n"
			+ "FROM lms_roles r\r\n"
			+ "LEFT JOIN lms_user_info uf ON uf.user_id = r.addedby\r\n"
			+ "LEFT JOIN lms_user_info ufu ON ufu.user_id = r.updatedby\r\n"
			+ "WHERE \r\n"
			+ "    r.role_Name ILIKE '%' || :keyword || '%'\r\n"
			+ "    OR r.createddate::TEXT ILIKE '%' || :keyword || '%'\r\n"
			+ "    OR r.description ILIKE '%' || :keyword || '%'\r\n"
			+ "    OR uf.name ILIKE '%' || :keyword || '%'\r\n"
			+ "    OR ufu.name ILIKE '%' || :keyword || '%' "
			+ "", nativeQuery = true)
	public Page<RolesDTO> getAllRolesWithFilter(Pageable pageable, String keyword );

}
