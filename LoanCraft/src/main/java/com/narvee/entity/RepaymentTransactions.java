package com.narvee.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.narvee.commons.AuditModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lms_repayment_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RepaymentTransactions extends AuditModel{
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate paymentDate;

    @Column(nullable = false)
    private Double amountPaid;

    @Column(nullable = false)
    private Double principalComponent;

    @Column(nullable = false)
    private Double interestComponent;

    @Column(nullable = false)
    private Double penaltyComponent;

    @Column(nullable = true)
    private Double remainingBalance;

    @Column(length = 50)
    private String paymentMode; // e.g., UPI, NEFT, Cash, etc.

//    @Column(length = 100)
//    private String receiptNumber;

    @Column(length = 255)
    private String description;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "repayment_id")
    @JsonBackReference
    private LmsRepayment repayment;
    
}