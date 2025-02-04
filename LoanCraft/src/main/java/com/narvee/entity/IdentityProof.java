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

	@Column(name = "document")
	@NotBlank(message = "Document name cannot be blank")
	private String document;

	@Column(name = "number")
	@NotBlank(message = "Identity number cannot be blank")
	private String number;

	@Column(name = "issue_date")
	@NotNull(message = "Issue date is required")
	@PastOrPresent(message = "Issue date must be in the past or present")
	private Date issueDate;

	@Column(name = "exp_date")
	@NotNull(message = "Expiration date is required")
	@Future(message = "Expiration date must be in the future")
	private Date expDate;

}
