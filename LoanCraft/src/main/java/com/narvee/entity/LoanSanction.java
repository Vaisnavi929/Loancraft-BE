package com.narvee.entity;

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
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "lmsLoanSaction")
@AllArgsConstructor
@NoArgsConstructor
public class LoanSanction extends AuditModel{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long loanSactionId;
	private Double loanRequested;
	private Double amountSanctioned;
	private Double amountDisbursed;
	private Double rateOfInterest;
	private String interestType;
	private int numberOfEmis;
	private Double emiAmount;
	private String repaymentMode;
	private Date emiStartDate;
	private Date emiEndDate;
	private Date emiDueDate;
	private String currency;
	private String paymentFreqmuency;
	private String status;
	private Double loanProcessingCharges;
	private Double documentationCharges;
	private Double penaltyForLatePayment;
	private Long addedBy;
	private Long updatedBy;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "loanId")
	private Applications applications;
}
