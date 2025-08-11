package com.narvee.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PenaltyResponse {
	private long overdueDays;
	private double perDayPenalty;
	private double totalPenalty;
	private long daysCovered;
	private double penaltyPaid;
	private double remainingPenalty;
	private double excessAmount;
	private long uncoveredDays;

}
