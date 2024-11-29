package com.narvee.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="LmsDetailsOfLoanAppliedFor")
@AllArgsConstructor
@NoArgsConstructor
public class DetailsOfLoanAppliedFor {
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private Long loanApplicantid;
	private String purposeOfLoan;
	private Double loanAmountRequired;
	private Long tenure;
	private String repaymentMode;
	private String earlierLoanInBeacon;
	private Long customerId;
	
	@OneToOne(cascade = CascadeType.ALL,mappedBy = "detailsOfLoanAppliedFor")
	private LoanApplication application;


}
