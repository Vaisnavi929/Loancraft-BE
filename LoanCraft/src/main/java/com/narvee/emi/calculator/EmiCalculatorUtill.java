package com.narvee.emi.calculator;

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
		Double principal = request.getLoanAmount();
		Double annualRate = request.getAnnualInterestRate();
		Integer months = request.getInstallmentTenor();
		LocalDate startDate = request.getStartDate().toLocalDate();
		Double monthlyRate = annualRate / 12 / 100;
		Double emi = (principal * monthlyRate * Math.pow(1 + monthlyRate, months))
				/ (Math.pow(1 + monthlyRate, months) - 1);

		List<RepaymentSchedule> scheduleList = new ArrayList<>();
		Double outstanding = principal;

		for (int i = 0; i < months; i++) {
			Double interest = outstanding * monthlyRate;
			Double principalRepaid = emi - interest;
			Double prevOutstanding = outstanding;
			outstanding -= principalRepaid;

			RepaymentSchedule schedule = new RepaymentSchedule();
			schedule.setOutstanding(Math.round(prevOutstanding));
			schedule.setInstallment(Math.round(emi));
			schedule.setInterest(Math.round(interest));
			schedule.setPrincipalRepaid(Math.round(principalRepaid));
			schedule.setRemainingPrincipalAmount(Math.max(0, Math.round(outstanding)));
			schedule.setDueDate(startDate.plusMonths(i));

			scheduleList.add(schedule);
		}

		EmiCalResponseDTO emiResponse = new EmiCalResponseDTO();
		emiResponse.setRepaymentSchedules(scheduleList);

		LocalDate minDueDate = scheduleList.stream().min(Comparator.comparing(RepaymentSchedule::getDueDate))
				.map(RepaymentSchedule::getDueDate).orElse(null);

		LocalDate maxDueDate = scheduleList.stream().max(Comparator.comparing(RepaymentSchedule::getDueDate))
				.map(RepaymentSchedule::getDueDate).orElse(null);

		EmiDatesDTO emiDates = new EmiDatesDTO();
		emiDates.setEmiStartDate(minDueDate);
		emiDates.setEmiEndDate(maxDueDate);
		emiDates.setMonthlyDueDate(maxDueDate.getDayOfMonth());
		emiDates.setNumberOfEmis(scheduleList.size());
		emiResponse.setEmiDates(emiDates);
		return emiResponse;

	}

	public static EmiCalResponseDTO calculateRepaymentFixed(RepaymentScheduleRequest request) {
	    Double principal = request.getLoanAmount(); // e.g., 200000
	    Double annualInterestRate = request.getAnnualInterestRate(); // e.g., 18
	    Integer months = request.getInstallmentTenor(); // e.g., 24
	    LocalDate startDate = request.getStartDate().toLocalDate();

	    // Total interest for full loan period
	    double totalInterest = (principal * annualInterestRate / 100) * (months / 12.0);

	    // Monthly fixed interest
	    double monthlyInterest = totalInterest / months;

	    // Monthly principal
	    double monthlyPrincipal = principal / months;

	    // Monthly EMI
	    double monthlyEMI = monthlyPrincipal + monthlyInterest;

	    // Total repayable amount
	    double totalRepayable = totalInterest + principal;

	    double outstandingAmount = totalRepayable;

	    System.out.printf("%-5s %-20s %-15s %-10s %-15s %-25s%n", "S.No", "Outstanding Amount", "Installment",
	            "Interest", "Principal Paid", "Remaining Principal");

	    List<RepaymentSchedule> scheduleList = new ArrayList<>();

	    for (int month = 1; month <= months; month++) {
	        double remainingPrincipal = outstandingAmount - monthlyEMI;
	        double principalPaid = monthlyPrincipal;

	        // Handle the final installment if less than EMI
	        if (remainingPrincipal < 0) {
	            monthlyEMI = outstandingAmount;
	            principalPaid = monthlyEMI - monthlyInterest;
	            remainingPrincipal = 0;
	        }

	        // Console debug output
	        System.out.printf("%-5d %-20.0f %-15.0f %-10.0f %-15.0f %-25.0f%n",
	                month, outstandingAmount, monthlyEMI, monthlyInterest, principalPaid, remainingPrincipal);

	        RepaymentSchedule schedule = new RepaymentSchedule();
	        schedule.setOutstanding(Math.round(outstandingAmount));
	        schedule.setInstallment(Math.round(monthlyEMI));
	        schedule.setInterest(Math.round(monthlyInterest));
	        schedule.setPrincipalRepaid(Math.round(principalPaid));
	        schedule.setRemainingPrincipalAmount(Math.round(remainingPrincipal));
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
