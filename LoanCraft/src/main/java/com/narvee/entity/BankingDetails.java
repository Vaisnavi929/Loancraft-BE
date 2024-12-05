package com.narvee.entity;

import java.util.Date;

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
@Table(name = "LmsBankingDetails")
@AllArgsConstructor
@NoArgsConstructor
public class BankingDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bankid;
	private String accountHolder;
	private String branchName;
	private Long accountNo;
	private Date dateOfOpening;
	private String ifscCode;
	private String accountType;

	@OneToOne(cascade = CascadeType.ALL,mappedBy ="bankingDetails")
	@JoinColumn(name = "loanId")
	private LoanApplication application;

}
