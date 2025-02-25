package com.narvee.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
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
	@Column(name = "identity_id")
	private Long identityId;

	@Column(name = "proof")
	@NotBlank(message = "Proof type cannot be blank")
	private String proof;

	@Column(name = "number")
	@NotBlank(message = "Identity number cannot be blank")
	private String number;

	@Column(name = "issue_date")
	private Date issueDate;

	@Column(name = "exp_date")
	private Date expDate;

}
