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
public class CustomersInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_id")
	private Long customerId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "gender")
	private String gender;

	@Column(name = "mobile_no")
	private String mobileNo;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	@JsonIgnore
	private String password;

	@Column(name = "fathers_or_spouses_name")
	private String fathersOrSpousesName;

	@Column(name = "dob")
	private LocalDate dob;

	@Column(name = "highest_qualification")
	private String highestQualification;

	@Column(name = "adhaar_number", unique = true)
	private String adhaarNumber;

	@Column(name = "account_number", unique = true)
	private String accountNumber;

	@OneToOne(mappedBy = "customer")
	@JsonManagedReference
	private PermanentAddress permanentAddress;

	@OneToOne(mappedBy = "customer")
	@JsonManagedReference
	private CurrentAddress currentAddress;

}
