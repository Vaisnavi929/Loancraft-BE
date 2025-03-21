package com.narvee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String fileName;

	    private String fileType;

//	    @Lob
// 	    @Column(columnDefinition = "BYTEA")
	    private byte[] data;
	    
	    @ManyToOne
	    @JoinColumn(name = "loanId")
	    @JsonIgnore
	    private Applications loanApplication; 

	     
    }

