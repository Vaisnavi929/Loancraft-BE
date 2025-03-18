package com.narvee.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.narvee.commons.AuditModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "LmsRoles")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Roles extends AuditModel {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "roleId")
	private Long roleId;

	@Column(name = "roleName")
	private String roleName;

	@Column(name = "description")
	private String description;

	@Column(name = "addedby")
	private String addedBy;

	@Column(name = "updatedby")
	private String updatedBy;;
	
//	@JsonIgnore
//	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
//	@JoinTable(name = "lms_role_privilege", joinColumns = {
//			@JoinColumn(name = "role_id", nullable = false, updatable = false, insertable = false) }, inverseJoinColumns = {
//					@JoinColumn(name = "privilege_id", nullable = false, updatable = false, insertable = false) })
//	private Set<Privilege> privileges = new HashSet<Privilege>();
	
//	@JsonIgnore
//	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
//	@JoinTable(name = "lms_role_privilege", joinColumns = {
//			@JoinColumn(name = "role_id", nullable = false) }, inverseJoinColumns = {
//					@JoinColumn(name = "privilege_id", nullable = false) })
//	private Set<Privilege> privileges = new HashSet<Privilege>();
//	
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "lms_role_privilege", joinColumns = {
			@JoinColumn(name = "role_id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "privilege_id", nullable = false, updatable = false, insertable = false) })
	private Set<Privilege> privileges = new HashSet<Privilege>();


}
