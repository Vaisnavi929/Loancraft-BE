package com.narvee.entity;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.narvee.commons.AuditModel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "Lms_customer_call_tracking")
public class CustomerCallTracking extends AuditModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cctId;
	private String customerName;
	private String agentName;
	private LocalDate date;
	@Column(columnDefinition = "TEXT")
	private String feedBack;
	private String status;
	private String loanAccountNumber;

	@OneToMany(mappedBy = "customerCall", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("cct-referenceInfo")
	// @ToStringExclude
	private List<ReferenceInfo> references = new ArrayList<>();

}
