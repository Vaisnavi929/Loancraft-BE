package com.narvee.entity;

import java.util.Date;

import jakarta.persistence.CascadeType;
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
@Table(name = "LmsApplicant")
@AllArgsConstructor
@NoArgsConstructor
public class Applicant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long applicantId;
	private String proof;
	private String document;
	private String number;
	private Date issueDate;
	private Date expDate;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "loanId", nullable = false)
	private LoanApplication loanApplication;

}
