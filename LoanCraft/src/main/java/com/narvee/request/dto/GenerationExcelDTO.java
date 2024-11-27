package com.narvee.request.dto;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerationExcelDTO {
	
	private String consumerName;
    private Date dateOfBirth;
    private String gender;
    private String incomeTaxIdNumber;
    private String passportNumber;
    private String passportIssueDate;
    private String voterIdNumber;
    private String drivingLicenseNumber;
    private String drivingLicenseIssueDate;
    private String drivingLicenseExpiryDate;
    private String rationCardNumber;
    private String universalIdNumber;
    private String additionalId1;
    private String additionalId2;
    private Long telephoneNoMobile;
    private String telephoneNoResidence;
    private String telephoneNoOffice;
    private String extensionOffice;
    private String telephoneNoOther;
    private String extensionOther;
    private String emailId1;
    private String emailId2;
    private String address1;
    private Long stateCode1;
    private Long pinCode1;
    private String addressCategory1;
    private String residenceCode1;
    private String address2;
    private String stateCode2;
    private String pinCode2;
    private String addressCategory2;
    private String residenceCode2;
    private String currentNewMemberCode;
    private String currentNewMemberShortName;
    private Long currNewAccountNo;
    private String accountType;
    private String ownershipIndicator;
    private Date dateOpenedDisbursed;
    private String dateOfLastPayment;
    private String dateClosed;
    private String dateReported;
    private String highCreditSanctionedAmt;
    private String currentBalance;
    private String amtOverdue;
    private String noOfDaysPastDue;
    private String oldMbrCode;
    private String oldMbrShortName;
    private String oldAccNo;
    private String oldAccType;
    private String oldOwnershipIndicator;
    private String suitFiledWilfulDefault;
    private String creditFacilityStatus;
    private String assetClassification;
    private String valueOfCollateral;
    private String typeOfCollateral;
    private String creditLimit;
    private String cashLimit;
    private String rateOfInterest;
    private String repaymentTenure;
    private String emiAmount;
    private String writtenOffAmountTotal;
    private String writtenOffPrincipalAmount;
    private String settlementAmt;
    private String paymentFrequency;
    private String actualPaymentAmt;
    private String occupationCode;
    private String income;
    private String netGrossIncomeIndicator;
    private String monthlyAnnualIncomeIndicator;
	
}
