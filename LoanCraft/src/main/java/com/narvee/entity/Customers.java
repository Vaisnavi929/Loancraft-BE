package com.narvee.entity;

import java.time.LocalDate;

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
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LmsCustomers")
public class Customers {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long customerId;

	@Column(name = "first_name")
	private String firstName;

	private String middleName;

	@Column(name = "last_name")
	private String lastName;

	private String gender;

	private String fathersOrSpousesName;

	private LocalDate dob;

	private String highestQualification;

	private String adhaarNumber;

	@Column(name = "mobile_no")
	private String mobileNo;

	@Column(name = "email")
	private String email;

	@Column(name = "password", unique = true)
	@JsonIgnore
	private String password;

	@Column(name = "accountNumber", unique = true)
	private String accountNumber;

	@OneToOne(mappedBy = "customer")
	@JsonManagedReference
	private PermanentAddress permanentAddress;

	@OneToOne(mappedBy = "customer")
	@JsonManagedReference
	private CurrentAddress currentAddress;

}
