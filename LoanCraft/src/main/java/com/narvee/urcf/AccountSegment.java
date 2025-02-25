package com.narvee.urcf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountSegment {
	private String segmentTag = "TL04T001";
	private String currentReportingMemberCode;
	private String currentMemberShortName;
	private String currentAccountNumber;
	private String accountType;
	private String ownershipIndicator;
	private String disbursedDate;
	private String dateOfLastPayment;
	private String dateClosed;
	private String dateReportedAndSertified;
	private String sactionedAmount;
	private String currentBalance;
	private String amountOverdue;
	private String numberOfDaysPastDue;
	private String oldReportingMemberCode;
	private String oldMemberShortName;
	private String oldAccountNumber;
	private String oldAccountType;
	private String oldOwnershipIndicator;
	private String suitFiled;
	private String creditFacilityStatus;
	private String assertClassification;
	private String valueOfCollateral;
	private String typeOfCollateral;
	private String creditLimit;
	private String cashLimit;
	private String rateOfInterest;
	private String repaymentTenure;
	private String emiAmount;
	private String writtenOffAmountTotal;
	private String writtenOffAmountPrincipal;
	private String settlementAmount;
	private String paymentFrequency;
	private String actualPaymentAmount;
	private String occupationCode;
	private String income;
	private String grossIncomeIndicator;
	private String annualIncomeIndicator;

	public String accountSegment(AccountSegment accountSegment) {
		StringBuilder accountSeg = new StringBuilder();
		

		accountSeg.append(segmentTag);
		accountSeg.append("01");
		
		String formattedCurrentReportingMemberCode = String.format("%02d", accountSegment.getCurrentReportingMemberCode().length());
		accountSeg.append(formattedCurrentReportingMemberCode);
		accountSeg.append(accountSegment.getCurrentReportingMemberCode());
		accountSeg.append("02");
		String formattedCurrentMemberShortName = String.format("%02d", accountSegment.getCurrentMemberShortName().length());
		accountSeg.append(formattedCurrentMemberShortName);
		accountSeg.append(accountSegment.getCurrentMemberShortName());
		accountSeg.append("03");
		String formattedCurrentAccountNumber = String.format("%02d", accountSegment.getCurrentAccountNumber().length());
		accountSeg.append(formattedCurrentAccountNumber);
		accountSeg.append(accountSegment.getCurrentAccountNumber());
		accountSeg.append("0402");
		accountSeg.append(getLoanTypeCode(accountSegment.getAccountType()));
		accountSeg.append("0501");
		accountSeg.append(getOwnershipIndicatorCode(accountSegment.getOwnershipIndicator()));
		accountSeg.append("0808");
		accountSeg.append(accountSegment.getDisbursedDate());
		if (accountSegment.getDateOfLastPayment() != null && !accountSegment.getDateOfLastPayment().isEmpty()) {
			accountSeg.append("0908");
			accountSeg.append(accountSegment.getDateOfLastPayment());
		}
		if (accountSegment.getDateClosed() != null && !accountSegment.getDateClosed().isEmpty()) {
			accountSeg.append("1008");
			accountSeg.append(accountSegment.getDateClosed());
		}
		accountSeg.append("1108");
		accountSeg.append(accountSegment.getDateReportedAndSertified());
		accountSeg.append("12");
		String formattedvalueOfSactionedAmount = String.format("%02d", accountSegment.getSactionedAmount().length());
		accountSeg.append(formattedvalueOfSactionedAmount);
		accountSeg.append(accountSegment.getSactionedAmount());
		accountSeg.append("13");
		String formattedvalueOfCurrentBalance = String.format("%02d", accountSegment.getCurrentBalance().length());
		accountSeg.append(formattedvalueOfCurrentBalance);
		accountSeg.append(accountSegment.getCurrentBalance());
		if (accountSegment.getAmountOverdue() != null && !accountSegment.getAmountOverdue().isEmpty()) {
			accountSeg.append("14");
			String formattedvalueOfAmountOverdue = String.format("%02d", accountSegment.getAmountOverdue().length());
			accountSeg.append(formattedvalueOfAmountOverdue);
			accountSeg.append(accountSegment.getAmountOverdue());
		}

		if (accountSegment.getNumberOfDaysPastDue() != null && !accountSegment.getNumberOfDaysPastDue().isEmpty()) {
			accountSeg.append("15");
			String formattedpastDueDays =accountSegment.getCurrentAccountNumber().length() <= 9
					? String.format("%02d", accountSegment.getCurrentAccountNumber().length())
					: String.valueOf(accountSegment.getCurrentAccountNumber().length());
			accountSeg.append(formattedpastDueDays.length());
			accountSeg.append(formattedpastDueDays);

		}

		if (accountSegment.getOldReportingMemberCode() != null && !accountSegment.getOldReportingMemberCode().isEmpty()) {
			accountSeg.append("16");
			String formattedOldReportingMemberCode = String.format("%02d", accountSegment.getOldReportingMemberCode().length());
			accountSeg.append(formattedOldReportingMemberCode);
			accountSeg.append(accountSegment.getOldReportingMemberCode());
		}

		if (accountSegment.getOldMemberShortName() != null && !accountSegment.getOldMemberShortName().isEmpty()) {
			accountSeg.append("17");
			String formattedOldMemberShortName = String.format("%02d", accountSegment.getOldMemberShortName().length());
			accountSeg.append(formattedOldMemberShortName);
			accountSeg.append(accountSegment.getOldMemberShortName());
		}

		if (accountSegment.getOldAccountNumber() != null && !accountSegment.getOldAccountNumber().isEmpty()) {
			accountSeg.append("18");
			accountSeg.append(accountSegment.getOldAccountNumber().length());
			accountSeg.append(accountSegment.getOldAccountNumber());
		}

		if (accountSegment.getOldAccountType() != null && !accountSegment.getOldAccountType().isEmpty()) {
			accountSeg.append("1902");
			accountSeg.append(getLoanTypeCode(accountSegment.getOldAccountType()));
		}

		if (accountSegment.getOldOwnershipIndicator() != null && !accountSegment.getOldOwnershipIndicator().isEmpty()) {
			accountSeg.append("2001");
			accountSeg.append(getOwnershipIndicatorCode(getOldOwnershipIndicator()));
		}

		if (accountSegment.getSuitFiled() != null && !accountSegment.getSuitFiled().isEmpty()) {
			accountSeg.append("2102");
			accountSeg.append(getSuitFiledStatusCode(accountSegment.getSuitFiled()));
		}

		if (accountSegment.getCreditFacilityStatus() != null && !accountSegment.getCreditFacilityStatus().isEmpty()) {
			accountSeg.append("2202");
			accountSeg.append(getCreditStatusCode(accountSegment.getCreditFacilityStatus()));
		}

		if (accountSegment.getAssertClassification() != null && !accountSegment.getAssertClassification().isEmpty()) {
			accountSeg.append("2602");
			accountSeg.append(getLoanClassificationCode(accountSegment.getAssertClassification()));
		}

		if (accountSegment.getValueOfCollateral() != null && !accountSegment.getValueOfCollateral().isEmpty()) {
			accountSeg.append("34");
			String formattedvalueOfCollateral = String.format("%02d", accountSegment.getValueOfCollateral().length());
			accountSeg.append(formattedvalueOfCollateral);
			accountSeg.append(accountSegment.getValueOfCollateral());
		}

		if (accountSegment.getTypeOfCollateral() != null && !accountSegment.getTypeOfCollateral().isEmpty()) {
			accountSeg.append("3502");
			accountSeg.append(accountSegment.getTypeOfCollateral());

			accountSeg.append(getCollateralTypeCode(accountSegment.getTypeOfCollateral()));
		}

		if (accountSegment.getCreditLimit() != null && !accountSegment.getCreditLimit().isEmpty()) {
			accountSeg.append("36");
			String formattedvalueOfCreditLimit = String.format("%02d", accountSegment.getCreditLimit().length());
			accountSeg.append(formattedvalueOfCreditLimit);
			accountSeg.append(accountSegment.getCreditLimit());

		}

		if (accountSegment.getCashLimit() != null && !accountSegment.getCashLimit().isEmpty()) {
			accountSeg.append("37");
			String formattedvalueOfCashLimit = String.format("%02d", accountSegment.getCashLimit().length());
			accountSeg.append(formattedvalueOfCashLimit);
			accountSeg.append(accountSegment.getCashLimit());

		}

		if (accountSegment.getRateOfInterest() != null && !accountSegment.getRateOfInterest().isEmpty()) {
		    String rateOfInterest = String.format("%.2f", Double.parseDouble(accountSegment.getRateOfInterest())); // Ensure 2 decimal places
		    accountSeg.append("38");
		    String formattedvalueOfRateOfInterest = String.format("%02d", rateOfInterest.length());
		    accountSeg.append(formattedvalueOfRateOfInterest);
		    accountSeg.append(rateOfInterest);
		}

		if (accountSegment.getRepaymentTenure() != null && !accountSegment.getRepaymentTenure().isEmpty()) {
			accountSeg.append("39");
			String formattedvalueOfRepaymentTenure = String.format("%02d", accountSegment.getRepaymentTenure().length());
			accountSeg.append(formattedvalueOfRepaymentTenure);
			accountSeg.append(accountSegment.getRepaymentTenure());

		}

		if (accountSegment.getEmiAmount() != null && !accountSegment.getEmiAmount().isEmpty()) {
			accountSeg.append("40");
			String formattedvalueOfEmiAmount = String.format("%02d", accountSegment.getEmiAmount().length());
			accountSeg.append(formattedvalueOfEmiAmount);
			accountSeg.append(accountSegment.getEmiAmount());
		}

		if (accountSegment.getWrittenOffAmountTotal() != null && !accountSegment.getWrittenOffAmountTotal().isEmpty()) {
			accountSeg.append("41");
			String formattedvalueOfWrittenOffAmountTotal = String.format("%02d", accountSegment.getWrittenOffAmountTotal().length());
			accountSeg.append(formattedvalueOfWrittenOffAmountTotal);
			accountSeg.append(accountSegment.getWrittenOffAmountTotal());
		}

		if (accountSegment.getWrittenOffAmountPrincipal() != null && !accountSegment.getWrittenOffAmountPrincipal().isEmpty()) {
			accountSeg.append("42");
			String formattedvalueOfWrittenOffAmountPrincipal = String.format("%02d", accountSegment.getWrittenOffAmountPrincipal().length());
			accountSeg.append(formattedvalueOfWrittenOffAmountPrincipal);
			accountSeg.append(accountSegment.getWrittenOffAmountPrincipal());
		}

		if (accountSegment.getSettlementAmount() != null && !accountSegment.getSettlementAmount().isEmpty()) {
			accountSeg.append("43");
			String formattedvalueOfSettlementAmount = String.format("%02d", accountSegment.getSettlementAmount().length());
			accountSeg.append(formattedvalueOfSettlementAmount);
			accountSeg.append(accountSegment.getSettlementAmount());
		}

		if (accountSegment.getPaymentFrequency() != null && !accountSegment.getPaymentFrequency().isEmpty()) {
			accountSeg.append("4402");
			accountSeg.append(getRepaymentFrequencyCode(accountSegment.getPaymentFrequency()));
		}

		if (accountSegment.getActualPaymentAmount() != null && !accountSegment.getActualPaymentAmount().isEmpty()) {
			accountSeg.append("45");
			String formattedvalueOfActualPaymentAmount = String.format("%02d", accountSegment.getActualPaymentAmount().length());
			accountSeg.append(formattedvalueOfActualPaymentAmount);
			accountSeg.append(accountSegment.getActualPaymentAmount());
		}

		if (accountSegment.getOccupationCode() != null &&!accountSegment.getOccupationCode().isEmpty()) {
			accountSeg.append("4602");
			accountSeg.append(getEmploymentTypeCode(accountSegment.getOccupationCode()));
		}

		if (accountSegment.getIncome() != null && !accountSegment.getIncome().isEmpty()) {
			accountSeg.append("47");
			String formattedvalueOfIncome = String.format("%02d", accountSegment.getIncome().length());
			accountSeg.append(formattedvalueOfIncome);
			accountSeg.append(accountSegment.getIncome());
		}

		if (accountSegment.getGrossIncomeIndicator() != null && !accountSegment.getGrossIncomeIndicator() .isEmpty()) {
			accountSeg.append("4801");
			accountSeg.append(accountSegment.getGrossIncomeIndicator() );
		}

		if (accountSegment.getAnnualIncomeIndicator() != null && !accountSegment.getAnnualIncomeIndicator().isEmpty()) {
			accountSeg.append("4901");
			accountSeg.append(accountSegment.getAnnualIncomeIndicator());
			accountSeg.append(accountSegment.getAnnualIncomeIndicator());
		}

		return accountSeg.toString();

	}

	public static String getEmploymentTypeCode(String input) {
		if (input == null || input.isEmpty()) {
			return ""; // Default for invalid input
		}

		switch (input) {
		case "01":
		case "Salaried":
			return "01";
		case "02":
		case "Self Employed Professional":
			return "02";
		case "03":
		case "Self Employed":
			return "03";
		case "04":
		case "Others":
			return "04";
		default:
			return ""; // Default case for unmapped values
		}
	}

	public static String getRepaymentFrequencyCode(String input) {
		if (input == null || input.isEmpty()) {
			return ""; // Default for invalid input
		}

		switch (input) {
		case "01":
		case "Weekly":
			return "01";
		case "02":
		case "Fortnightly":
			return "02";
		case "03":
		case "Monthly":
			return "03";
		case "04":
		case "Quarterly":
			return "04";
		case "05":
		case "Bullet payment":
			return "05";
		case "06":
		case "Daily":
			return "06";
		case "07":
		case "Half yearly":
			return "07";
		case "08":
		case "Yearly":
			return "08";
		case "09":
		case "On-demand":
			return "09";
		default:
			return ""; // Default case for unmapped values
		}
	}

	public static String getCollateralTypeCode(String input) {
		if (input == null || input.isEmpty()) {
			return ""; // Default for invalid input
		}

		switch (input) {
		case "00":
		case "No Collateral":
			return "00";
		case "01":
		case "Property":
			return "01";
		case "02":
		case "Gold":
			return "02";
		case "03":
		case "Shares":
			return "03";
		case "04":
		case "Saving Account and Fixed Deposit":
			return "04";
		default:
			return ""; // Default case for unmapped values
		}
	}

	public static String getLoanClassificationCode(String input) {
		if (input == null || input.isEmpty()) {
			return ""; // Default for invalid input
		}

		switch (input) {
		case "01":
		case "Standard":
			return "01";
		case "02":
		case "Substandard":
			return "02";
		case "03":
		case "Doubtful":
			return "03";
		case "04":
		case "Loss":
			return "04";
		case "05":
		case "Special Mention Account":
			return "05";
		default:
			return ""; // Default case for unmapped values
		}
	}

	// Method to get Account Status Code
	public static String getCreditStatusCode(String input) {
		if (input == null || input.isEmpty()) {
			return ""; // Default for invalid input
		}

		switch (input) {
		case "00":
		case "Restructured Loan":
			return "00";
		case "01":
		case "Restructured Loan (Govt. Mandated)":
			return "01";
		case "02":
		case "Written-off":
			return "02";
		case "03":
		case "Settled":
			return "03";
		case "04":
		case "Post (WO) Settled":
			return "04";
		case "05":
		case "Account Sold":
			return "05";
		case "06":
		case "Written Off and Account Sold":
			return "06";
		case "07":
		case "Account Purchased":
			return "07";
		case "08":
		case "Account Purchased and Written Off":
			return "08";
		case "09":
		case "Account Purchased and Settled":
			return "09";
		case "10":
		case "Account Purchased and Restructured":
			return "10";
		case "11":
		case "Restructured due to Natural Calamity":
			return "11";
		case "12":
		case "Restructured due to COVID-19":
			return "12";
		case "99":
		case "Clear existing status":
			return "99";
		default:
			return ""; // Default case for unmapped values
		}
	}

	public static String getSuitFiledStatusCode(String input) {
		if (input == null || input.isEmpty()) {
			return ""; // Default for invalid input
		}

		switch (input) {
		case "00":
		case "No Suit filed":
			return "00";
		case "01":
		case "Suit filed":
			return "01";
		case "02":
		case "Wilful default":
			return "02";
		case "03":
		case "Suit filed (Wilful default)":
			return "03";
		default:
			return ""; // Default case for unmapped values
		}
	}

	public static String getOwnershipIndicatorCode(String input) {
		if (input == null || input.isEmpty()) {
			return ""; // Default for invalid input
		}

		switch (input) {
		case "1":
		case "Individual":
			return "1";
		case "2":
		case "Authorised User":
			return "2";
		case "3":
		case "Guarantor":
			return "3";
		case "4":
		case "Joint":
			return "4";
		default:
			return ""; // Default case for unmapped values
		}
	}

	public static String getLoanTypeCode(String input) {
		if (input == null || input.isEmpty()) {
			return ""; // Default for invalid input
		}

		switch (input) {
		case "01":
		case "Current Auto Loan (Personal)":
			return "01";
		case "02":
		case "Current Housing Loan":
			return "02";
		case "03":
		case "Current Property Loan":
			return "03";
		case "04":
		case "Current Loan Against Shares / Securities":
			return "04";
		case "05":
		case "Current Personal Loan":
			return "05";
		case "06":
		case "Current Consumer Loan":
			return "06";
		case "07":
		case "Current Gold Loan":
			return "07";
		case "08":
		case "Current Education Loan":
			return "08";
		case "09":
		case "Current Loan to Professional":
			return "09";
		case "10":
		case "Current Credit Card":
			return "10";
		case "11":
		case "Current Leasing":
			return "11";
		case "12":
		case "Current Overdraft":
			return "12";
		case "13":
		case "Current Two-Wheeler Loan":
			return "13";
		case "14":
		case "Current Non-Funded Credit Facility":
			return "14";
		case "15":
		case "Current Loan Against Bank Deposits":
			return "15";
		case "16":
		case "Current Fleet Card":
			return "16";
		case "17":
		case "Current Commercial Vehicle Loan":
			return "17";
		case "18":
		case "Future Telco – Wireless":
			return "18";
		case "19":
		case "Future Telco – Broadband":
			return "19";
		case "20":
		case "Future Telco – Landline":
			return "20";
		case "21":
		case "Current Seller Financing":
			return "21";
		case "23":
		case "Current GECL Loan Secured":
			return "23";
		case "24":
		case "Current GECL Loan Unsecured":
			return "24";
		default:
			return ""; // Default case for unmapped values
		}
	}

	public static AccountSegment mapConsumerDTOToAccountSegment(ConsumerInfoDTO consumerDTO) {
		AccountSegment accountSegment = new AccountSegment();
		accountSegment.setCurrentReportingMemberCode(consumerDTO.getCurrentNewMemberCode());
		accountSegment.setCurrentMemberShortName(consumerDTO.getCurrentNewMemberShortName());
		accountSegment.setCurrentAccountNumber(consumerDTO.getCurrNewAccountNo());
		accountSegment.setAccountType(consumerDTO.getAccountType());
		accountSegment.setOwnershipIndicator(consumerDTO.getOwnershipIndicator());
		accountSegment.setDisbursedDate(consumerDTO.getDateOpenedDisbursed());
		accountSegment.setDateOfLastPayment(consumerDTO.getDateOfLastPayment());
		accountSegment.setDateClosed(consumerDTO.getDateClosed());
		accountSegment.setDateReportedAndSertified(consumerDTO.getDateReported());
		accountSegment.setSactionedAmount(consumerDTO.getHighCreditSanctionedAmt());
		accountSegment.setCurrentBalance(consumerDTO.getCurrentBalance());
		accountSegment.setAmountOverdue(consumerDTO.getAmountOverdue());
		accountSegment.setNumberOfDaysPastDue(consumerDTO.getDrivingLicenseNumber());
		accountSegment.setOldReportingMemberCode(consumerDTO.getOldMbrCode());
		accountSegment.setOldMemberShortName(consumerDTO.getOldMbrShortName());
		accountSegment.setOldAccountNumber(consumerDTO.getOldAccNo());
		accountSegment.setOldAccountType(consumerDTO.getOldAccType());
		accountSegment.setOldOwnershipIndicator(consumerDTO.getOldOwnershipIndicator());
		accountSegment.setSuitFiled(consumerDTO.getSuitFiledWilfulDefault());
		accountSegment.setCreditFacilityStatus(consumerDTO.getCreditFacilityStatus());
		accountSegment.setAssertClassification(consumerDTO.getAssetClassification());
		accountSegment.setValueOfCollateral(consumerDTO.getValueOfCollateral());
		accountSegment.setTypeOfCollateral(consumerDTO.getTypeOfCollateral());
		accountSegment.setCreditLimit(consumerDTO.getCreditLimit());
		accountSegment.setCashLimit(consumerDTO.getCashLimit());
		accountSegment.setRateOfInterest(consumerDTO.getRateOfInterest());
		accountSegment.setRepaymentTenure(consumerDTO.getRepaymentTenure());
		accountSegment.setEmiAmount(consumerDTO.getEmiAmount());
		accountSegment.setWrittenOffAmountTotal(consumerDTO.getWrittenOffAmountTotal());
		accountSegment.setWrittenOffAmountPrincipal(consumerDTO.getWrittenOffPrincipalAmount());
		accountSegment.setSettlementAmount(consumerDTO.getSettlementAmt());
		accountSegment.setPaymentFrequency(consumerDTO.getPaymentFrequency());
		accountSegment.setActualPaymentAmount(consumerDTO.getActualPaymentAmt());
		accountSegment.setOccupationCode(consumerDTO.getOccupationCode());
		accountSegment.setIncome(consumerDTO.getIncome());
		accountSegment.setGrossIncomeIndicator(consumerDTO.getNetGrossIncomeIndicator());
		accountSegment.setAnnualIncomeIndicator(consumerDTO.getMonthlyAnnualIncomeIndicator());

		return accountSegment;
	}

}
