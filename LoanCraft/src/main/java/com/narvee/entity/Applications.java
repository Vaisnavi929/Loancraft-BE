package com.narvee.entity;

import java.util.List;
import java.util.Set;

import com.narvee.commons.AuditModel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LmsApplications")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Applications extends AuditModel {
	private static final long serialVersionUID = 1L;

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
	
	@Column(name = "updatedby")
	private String updatedBy;

	@ManyToOne
	@JoinColumn(name = "customerId")
	private CustomersInfo customers;


	@OneToMany
	@JoinColumn(name = "loanId")
	private List<Reference> reference;


	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "identity_id")
	private IdentityProof identityProof;

}
