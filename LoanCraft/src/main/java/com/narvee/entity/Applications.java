package com.narvee.entity;

import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LmsApplications")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Applications {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long loanId;
	private String applicationReferenceId;
	private Long amaxnum;
	private String status = "Applied";
	private String employmentType;
	private Double salary;
	private Long yearsinEmployment;
	private String companyName;
	private String companyAddress;
	private String pinCode;
	private Double loanAmount;
	private String repaymentMode;
	private String loanTenure;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "customerId")
	private Customers customers;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "referenceId")
	private Set<Reference> reference;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "identityId")
	private IdentityProof identityProof;

}
