package com.narvee.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="LmsIncomeDetails")
@AllArgsConstructor
@NoArgsConstructor
public class IncomeDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long incomeid;
	private Double householdIncome;
	private Double householdExpenditure;
	private Double applicantIncome;
	private Double otherIncome1;
	private Double otherIncome2;
	private Double otherIncome3;
	private Double totalIncome;
	private Double netIncome;
}
