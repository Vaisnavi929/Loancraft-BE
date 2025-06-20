package com.narvee.entity;

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
	private boolean isApproved;

	private Double recievableMount;
	private Double requestedAmount;

	@ManyToOne
	@JoinColumn(name = "repayment_id")
	private LmsRepayment lmsRepayment;

}
