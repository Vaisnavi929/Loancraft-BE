package com.narvee.util;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PenaltyCalculator {


    public static double calculatePenalty(LocalDate dueDate, LocalDate tillDate, double dueAmount, double dailyPenaltyRate) {
        if (tillDate.isAfter(dueDate)) {
            long daysOverdue = ChronoUnit.DAYS.between(dueDate, tillDate);
            double penalty = dueAmount * dailyPenaltyRate * daysOverdue;
            // Round to 2 decimal places
            return BigDecimal.valueOf(penalty)
                             .setScale(2, RoundingMode.HALF_UP)
                             .doubleValue();
        } else {
            return 0.0; // No penalty if not overdue
        }
    }
}
