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
}
