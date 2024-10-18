package com.narvee.entity;

import com.narvee.commons.AuditModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "LmsUserRoles")
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity  extends AuditModel{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	
	@Column(name = "RoleId")
  private Long roleId;
	
	@Column(name ="RoleName")
   private String roleName;
}

