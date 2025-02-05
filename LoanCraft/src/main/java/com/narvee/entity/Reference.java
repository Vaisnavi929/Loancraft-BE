package com.narvee.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "LmsReference")
@AllArgsConstructor
@NoArgsConstructor
public class Reference {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reference_id")
	private Long referenceId;


	@Column(name = "initial")
	private String initial;

	@Column(name = "full_name")
	@NotBlank(message = "Full name cannot be blank")
	private String fullName;

	@Column(name = "known_for_years")
	@NotNull(message = "Known for years is required")
	@PositiveOrZero(message = "Known for years cannot be negative")
	private Long knownForYears;

	@Column(name = "relationship")
	@NotBlank(message = "Relationship cannot be blank")
	private String relationship;


	@Column(name = "pincode")
	@NotNull(message = "Pincode is required")
	@Digits(integer = 6, fraction = 0, message = "Pincode must be a 6-digit number")
	@Min(value = 100000, message = "Pincode must be a valid 6-digit number")
	private Long pincode;

	@Column(name = "mobilenumber")
	@NotNull(message = "Mobile number is required")
	@Digits(integer = 10, fraction = 0, message = "Mobile number must be a 10-digit number")
	@Min(value = 1000000000, message = "Mobile number must be a valid 10-digit number")
	private Long mobilenumber;


}
