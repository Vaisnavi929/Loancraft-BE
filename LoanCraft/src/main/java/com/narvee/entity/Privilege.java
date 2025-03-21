package com.narvee.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.narvee.commons.AuditModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lms_privilege")
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler" })
@Data
@EqualsAndHashCode(callSuper = true)
public class Privilege extends AuditModel{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "type")
	private String type;

	@Column(name = "cardType")
	private String cardType ="one";  //one/many
	
	@Column(name = "description")
	private String description;

	@Column(name = "createdby", insertable = true, updatable = false)
	private Long createdBy;

	@Column(name = "updatedby", insertable = true)
	private Long updatedBy;

	private boolean selected;
	
	public enum privilegeType {

		USER, ROLES, DASHBOARD, HOME, HRM, CUSTOMER_INFO, CUSTOMER_DASHBOARD,APPLICATIONS

	}
	
}
