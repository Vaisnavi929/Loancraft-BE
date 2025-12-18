package com.narvee.entity;

import java.time.LocalDate;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "lms_check_deposit")
public class CheckDeposit extends AuditModel {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "chequeNumber")
	private String chequeNumber;
	@Column(name = "postingDate")
	private LocalDate postingDate;
	@Column(name = "chequeClearanceDate")
	private LocalDate chequeClearanceDate;
	@Column(name = "amount")
	private Double amount = 0.0;;
	@Column(name = "reason")
	private String reason;
	@Column(nullable = true, name = "chequeReturnCharges")
	private Double chequeReturnCharges = 0.0;
	@Column(name = "remarks")
	private String remarks;
	private String status = "Posted";
	@Column(name = "addedby")
	private Long addedBy;
	@Column(name = "updatedby")
	private Long updatedBy;

	@ManyToOne
	@JoinColumn(name = "disbursment_id")
	private Disbursment disbursment;

	@ManyToOne
	@JoinColumn(name = "repayment_id")
	private LmsRepayment lmsRepayment;

}
