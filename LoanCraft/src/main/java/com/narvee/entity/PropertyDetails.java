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
@Table(name="LmsPropertyDetails")
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long propertyid;
	private String ownerName;
	private String propertyAddress;
    private Double plotArea;
    private Double builtUpArea;
    private Double buildingAge;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "loanId")
    private LoanApplication loanApplication; 
    
 
}

