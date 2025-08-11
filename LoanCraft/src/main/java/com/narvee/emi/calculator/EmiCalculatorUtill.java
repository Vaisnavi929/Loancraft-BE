package com.narvee.emi.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.narvee.request.dto.EmiCalResponseDTO;
import com.narvee.request.dto.EmiDatesDTO;
import com.narvee.request.dto.RepaymentScheduleRequest;

@Component
public class EmiCalculatorUtill {

	public static EmiCalResponseDTO calculateRepaymentfloating(RepaymentScheduleRequest request) {
	    BigDecimal principal = BigDecimal.valueOf(request.getLoanAmount());
	    BigDecimal annualRate = BigDecimal.valueOf(request.getAnnualInterestRate());
	    int months = request.getInstallmentTenor();
	    LocalDate startDate = request.getStartDate().toLocalDate();

	    // Monthly rate = annualRate / 12 / 100
	    BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
	                                       .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

	    // EMI calculation
	    BigDecimal onePlusRatePowN = BigDecimal.valueOf(Math.pow(monthlyRate.add(BigDecimal.ONE).doubleValue(), months));
	    BigDecimal emi = principal.multiply(monthlyRate).multiply(onePlusRatePowN)
	                    .divide(onePlusRatePowN.subtract(BigDecimal.ONE), 10, RoundingMode.HALF_UP)
	                    .setScale(2, RoundingMode.HALF_UP);

	    List<RepaymentSchedule> scheduleList = new ArrayList<>();
	    BigDecimal outstanding = principal;

	    for (int i = 0; i < months; i++) {
	        BigDecimal interest = outstanding.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
	        BigDecimal principalRepaid = emi.subtract(interest).setScale(2, RoundingMode.HALF_UP);
	        BigDecimal prevOutstanding = outstanding.setScale(2, RoundingMode.HALF_UP);
	        outstanding = outstanding.subtract(principalRepaid).setScale(2, RoundingMode.HALF_UP);

	        RepaymentSchedule schedule = new RepaymentSchedule();
	        schedule.setOutstanding(prevOutstanding.doubleValue());
	        schedule.setInstallment(emi.doubleValue());
	        schedule.setInterest(interest.doubleValue());
	        schedule.setPrincipalRepaid(principalRepaid.doubleValue());
	        schedule.setRemainingPrincipalAmount(outstanding.doubleValue());
	        schedule.setDueDate(startDate.plusMonths(i));

	        scheduleList.add(schedule);
	    }

	    EmiCalResponseDTO emiResponse = new EmiCalResponseDTO();
	    emiResponse.setRepaymentSchedules(scheduleList);

	    LocalDate minDueDate = scheduleList.stream()
	            .min(Comparator.comparing(RepaymentSchedule::getDueDate))
	            .map(RepaymentSchedule::getDueDate)
	            .orElse(null);

	    LocalDate maxDueDate = scheduleList.stream()
	            .max(Comparator.comparing(RepaymentSchedule::getDueDate))
	            .map(RepaymentSchedule::getDueDate)
	            .orElse(null);

	    EmiDatesDTO emiDates = new EmiDatesDTO();
	    emiDates.setEmiStartDate(minDueDate);
	    emiDates.setEmiEndDate(maxDueDate);
	    emiDates.setMonthlyDueDate(maxDueDate.getDayOfMonth());
	    emiDates.setNumberOfEmis(scheduleList.size());

	    emiResponse.setEmiDates(emiDates);
	    return emiResponse;
	}


	public static EmiCalResponseDTO calculateRepaymentFixed(RepaymentScheduleRequest request) {
	    BigDecimal principal = BigDecimal.valueOf(request.getLoanAmount()); // e.g., 200000
	    BigDecimal annualInterestRate = BigDecimal.valueOf(request.getAnnualInterestRate()); // e.g., 18
	    int months = request.getInstallmentTenor(); // e.g., 24
	    LocalDate startDate = request.getStartDate().toLocalDate();

	    // Total interest = (P * R / 100) * (months / 12)
	    BigDecimal totalInterest = principal.multiply(annualInterestRate)
	            .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
	            .multiply(BigDecimal.valueOf(months).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP));

	    // Monthly interest
	    BigDecimal monthlyInterest = totalInterest.divide(BigDecimal.valueOf(months), 10, RoundingMode.HALF_UP);

	    // Monthly principal
	    BigDecimal monthlyPrincipal = principal.divide(BigDecimal.valueOf(months), 10, RoundingMode.HALF_UP);

	    // Monthly EMI
	    BigDecimal monthlyEMI = monthlyPrincipal.add(monthlyInterest).setScale(2, RoundingMode.HALF_UP);

	    // Total repayable amount
	    BigDecimal totalRepayable = totalInterest.add(principal);

	    BigDecimal outstandingAmount = totalRepayable;

	    List<RepaymentSchedule> scheduleList = new ArrayList<>();

	    for (int month = 1; month <= months; month++) {
	        BigDecimal remainingPrincipal = outstandingAmount.subtract(monthlyEMI).setScale(2, RoundingMode.HALF_UP);
	        BigDecimal principalPaid = monthlyPrincipal.setScale(2, RoundingMode.HALF_UP);
	        BigDecimal thisMonthEMI = monthlyEMI;

	        // Final installment adjustment
	        if (remainingPrincipal.compareTo(BigDecimal.ZERO) < 0) {
	            thisMonthEMI = outstandingAmount.setScale(2, RoundingMode.HALF_UP);
	            principalPaid = thisMonthEMI.subtract(monthlyInterest).setScale(2, RoundingMode.HALF_UP);
	            remainingPrincipal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
	        }

	        RepaymentSchedule schedule = new RepaymentSchedule();
	        schedule.setOutstanding(outstandingAmount.setScale(2, RoundingMode.HALF_UP).doubleValue());
	        schedule.setInstallment(thisMonthEMI.doubleValue());
	        schedule.setInterest(monthlyInterest.setScale(2, RoundingMode.HALF_UP).doubleValue());
	        schedule.setPrincipalRepaid(principalPaid.doubleValue());
	        schedule.setRemainingPrincipalAmount(remainingPrincipal.doubleValue());
	        schedule.setDueDate(startDate.plusMonths(month - 1));

	        scheduleList.add(schedule);

	        outstandingAmount = remainingPrincipal;
	    }

	    // Build response
	    EmiCalResponseDTO response = new EmiCalResponseDTO();
	    response.setRepaymentSchedules(scheduleList);

	    EmiDatesDTO datesDTO = new EmiDatesDTO();
	    datesDTO.setEmiStartDate(startDate);
	    datesDTO.setEmiEndDate(startDate.plusMonths(months - 1));
	    datesDTO.setNumberOfEmis(months);
	    datesDTO.setMonthlyDueDate(startDate.getDayOfMonth());

	    response.setEmiDates(datesDTO);

	    return response;
	}

}
