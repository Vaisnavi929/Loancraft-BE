package com.narvee.entity;

import java.time.LocalDate;

import com.narvee.commons.AuditModel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="lms_disbursment")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Disbursment extends AuditModel{
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long disbursmentId;
	@Column(name ="disb_ref_no")
	private String disbRefNo;
    private LocalDate date;
    
	@Column(name = "amaxnum")
	private Long amaxnum;
    
    private String accountNo;
    
    private String isfc;

    private Double disbursedAmount;

    private String remarks;

    private String branch;
    
    private String status;
    
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "loanSactionId")
    private LoanSanction loanSanction;

}
