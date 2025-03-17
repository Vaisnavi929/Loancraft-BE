package com.narvee.entity;

import java.time.LocalDate;


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

@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LmsPayment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentId;
	private Double dueAmount;
	private LocalDate dueDate;
	private String amountPaid;
	private String amountStatus;
	
	  @ManyToOne
	  @JoinColumn(name = "disbursment_id")
	  private Disbursment disbursment;
	

}
