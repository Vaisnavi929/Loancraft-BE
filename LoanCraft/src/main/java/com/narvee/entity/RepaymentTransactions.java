package com.narvee.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.narvee.commons.AuditModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lms_repayment_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RepaymentTransactions extends AuditModel {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDate paymentDate;

	@Column(nullable = false)
	private Double amountPaid = 0.0;

	@Column(nullable = false)
	private Double principalComponent = 0.0;

	@Column(nullable = false)
	private Double interestComponent = 0.0;

	@Column(nullable = false)
	private Double penaltyComponent = 0.0;

//	@Column(nullable = true)
//	private Double remainingBalance = 0.0;

	private Double otherCharges = 0.0;
	
	private String otherChargesType;
	
	@Column(nullable = false)
	private Double latePenalty = 0.0;
	
	private Double chequeReturnCharges = 0.0;

	@Column(length = 50)
	private String paymentMode; // e.g., UPI, NEFT, Cash, etc.

//    @Column(length = 100)
//    private String receiptNumber;

	@Column(length = 255)
	private String description;

	@ManyToOne(optional = false)
	@JoinColumn(name = "repayment_id")
	@JsonBackReference
	private LmsRepayment repayment;

}