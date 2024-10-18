package com.narvee.entity;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
	private String noOfDependents;
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
    private CurrentAddress currentAddress;
	
	@OneToOne(cascade = CascadeType.ALL)
    private PermanentAddress permanentAddress;
	
	@OneToOne(cascade = CascadeType.ALL)
	private BankingDetails bankingDetails;
	
	@OneToMany(cascade =CascadeType.ALL)
	private List<Applicant> applicant;
	
	@OneToOne(cascade = CascadeType.ALL)
    private DetailsOfLoanAppliedFor detailsOfLoanAppliedFor;
	
	@OneToOne(cascade = CascadeType.ALL)
	private EmploymentDetails employmentDetails;
	
	@OneToOne(cascade = CascadeType.ALL)
    private IncomeDetails incomeDetails;
	
	@OneToMany(cascade = CascadeType.ALL)
    private List<PropertyDetails> propertyDetails;
	
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "loanId")
    private List<Reference> references;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "loanId")
    private List<ExistingLoanDetails> existingLoanDetails;
    
    @OneToOne(mappedBy = "loanApplication", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "loanId")
    @JsonIgnore
    private LoanSanction loanSanction;
    
   
    
}

