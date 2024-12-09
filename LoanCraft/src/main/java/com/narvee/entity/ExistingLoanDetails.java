package com.narvee.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "LmsExistingLoanDetails")
@AllArgsConstructor
@NoArgsConstructor
public class ExistingLoanDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long existingid;
	private String nameOfInstitution;
	private String purposeOfLoan;
	private Double loanAmount;
	private Double tenureOfLoan;
	private Long monthlyInstallment;
	private Double currentOutstanding;
	private Long balanceTenure;
	
	@ManyToOne
	@JoinColumn(name = "loanId")
	private LoanApplication loanApplication;
	

}
