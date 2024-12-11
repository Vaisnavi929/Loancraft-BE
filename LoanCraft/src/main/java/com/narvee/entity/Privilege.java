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
import lombok.NoArgsConstructor;

@Entity
@Table(name = "privilege")
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler" })
@Data
public class Privilege extends AuditModel{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "type")
	private String type;

	@Column(name = "description")
	private String description;

	@Column(name = "createdby", insertable = true, updatable = false)
	private Long createdBy;

	@Column(name = "updatedby", insertable = true)
	private Long updatedBy;

	private boolean selected;
	
	@Column(name = "cardType")
	private String cardType ="one";  //one/many

	public enum privilegeType {
		USER, ROLE, DASHBOARD
	}
	
}
