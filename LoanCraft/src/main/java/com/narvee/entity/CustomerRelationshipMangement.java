package com.narvee.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "LmsCrm")
public class CustomerRelationshipMangement {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long crmId;
	private String customerName;
	private String moblieNumber;
	private String email;
	
	
	
}
