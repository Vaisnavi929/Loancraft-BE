package com.narvee.entity;

import java.util.Date;

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
@Table(name = "LmsCustomerDetails")
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetails {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
    private String loanNumber;
    private Long loanAmount;
    private Date loanIssueDate;
    private Double rateOfInterest;
    private Double emiAmount;
    private String loanToner;
    private Date emiDate;
    private Date loanClosingDate;
    private String paymentStatus;    
    private Date dateOfPayment; 
    private Double cqReturnCharges;
    private Double penalty;
    private String remarks;

}
