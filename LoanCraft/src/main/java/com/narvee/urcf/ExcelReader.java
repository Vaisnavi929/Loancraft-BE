package com.narvee.urcf;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelReader {
	public static void main(String[] args) {
		String filePath = "UAT.xlsx"; // Update with the actual path

		try (FileInputStream fis = new FileInputStream(new File(filePath)); Workbook workbook = new XSSFWorkbook(fis)) {

			Sheet sheet = workbook.getSheet("Consumer_Format"); // Read the specified sheet
			DataFormatter dataFormatter = new DataFormatter(); // Converts all cell values to String
			int rowIndex = 1;
			int totalColumns = 72;
			int colndex = 0;
			
			for (Row row : sheet) {
				if (isRowEmpty(row))
					continue; // Skip empty rows

				ConsumerInfoDTO consumer = new ConsumerInfoDTO();
				for (int i = 0; i < totalColumns; i++) {
					Cell cell = row.getCell(i);
					String cellValue = dataFormatter.formatCellValue(cell); // Convert every cell value to
																						// String
		
					setConsumerData( consumer,  i,  cellValue);
//                 
//                    }

					colndex++;
					
					
				}
				colndex = 0;

			
				System.err.println("----------");

				System.err.println(rowIndex++);
//				System.out.println(con);
				// New line after each row
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean isRowEmpty(Row row) {
		if (row == null)
			return true;
		for (Cell cell : row) {
			if (cell != null && cell.getCellType() != CellType.BLANK) {
				return false;
			}
		}
		return true;
	}
	
	
	
	public static  void setConsumerData(ConsumerInfoDTO consumer, int columnIndex, String cellValue) {
        // Map columns to DTO fields (Modify according to your DTO fields)
		switch (columnIndex) {
        case 0: consumer.setConsumerName(cellValue); break;
        case 1: consumer.setDateOfBirth(cellValue); break;
        case 2: consumer.setGender(cellValue); break;
        case 3: consumer.setIncomeTaxIdNumber(cellValue); break;
        case 4: consumer.setPassportNumber(cellValue); break;
        case 5: consumer.setPassportIssueDate(cellValue); break;
        case 6: consumer.setPassportExpiryDate(cellValue); break;
        case 7: consumer.setVoterIdNumber(cellValue); break;
        case 8: consumer.setDrivingLicenseNumber(cellValue); break;
        case 9: consumer.setDrivingLicenseIssueDate(cellValue); break;
        case 10: consumer.setDrivingLicenseExpiryDate(cellValue); break;
        case 11: consumer.setRationCardNumber(cellValue); break;
        case 12: consumer.setUniversalIdNumber(cellValue); break;
        case 13: consumer.setAdditionalId1(cellValue); break;
        case 14: consumer.setAdditionalId2(cellValue); break;
        case 15: consumer.setTelephoneNoMobile(cellValue); break;
        case 16: consumer.setTelephoneNoResidence(cellValue); break;
        case 17: consumer.setTelephoneNoOffice(cellValue); break;
        case 18: consumer.setExtensionOffice(cellValue); break;
        case 19: consumer.setTelephoneNoOther(cellValue); break;
        case 20: consumer.setExtensionOther(cellValue); break;
        case 21: consumer.setEmailId1(cellValue); break;
        case 22: consumer.setEmailId2(cellValue); break;
        case 23: consumer.setAddress1(cellValue); break;
        case 24: consumer.setStateCode1(cellValue); break;
        case 25: consumer.setPinCode1(cellValue); break;
        case 26: consumer.setAddressCategory1(cellValue); break;
        case 27: consumer.setResidenceCode1(cellValue); break;
        case 28: consumer.setAddress2(cellValue); break;
        case 29: consumer.setStateCode2(cellValue); break;
        case 30: consumer.setPinCode2(cellValue); break;
        case 31: consumer.setAddressCategory2(cellValue); break;
        case 32: consumer.setResidenceCode2(cellValue); break;
        case 33: consumer.setCurrentNewMemberCode(cellValue); break;
        case 34: consumer.setCurrentNewMemberShortName(cellValue); break;
        case 35: consumer.setCurrNewAccountNo(cellValue); break;
        case 36: consumer.setAccountType(cellValue); break;
        case 37: consumer.setOwnershipIndicator(cellValue); break;
        case 38: consumer.setDateOpenedDisbursed(cellValue); break;
        case 39: consumer.setDateOfLastPayment(cellValue); break;
        case 40: consumer.setDateClosed(cellValue); break;
        case 41: consumer.setDateReported(cellValue); break;
        case 42: consumer.setHighCreditSanctionedAmt(cellValue); break;
        case 43: consumer.setCurrentBalance(cellValue); break;
        case 44: consumer.setAmountOverdue(cellValue); break;
        case 45: consumer.setNoOfDaysPastDue(cellValue); break;
        case 46: consumer.setOldMbrCode(cellValue); break;
        case 47: consumer.setOldMbrShortName(cellValue); break;
        case 48: consumer.setOldAccNo(cellValue); break;
        case 49: consumer.setOldAccType(cellValue); break;
        case 50: consumer.setOldOwnershipIndicator(cellValue); break;
        case 51: consumer.setSuitFiledWilfulDefault(cellValue); break;
        case 52: consumer.setCreditFacilityStatus(cellValue); break;
        case 53: consumer.setAssetClassification(cellValue); break;
        case 54: consumer.setValueOfCollateral(cellValue); break;
        case 55: consumer.setTypeOfCollateral(cellValue); break;
        case 56: consumer.setCreditLimit(cellValue); break;
        case 57: consumer.setCashLimit(cellValue); break;
        case 58: consumer.setRateOfInterest(cellValue); break;
        case 59: consumer.setRepaymentTenure(cellValue); break;
        case 60: consumer.setEmiAmount(cellValue); break;
        case 61: consumer.setWrittenOffAmountTotal(cellValue); break;
        case 62: consumer.setWrittenOffPrincipalAmount(cellValue); break;
        case 63: consumer.setSettlementAmt(cellValue); break;
        case 64: consumer.setPaymentFrequency(cellValue); break;
        case 65: consumer.setActualPaymentAmt(cellValue); break;
        case 66: consumer.setOccupationCode(cellValue); break;
        case 67: consumer.setIncome(cellValue); break;
        case 68: consumer.setNetGrossIncomeIndicator(cellValue); break;
        case 69: consumer.setMonthlyAnnualIncomeIndicator(cellValue); break;
        case 70: consumer.setCkyc(cellValue); break;
        case 71: consumer.setNregaCardNumber(cellValue); break;
        default: throw new IllegalArgumentException("Invalid index: " + columnIndex);
        }
    }
}
