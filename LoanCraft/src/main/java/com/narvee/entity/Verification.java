package com.narvee.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LmsVerification")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Verification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long verifiedId;
	private Boolean reviewed;
	private Boolean cibil;
	private Boolean physicalLocationVerified;
	private Boolean sanction;
	private Boolean disbursed;
	

	@OneToMany(mappedBy = "verification", cascade = CascadeType.ALL)
	private List<Document> physicalLocationDocument = new ArrayList<>();

	@OneToOne
	
	@JoinColumn(name = "loanId")
	private Applications applications;	

}

