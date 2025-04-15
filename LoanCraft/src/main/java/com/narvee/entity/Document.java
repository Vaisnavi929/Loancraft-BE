package com.narvee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name="lmsDocument")
@AllArgsConstructor
@NoArgsConstructor
public class Document {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String fileName;

	private String fileType;
	
	private Boolean verified;
	
	private String remarks;
	
	@ManyToOne
	@JoinColumn(name = "loanId")
	@JsonIgnore
	private Applications loanApplication;


	     
    }   


