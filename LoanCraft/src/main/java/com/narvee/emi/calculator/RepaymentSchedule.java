package com.narvee.emi.calculator;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepaymentSchedule {
    private Long outstanding;
    private Long installment;
    private Long interest;
    private Long principalRepaid;
    private Long remainingPrincipalAmount;
    private LocalDate dueDate;
    
}