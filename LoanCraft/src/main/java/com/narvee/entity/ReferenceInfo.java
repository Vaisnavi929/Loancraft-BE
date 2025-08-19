package com.narvee.entity;

import com.narvee.commons.AuditModel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Lms_reference_info")
public class ReferenceInfo extends AuditModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long referenceId;
	private String referenceName;
	private String mobileNumber;
	@Column(columnDefinition = "TEXT")
	private String referenceFeedBack;
	private String referenceStatus;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cctId")
	private CustomerCallTracking customerCallTracking;

	
}
