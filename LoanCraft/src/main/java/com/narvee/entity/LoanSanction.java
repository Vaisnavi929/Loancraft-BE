package com.narvee.entity;

import java.time.LocalDate;
import java.util.Date;

import com.narvee.commons.AuditModel;

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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "lmsLoanSaction")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoanSanction extends AuditModel{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long loanSactionId;
	private Double loanRequested;
	private Double amountSanctioned;
	private Double rateOfInterest;
	private String interestType;
	private int numberOfEmis;
	private Double emiAmount;
	private String repaymentMode;
	private Date emiStartDate;
	private Date emiEndDate;
	private String dueDate;
	private String paymentFreqmuency;
	private String status;
	private Double loanProcessingCharges;
	private Double documentationCharges;
	private Long addedBy;
	private Long updatedBy;
	private String loanAccountNumber;
	private Double originationFee;
	private Double returnOriginationfee;
	
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "loanId")
	private Applications applications;
}
