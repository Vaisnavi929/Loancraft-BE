package com.narvee.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.narvee.entity.Roles;
@Repository
public interface RolesRepository extends JpaRepository<Roles, Serializable>{
void deleteByRoleId(Long roleId);
}
