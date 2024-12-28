package com.narvee.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="States")
public class States {
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private Long stateId;
	private String stateCode;
	private String state;
	private Long prefixMin;
	private Long prefixMax;
//	
//	 @OneToOne(mappedBy = "stateCode")
//	 @JoinColumn(name = "pAddressId") 
//	    private PermanentAddress permanentAddress;
//
//	    @OneToOne(mappedBy = "stateCode")
//	    @JoinColumn(name = "cAddressId") 
//	    private CurrentAddress currentAddress;
	
	

}
