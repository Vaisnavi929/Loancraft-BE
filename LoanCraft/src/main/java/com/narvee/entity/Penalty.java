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
import lombok.NoArgsConstructor;

@Entity
@Table(name ="LmsPenalty")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Penalty extends AuditModel{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long penaltyId;
	private Double penalty;
	private String remarks; 
	@ManyToOne
	@JoinColumn(name = "paymentId")
	private LmsPayment lmsPayment;
////
	

}
