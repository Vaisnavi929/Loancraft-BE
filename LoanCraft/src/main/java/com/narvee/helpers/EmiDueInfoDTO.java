package com.narvee.helpers;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmiDueInfoDTO {
	private int emiNumber;
	private LocalDate dueDate;
	private long dueDays;
	private double dueAmount;
	private double chequeReturnCharge;
	private double penalty;
	private String emiMonth;
}
