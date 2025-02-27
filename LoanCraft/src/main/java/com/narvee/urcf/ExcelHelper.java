package com.narvee.urcf;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ExcelHelper {

    // Check if the uploaded file is a valid Excel file
    public static boolean hasExcelFormat(MultipartFile file) {
        return file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    // Read data from Excel and return a list of Employee objects
    public static List<ConsumerInfoDTO> parseExcel(InputStream inputStream) {
        List<ConsumerInfoDTO> consumerList=new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // Read first sheet
            Iterator<Row> rows = sheet.iterator();
            boolean isFirstRow = true;
            
       

//            int i=0;
            while (rows.hasNext()) {
            	
            	
                Row currentRow = rows.next();
                if (isFirstRow) { 
                    isFirstRow = false; // Skip header row
                    continue;
                }
                
                // Skip empty rows
                if (isRowEmpty(currentRow)) {
                    continue;
                }

               
             // Read row data
                ConsumerInfoDTO consumer = new ConsumerInfoDTO();
                consumer.setConsumerName(getCellValue(currentRow.getCell(0)));
                consumer.setDateOfBirth(getCellValue(currentRow.getCell(1)));
                consumer.setGender(getCellValue(currentRow.getCell(2)));
                consumer.setIncomeTaxIdNumber(getCellValue(currentRow.getCell(3)));
                consumer.setPassportNumber(getCellValue(currentRow.getCell(4)));
                consumer.setPassportIssueDate(getCellValue(currentRow.getCell(5)));
                consumer.setPassportExpiryDate(getCellValue(currentRow.getCell(6)));
                consumer.setVoterIdNumber(getCellValue(currentRow.getCell(7)));
                consumer.setDrivingLicenseNumber(getCellValue(currentRow.getCell(8)));
                consumer.setDrivingLicenseIssueDate(getCellValue(currentRow.getCell(9)));
                consumer.setDrivingLicenseExpiryDate(getCellValue(currentRow.getCell(10)));
                consumer.setRationCardNumber(getCellValue(currentRow.getCell(11)));
                consumer.setUniversalIdNumber(getCellValue(currentRow.getCell(12)));
                consumer.setAdditionalId1(getCellValue(currentRow.getCell(13)));
                consumer.setAdditionalId2(getCellValue(currentRow.getCell(14)));
                consumer.setTelephoneNoMobile(getCellValue(currentRow.getCell(15)));
                consumer.setTelephoneNoResidence(getCellValue(currentRow.getCell(16)));
                consumer.setTelephoneNoOffice(getCellValue(currentRow.getCell(17)));
                consumer.setExtensionOffice(getCellValue(currentRow.getCell(18)));
                consumer.setTelephoneNoOther(getCellValue(currentRow.getCell(19)));
                consumer.setExtensionOther(getCellValue(currentRow.getCell(20)));
                consumer.setEmailId1(getCellValue(currentRow.getCell(21)));
                consumer.setEmailId2(getCellValue(currentRow.getCell(22)));
                consumer.setAddress1(getCellValue(currentRow.getCell(23)));
                consumer.setStateCode1(getCellValue(currentRow.getCell(24)));
                consumer.setPinCode1(getCellValue(currentRow.getCell(25)));
                consumer.setAddressCategory1(getCellValue(currentRow.getCell(26)));
                consumer.setResidenceCode1(getCellValue(currentRow.getCell(27)));
                consumer.setAddress2(getCellValue(currentRow.getCell(28)));
                consumer.setStateCode2(getCellValue(currentRow.getCell(29)));
                consumer.setPinCode2(getCellValue(currentRow.getCell(30)));
                consumer.setAddressCategory2(getCellValue(currentRow.getCell(31)));
                consumer.setResidenceCode2(getCellValue(currentRow.getCell(32)));
                consumer.setCurrentNewMemberCode(getCellValue(currentRow.getCell(33)));
                consumer.setCurrentNewMemberShortName(getCellValue(currentRow.getCell(34)));
                consumer.setCurrNewAccountNo(getCellValue(currentRow.getCell(35)));
                consumer.setAccountType(getCellValue(currentRow.getCell(36)));
                consumer.setOwnershipIndicator(getCellValue(currentRow.getCell(37)));
                consumer.setDateOpenedDisbursed(getCellValue(currentRow.getCell(38)));
                consumer.setDateOfLastPayment(getCellValue(currentRow.getCell(39)));
                consumer.setDateClosed(getCellValue(currentRow.getCell(40)));
                consumer.setDateReported(getCellValue(currentRow.getCell(41)));
                consumer.setHighCreditSanctionedAmt(getCellValue(currentRow.getCell(42)));
                consumer.setCurrentBalance(getCellValue(currentRow.getCell(43)));
                consumer.setAmountOverdue(getCellValue(currentRow.getCell(44)));
                consumer.setNoOfDaysPastDue(getCellValue(currentRow.getCell(45)));
                consumer.setOldMbrCode(getCellValue(currentRow.getCell(46)));
                consumer.setOldMbrShortName(getCellValue(currentRow.getCell(47)));
                consumer.setOldAccNo(getCellValue(currentRow.getCell(48)));
                consumer.setOldAccType(getCellValue(currentRow.getCell(49)));
                consumer.setOldOwnershipIndicator(getCellValue(currentRow.getCell(50)));
                consumer.setSuitFiledWilfulDefault(getCellValue(currentRow.getCell(51)));
                consumer.setCreditFacilityStatus(getCellValue(currentRow.getCell(52)));
                consumer.setAssetClassification(getCellValue(currentRow.getCell(53)));
                consumer.setValueOfCollateral(getCellValue(currentRow.getCell(54)));
                consumer.setTypeOfCollateral(getCellValue(currentRow.getCell(55)));
                consumer.setCreditLimit(getCellValue(currentRow.getCell(56)));
                consumer.setCashLimit(getCellValue(currentRow.getCell(57)));
                consumer.setRateOfInterest(getCellValue(currentRow.getCell(58)));
                consumer.setRepaymentTenure(getCellValue(currentRow.getCell(59)));
                consumer.setEmiAmount(getCellValue(currentRow.getCell(60)));
                consumer.setWrittenOffAmountTotal(getCellValue(currentRow.getCell(61)));
                consumer.setWrittenOffPrincipalAmount(getCellValue(currentRow.getCell(62)));
                consumer.setSettlementAmt(getCellValue(currentRow.getCell(63)));
                consumer.setPaymentFrequency(getCellValue(currentRow.getCell(64)));
                consumer.setActualPaymentAmt(getCellValue(currentRow.getCell(65)));
                consumer.setOccupationCode(getCellValue(currentRow.getCell(66)));
                consumer.setIncome(getCellValue(currentRow.getCell(67)));
                consumer.setNetGrossIncomeIndicator(getCellValue(currentRow.getCell(68)));
                consumer.setMonthlyAnnualIncomeIndicator(getCellValue(currentRow.getCell(69)));
                consumer.setCkyc(getCellValue(currentRow.getCell(70)));
                consumer.setNregaCardNumber(getCellValue(currentRow.getCell(71)));

//           
                consumerList.add(consumer);
               
            }
            
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
        return consumerList;
    }
    
    
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return ""; // Return empty string for null cells
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
                }
                double numericValue = cell.getNumericCellValue();
                // Check if it's a whole number
                if (numericValue % 1 == 0) {
                    return String.valueOf((long) numericValue);
                } else {
                    return String.valueOf(numericValue);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return ""; // Return empty string for unsupported types
        }
    }

    
    
    private static boolean isRowEmpty(Row row) {
        if (row == null) return true;

        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false; // Row is not empty
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
