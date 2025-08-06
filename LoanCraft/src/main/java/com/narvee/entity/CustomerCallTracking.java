package com.narvee.entity;

import java.time.LocalDateTime;

import com.narvee.commons.AuditModel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cCtid;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String duration;
	@Column(columnDefinition = "TEXT")
	private String feedBack;
	private String status;
	

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name ="crm_Id" )
	private CustomerRelationshipMangement customerRelationshipMangement;
	
}
