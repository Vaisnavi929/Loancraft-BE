package com.narvee.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Data
@Table(name="lmsLoanSaction")
@AllArgsConstructor
@NoArgsConstructor
public class LoanSanction {
	
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long loanSactionId;
    private Double loanRequested;
    private Double amountSanctioned;
    private Double amountDisbursed;
    private Double rateOfInterest;
    private String interestType;
    private int numberOfEmis;
    private Double emiAmount;
    private String repaymentMode;
    private Date emiStartDate;
    private Date emiEndDate;
    private Date emiDueDate;
    private String currency;
    private String paymentFrequency;
    private Double creditLimit;
    private Double cashLimit;
    private String status;
    
    @OneToOne(fetch = FetchType.EAGER,mappedBy ="loanSanction")
    @JoinColumn(name = "loanId")
    private LoanApplication loanApplication;
    
    @OneToMany(mappedBy = "loanSanction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Payment> payment;
} 
