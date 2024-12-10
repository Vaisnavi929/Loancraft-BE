package com.narvee.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.catalina.User;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.narvee.commons.AuditModel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "LmsRoles")
@NoArgsConstructor
@AllArgsConstructor
public class Roles extends AuditModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "roleId")
	private Long roleId;

//	@Column(name = "id")
//	private Long id;	
	
	@Column(name = "roleName")
	private String roleName;

//	@JsonBackReference
//	@ManyToMany(mappedBy = "roles")
//    private Set<UserInfo> users = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "role_privilege", joinColumns = {
			@JoinColumn(name = "role_id", nullable = false, updatable = false, insertable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "privilege_id", nullable = false, updatable = false, insertable = false) })
	private Set<Privilege> privileges = new HashSet<Privilege>();

}
