package com.narvee.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.narvee.entity.UserInfo;

@Repository
public interface UserRepository extends JpaRepository<UserInfo,Long> {
	
	Optional<UserInfo> findByEmail(String email);
	
	Optional<UserInfo> findByName(String name);
	
	@Query(value = "SELECT email,name FROM lms_user_info ", nativeQuery = true)
    Page<UserInfo> findAllUsers(Pageable pageable);

	@Query(value = "SELECT email,name FROM lms_user_info"
			+ "email LIKE CONCAT('%', :keyword, '%') OR "
			+ "name LIKE CONCAT('%', :keyword, '%') OR " 
			, nativeQuery = true)
	Page<UserInfo> findUsersByKeyword(@Param("keyword") String keyword, Pageable pageable);
	
}


