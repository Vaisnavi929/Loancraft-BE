package com.narvee.request.dto;

import java.util.List;

import com.narvee.emi.calculator.RepaymentSchedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmiCalResponseDTO {
	
	private List<RepaymentSchedule> repaymentSchedules;
	private EmiDatesDTO emiDates;

}
