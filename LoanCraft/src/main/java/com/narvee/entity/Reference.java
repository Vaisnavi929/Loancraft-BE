package com.narvee.entity;

import jakarta.persistence.CascadeType;
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
@Table(name = "LmsReference")
@AllArgsConstructor
@NoArgsConstructor
public class Reference {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long referenceid;
	private String initial;
	private String fullName;
	private Long knownForYears;
	private String relationship;
	private Long pincode;
	private Long mobilenumber;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "loanId")
	
	private LoanApplication loanApplication;

}
