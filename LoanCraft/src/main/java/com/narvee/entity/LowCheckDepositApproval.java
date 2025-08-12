package com.narvee.entity;

import java.time.LocalDate;

import com.narvee.commons.AuditModel;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LmsLowCheckDepositApproval")
@EqualsAndHashCode(callSuper = true)
public class LowCheckDepositApproval extends AuditModel {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long requestById;
	private Long approvedById;
	private boolean isApproved = false;
	private String remarks;
	private Double receivableAmount;
	private String status;
	private Double requestedAmount;
	private LocalDate postedDate;
	private String chequeNumber;

	@ManyToOne
	@JoinColumn(name = "repayment_id")
	private LmsRepayment lmsRepayment;
	
	@ManyToOne
	@JoinColumn(name = "disbursment_id")
	private Disbursment disbursment;

}
