package com.narvee.entity;

import java.util.Date;

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
@Data
@Table(name="LmsLedger")
@AllArgsConstructor
@NoArgsConstructor
public class Ledger {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ledgerId;
	private Date date;
	private Double loanAmount;
	private Double principleAmount;
	private Double amountPaid;
	private Double balance;
	private Double paid;
	
	@ManyToOne
	@JoinColumn(name="payamentId")
	private Payment payment;

}
