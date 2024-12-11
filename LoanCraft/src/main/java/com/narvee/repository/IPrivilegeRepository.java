package com.narvee.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narvee.entity.Privilege;

public interface IPrivilegeRepository extends JpaRepository<Privilege, Serializable> {

}
