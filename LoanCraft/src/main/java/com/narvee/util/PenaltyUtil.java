package com.narvee.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.narvee.entity.RepaymentTransactions;
import com.narvee.response.PenaltyResponse;

public class PenaltyUtil {

	public static PenaltyResponse calculatePenaltyCoverage(LocalDate dueDate, double dueAmount,
			double paidAmount,RepaymentTransactions lastTran ,LocalDate date) {

		double monthlyPenaltyRate = 0.03; // 3% per month
		double dailyPenaltyRate = monthlyPenaltyRate / 30;
		LocalDate now =date;

		long overdueDays = ChronoUnit.DAYS.between(dueDate, now);
		overdueDays = Math.max(0, overdueDays);
		if (lastTran!=null) {
			overdueDays=overdueDays+lastTran.getPenaltyUncoveredDays();
		}

		double perDayPenalty = dueAmount * dailyPenaltyRate;
		double totalPenalty = perDayPenalty * overdueDays;

		double penaltyPaid;
		long daysCovered;
		double remainingPenalty;
		double excessAmount = 0;

		if (paidAmount >= totalPenalty) {
			penaltyPaid = totalPenalty;
			daysCovered = overdueDays;
			remainingPenalty = 0;
			excessAmount = paidAmount - totalPenalty;
		} else {
			penaltyPaid = paidAmount;
			daysCovered = Math.round(paidAmount / perDayPenalty);
			remainingPenalty = totalPenalty - penaltyPaid;
		}

		long uncoveredDays = overdueDays - daysCovered;

		return new PenaltyResponse(
			overdueDays,
			Math.round(perDayPenalty),
			Math.round(totalPenalty),
			daysCovered,
			Math.round(penaltyPaid),
			Math.round(remainingPenalty),
			Math.round(excessAmount),
			uncoveredDays
		);
	}

}
