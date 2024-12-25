package com.narvee.entity;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "IdentityProof")
@AllArgsConstructor
@NoArgsConstructor
public class IdentityProof {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long identityId;
	private String proof;
	private String document;
	private String number;
	private Date issueDate;
	private Date expDate;
	
	

}
