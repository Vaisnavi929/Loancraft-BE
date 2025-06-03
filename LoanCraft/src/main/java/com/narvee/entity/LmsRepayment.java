
package com.narvee.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LmsRepayment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long repaymentId;
	private Double dueAmount;
	private LocalDate dueDate;
	private Double amountPaid;
	private String amountStatus;
	private String paymentType;
	private Double interest ;
	private String remarks;
	private Double principalAmount;
	
	
	 @ManyToOne
	 @JoinColumn(name = "disbursment_id")
	 private Disbursment disbursment;
	 
	 @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)	 
	 @JoinColumn(name = "penaltyId")
	 private List<Penalty> penalty;	  
}

