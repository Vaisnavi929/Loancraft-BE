package com.narvee.entity;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.narvee.commons.AuditModel;
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

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "gender")
	private String gender;

	@Column(name = "fathers_or_spouses_name")
	private String fathersOrSpousesName;

	@Column(name = "dob")
	private LocalDate dob;

	@Column(name = "highest_qualification")
	private String highestQualification;

	@Column(name = "adhaar_number")
	private String adhaarNumber;

	@Column(name = "mobile_number")
	private String mobileNumber;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	@JsonIgnore
	private String password;

	@Column(name = "account_number", unique = true)
	private String accountNumber;

	@Column(name = "added_by")
	private Long addedBy;

	@Column(name = "updated_by")
	private Long updatedBy;

	@OneToOne(mappedBy = "customer")
	@JsonManagedReference
	private PermanentAddress permanentAddress;

	@OneToOne(mappedBy = "customer")
	@JsonManagedReference
	private CurrentAddress currentAddress;

}
