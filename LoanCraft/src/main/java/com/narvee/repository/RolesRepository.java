package com.narvee.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narvee.entity.Roles;

public interface RolesRepository extends JpaRepository<Roles, Serializable>{

}
