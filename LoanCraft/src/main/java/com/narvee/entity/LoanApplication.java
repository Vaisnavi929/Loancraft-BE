package com.narvee.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LmsLoanApplication")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long loanId;
	private String applicationReferenceId;
	private Long amaxnum;
	private String title;
	private String fullname;
	private String fatherName;
	private Date dateOfBirth;
	private String gender;
	private String maritalStatus;
	private String mailId;
	private String higherQualification;
	private Long noOfDependents;
	private String category;
	private String religion;
	private String panCardNumber;
	private String aadharNumber;
	private String passportNumber;
	private String voterIdNumber;
	private String drivingLicenseNumber;
	private String gstNumber;
	private String mobileNumber;
	private String status;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cAddressId")
	private CurrentAddress currentAddress;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "pAddressId")
	private PermanentAddress permanentAddress;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "bankid")
	private BankingDetails bankingDetails;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "loanApplicantid")
	private DetailsOfLoanAppliedFor detailsOfLoanAppliedFor;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "empid")
	private EmploymentDetails employmentDetails;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "incomeid")
	private IncomeDetails incomeDetails;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "loanSactionId")
	private LoanSanction loanSanction;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "loanApplication")
	private List<PropertyDetails> propertyDetails;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "loanApplication")
	private List<Reference> references;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "loanApplication")
	private List<ExistingLoanDetails> existingLoanDetails;

}
