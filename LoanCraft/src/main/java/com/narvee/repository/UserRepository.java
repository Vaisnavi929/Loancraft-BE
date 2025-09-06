package com.narvee.repository;

import java.util.List;
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
public interface UserRepository extends JpaRepository<UserInfo, Long> {

	Optional<UserInfo> findByEmail(String email);
	
	Optional<UserInfo> findByMobileNumber(String mobileNumber);
	
	Optional<UserInfo> findByName(String name);

//	@Query(value = "SELECT r.role_name as designation, u.user_Id, u.email,u.name, u.mobile_number,u.status,date(u.createddate) as createddate,date(u.updateddate) as updateddate,uf.name as addedby  FROM lms_user_info u "
//			+ "left join lms_user_info uf on   u.addedby=uf.user_id "
//			+ "join lms_user_roles  ur on ur.user_id=u.user_Id "
//			+ "join lms_roles r  on r.role_id=ur.role_id", nativeQuery = true)
//	Page<UserResponseDTO> findAllUsers(Pageable pageable);

	@Query(value = """
			SELECT 
			    r.role_name AS designation, 
			    u.user_Id, 
			    u.email,
			    u.name, 
			    u.mobile_number,
			    u.status,
			    DATE(u.createddate) AS createddate,
			    DATE(u.updateddate) AS updateddate,
			    uf.name AS addedby, 
			    uu.name AS updatedby  
			FROM lms_user_info u
			LEFT JOIN lms_user_info uf ON u.addedby = uf.user_id
			LEFT JOIN lms_user_info uu ON u.updatedby = uu.user_id 
			JOIN lms_user_roles ur ON ur.user_id = u.user_Id
			JOIN lms_roles r ON r.role_id = ur.role_id
			""", nativeQuery = true)
			Page<UserResponseDTO> findAllUsers(Pageable pageable);
	
	@Query(value = """
				SELECT
			    r.role_name AS designation,
			    u.user_id,
			    u.email,
			    u.name,
			    u.mobile_number,
			    u.status,
			    DATE(u.createddate) AS createddate,
			    DATE(u.updateddate) AS updateddate,
			    uf.name AS addedby,
			    r.role_name AS designation,
			    uu.name AS updatedby  
			FROM lms_user_info u
			LEFT JOIN lms_user_info uf ON u.addedby = uf.user_id
			LEFT JOIN lms_user_info uu ON u.updatedby = uu.user_id 
			JOIN lms_user_roles ur ON ur.user_id = u.user_id
			JOIN lms_roles r ON r.role_id = ur.role_id
			WHERE
			    u.email ILIKE CONCAT('%', :keyword, '%') OR
			    uf.name ILIKE CONCAT('%', :keyword, '%') OR
			    uu.name ILIKE CONCAT('%', :keyword, '%') OR
			    u.name ILIKE CONCAT('%', :keyword, '%') OR
			    u.mobile_number ILIKE CONCAT('%', :keyword, '%') OR
			    CAST(u.createddate::date AS TEXT) ILIKE CONCAT('%', :keyword, '%') OR
			    r.role_name ILIKE CONCAT('%', :keyword, '%') OR
			    u.status ILIKE CONCAT(:keyword, '%') OR
			    CAST(u.updateddate ::date AS TEXT) ILIKE CONCAT('%', :keyword, '%') """, nativeQuery = true)
	Page<UserResponseDTO> findUsersByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
