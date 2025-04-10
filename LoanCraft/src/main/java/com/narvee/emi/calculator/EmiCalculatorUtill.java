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

	public static EmiCalResponseDTO calculateRepaymentSchedule(RepaymentScheduleRequest request) {
		Double principal = request.getLoanAmount(); // Total loan amount
		Double annualRate = request.getAnnualInterestRate(); // Annual interest rate
		Integer months = request.getInstallmentTenor(); // Total number of installments

		// Convert annual rate to monthly rate
		Double monthlyRate = annualRate / 12 / 100;

		// Calculate fixed EMI (Equated Monthly Installment)
		Double emi = (principal * monthlyRate * Math.pow(1 + monthlyRate, months))
				/ (Math.pow(1 + monthlyRate, months) - 1);

		List<RepaymentSchedule> scheduleList = new ArrayList<>();
		Double outstanding = principal; // Initial outstanding loan amount

		for (int i = 0; i < months; i++) {
			Double interest = outstanding * monthlyRate; // Interest for the current period
			Double principalRepaid = emi - interest; // Principal repaid in this period
			Double prevOutstanding = outstanding; // Store current outstanding before deducting principal

			outstanding -= principalRepaid; // Update remaining outstanding principal after repayment

			RepaymentSchedule schedule = new RepaymentSchedule();

			// Set the values before reducing the outstanding amount
			schedule.setOutstanding(Math.round(prevOutstanding)); // Outstanding at the start of the period (before
																	// repayment)
			schedule.setInstallment(Math.round(emi)); // Fixed installment (EMI)
			schedule.setInterest(Math.round(interest)); // Interest paid for this period
			schedule.setPrincipalRepaid(Math.round(principalRepaid)); // Principal repaid for this period
			schedule.setRemainingPrincipalAmount(Math.max(0, Math.round(outstanding))); // Remaining principal, ensuring
																						// no negative values
			schedule.setDueDate(LocalDate.now().plusMonths(i + 1)); // Setting due date for installment
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

}
