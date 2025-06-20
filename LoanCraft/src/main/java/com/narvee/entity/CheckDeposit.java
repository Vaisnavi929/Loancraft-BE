package com.narvee.entity;

import java.time.LocalDate;

import com.narvee.commons.AuditModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LmsCheckDeposit")
@EqualsAndHashCode(callSuper = true)
public class CheckDeposit extends AuditModel {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String chequeNumber;
	private LocalDate postingDate;
	private Double amount;

	private String reason;

	private String remarks;
	private String status = "Posted";

	@ManyToOne
	@JoinColumn(name = "disbursment_id")
	private Disbursment disbursment;
	@OneToOne
	@JoinColumn(name = "repayment_id")
	private LmsRepayment lmsRepayment;

}
