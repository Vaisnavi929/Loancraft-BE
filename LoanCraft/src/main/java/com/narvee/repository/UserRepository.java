package com.narvee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.narvee.entity.UserInfo;

@Repository
public interface UserRepository extends JpaRepository<UserInfo,Integer> {
	
	Optional<UserInfo> findByEmail(String email);
	
	Optional<UserInfo> findByName(String name);
}


