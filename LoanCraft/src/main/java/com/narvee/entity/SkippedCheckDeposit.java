package com.narvee.entity;

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

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;


@Entity
@Table(name = "lms_skipped_check_deposit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkippedCheckDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "row_no")
    private Integer rowNo;
    
    @Column(name = "loan_account_number")
    private String loanAccountNumber;

    @Column(name = "cheque_number")
    private String chequeNumber;

    @Column(name = "posting_date")
    private LocalDate postingDate;

    @Column(name = "cheque_clearance_date")
    private LocalDate chequeClearanceDate;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "status")
    private String status;

    @Column(name = "reason")
    private String reason;

    @Column(name = "skip_reason")
    private String skipReason;

    @ManyToOne
    @JoinColumn(name = "disbursment_id")
    private Disbursment disbursment;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    
    
}
