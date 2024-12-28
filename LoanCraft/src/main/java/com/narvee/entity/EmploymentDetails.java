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
@Table(name="LmsEmploymentDetails")
@AllArgsConstructor
@NoArgsConstructor
public class EmploymentDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long empid;
	private String employmentType;
	private Double salary;
	private Long yearsinEmployment;
	private String companyName;
	private String companyAdrees;
	private Long pincode;
	private String gstDetails;
	
	

}
