package com.narvee.response;

import java.time.LocalDate;
import java.util.List;

import com.narvee.helpers.EmiDueInfoDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceipetResponse {
	
	private double loanAmount;
	private double outstanding;
	private double principal;
	private double interest;
	private int numberOfEmisDue;
	private double repaid;
	private LocalDate dueDate;
	private double emiDueAmount;
	private double totalDue;
	private double totalPenalty;
	private List<EmiDueInfoDTO> emiDueInfo;	
	
	
	
	

}
