package com.narvee.emi.calculator;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepaymentSchedule {
    private double outstanding;
    private double installment;
    private double interest;
    private double principalRepaid;
    private double remainingPrincipalAmount;
    private LocalDate dueDate;
    
    
}