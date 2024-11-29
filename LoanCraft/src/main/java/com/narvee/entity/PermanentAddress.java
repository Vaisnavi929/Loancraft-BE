package com.narvee.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="LmsPermanentAdress")
@NoArgsConstructor
@AllArgsConstructor
public class PermanentAddress {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pAddressId;
	private String address;
	private String city;
	private String state;
	private String country;
	private String location;
	private String landmark;
	private Long stayedOfYears;
	private Long pincode;

	@OneToOne(cascade = CascadeType.ALL,mappedBy ="permanentAddress")
	@JoinColumn(name = "loanId")
	private LoanApplication application;
	
}

