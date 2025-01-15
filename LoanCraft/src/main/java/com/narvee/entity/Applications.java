package com.narvee.entity;

import java.util.List;
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
	
	@Column(name = "application_reference_id")
	private String applicationReferenceId;
	
	@Column(name = "amaxnum")
	private Long amaxnum;
	
	private String status = "Applied";
	
	@Column(name = "employment_type")
	private String employmentType;
	
	@Column(name = "salary")
	private Double salary;
	
	@Column(name = "yearsin_employment")
	private Long yearsinEmployment;
	
	@Column(name = "company_name")
	private String companyName;
	
	@Column(name = "company_address")
	private String companyAddress;
	
	@Column(name = "pin_code")
	private String pinCode;
	
	@Column(name = "loan_amount")
	private Double loanAmount;
	
	@Column(name = "repayment_mode")
	private String repaymentMode;
	
	@Column(name = "loan_tenure")
	private String loanTenure;
	
	@Column(name = "updatedby")
	private String updatedBy;

	@ManyToOne
	@JoinColumn(name = "customerId")
	private CustomersInfo customers;

	@OneToMany
	@JoinColumn(name = "loan_id")
	private List<Reference> reference;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "identity_id")
	private IdentityProof identityProof;

}
