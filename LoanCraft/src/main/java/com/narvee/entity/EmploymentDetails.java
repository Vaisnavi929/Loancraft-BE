package com.narvee.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="LmsEmploymentDetails")
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long empid;
	private String employmentType;
	private Double salary;
	private Long yearsinEmployment;
	private String companyName;
	private String companyAdrees;
	private Long pincode;
	private String gstDetails;
	
	@OneToOne(cascade = CascadeType.ALL,mappedBy ="employmentDetails")
	@JoinColumn(name = "loanId")
	private LoanApplication application;
	

}
