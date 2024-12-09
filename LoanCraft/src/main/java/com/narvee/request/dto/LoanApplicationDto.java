package com.narvee.request.dto;

import java.util.Date;
import java.util.List;

import com.narvee.entity.BankingDetails;
import com.narvee.entity.CurrentAddress;
import com.narvee.entity.DetailsOfLoanAppliedFor;
import com.narvee.entity.EmploymentDetails;
import com.narvee.entity.ExistingLoanDetails;
import com.narvee.entity.IdentityProof;
import com.narvee.entity.IncomeDetails;
import com.narvee.entity.PermanentAddress;
import com.narvee.entity.PropertyDetails;
import com.narvee.entity.Reference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanApplicationDto {
	
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
	
    private CurrentAddress currentAddress;

    private PermanentAddress permanentAddress;
    
	private BankingDetails bankingDetails;
	
	private List<IdentityProof> identity;

    private DetailsOfLoanAppliedFor detailsOfLoanAppliedFor;
	
	private EmploymentDetails employmentDetails;
	
    private IncomeDetails incomeDetails;
	     
    private List<PropertyDetails> propertyDetails;
  
    private List<Reference> references;
    
    private List<ExistingLoanDetails> existingLoanDetails;
    

    
}
