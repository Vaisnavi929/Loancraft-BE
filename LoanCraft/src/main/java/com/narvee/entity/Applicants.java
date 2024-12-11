package com.narvee.entity;

import jakarta.persistence.Column;
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
@AllArgsConstructor
@NoArgsConstructor
@Table(name="applicant")
public class Applicants {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long applicantId;
	
	@Column(name = "first_name") 
    private String firstName;

    @Column(name = "last_name") 
    private String lastName;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "email") 
    private String email;

    @Column(name = "password")
    private String password;
}
