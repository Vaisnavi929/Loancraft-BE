package com.narvee.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.narvee.entity.UserInfo;
import com.narvee.response.UserResponseDTO;

@Repository
public interface UserRepository extends JpaRepository<UserInfo,Long> {
	
	Optional<UserInfo> findByEmail(String email);
	
	Optional<UserInfo> findByName(String name);
	
	@Query(value = "SELECT r.role_name as designation, u.user_Id, u.email,u.name, u.mobile_number,u.status,date(u.createddate) as createddate,date(u.updateddate) as updateddate,uf.name as addedby  FROM lms_user_info u "
			+ "left join lms_user_info uf on   u.addedby=uf.user_id "
			+ "join lms_user_roles  ur on ur.user_id=u.user_Id "
			+ "join lms_roles r  on r.role_id=ur.role_id", nativeQuery = true)
    Page<UserResponseDTO> findAllUsers(Pageable pageable);

	@Query(value = "SELECT r.role_name as designation, u.user_Id, u.email,u.name, u.mobile_number,u.status,date(u.createddate) as createddate,date(u.updateddate) as updateddate,uf.name as addedby,r.role_name as designation  FROM lms_user_info u "
			+ "	left join lms_user_info uf on   u.addedby=uf.user_id "
			+ "	join lms_user_roles  ur on ur.user_id=u.user_Id "
			+ "	join lms_roles r  on r.role_id=ur.role_id where "
			+ "u.email LIKE CONCAT('%', :keyword, '%') OR "
			+ "u.name LIKE CONCAT('%', :keyword, '%') OR u.mobile_number LIKE CONCAT('%', :keyword, '%') OR u.createddate LIKE CONCAT('%', :keyword, '%') "
			+ "OR r.role_name LIKE CONCAT('%', :keyword, '%') OR u.status LIKE CONCAT( :keyword, '%') OR u.updateddate LIKE CONCAT('%', :keyword, '%')" 
			, nativeQuery = true)
	Page<UserResponseDTO> findUsersByKeyword(@Param("keyword") String keyword, Pageable pageable);
	
}


