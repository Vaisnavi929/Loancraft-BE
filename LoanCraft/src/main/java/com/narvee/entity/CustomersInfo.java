package com.narvee.entity;

import java.time.LocalDate;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.narvee.commons.AuditModel;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

@Table(name = "LmsCustomers_info")
@EqualsAndHashCode(callSuper = true)
public class CustomersInfo extends AuditModel {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long customerId;

	@Column(name = "firstName")
	private String firstName;


	private String middleName;

	@Column(name = "last_name")
	private String lastName;


	private String gender;

	private String fathersOrSpousesName;

	private LocalDate dob;

	private String highestQualification;

	private String adhaarNumber;

	@Column(name = "mobileNumber")
	private String mobileNumber;


	@Column(name = "email")
	private String email;


	@Column(name = "password", unique = true)
	@JsonIgnore
	private String password;

	@Column(name = "accountNumber", unique = true)
	private String accountNumber;

	@Column(name = "addedby")
	private Long addedBy;

	@Column(name = "updatedby")
	private Long updatedBy;


	@OneToOne(mappedBy = "customer")
	@JsonManagedReference
	private PermanentAddress permanentAddress;

	@OneToOne(mappedBy = "customer")
	@JsonManagedReference
	private CurrentAddress currentAddress;

}
