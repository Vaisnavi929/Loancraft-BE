package com.narvee.urcf;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelReader1 {
    public static void main(String[] args) {
        String filePath = "UAT.xlsx"; // Update with the actual path
        
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet("Consumer_Format"); // Read the specified sheet
            Iterator<Row> rowIterator = sheet.iterator();
            
            List<ConsumerInfoDTO> consumerList = new ArrayList<>();
            Row headerRow = rowIterator.hasNext() ? rowIterator.next() : null;
            
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (isRowEmpty(row)) continue; // Skip empty rows
                
                ConsumerInfoDTO consumer = new ConsumerInfoDTO();
                consumer.setConsumerName(getCellValue(row.getCell(0)));
                consumer.setDateOfBirth(getCellValue(row.getCell(1)));
                consumer.setGender(getCellValue(row.getCell(2)));
                consumer.setIncomeTaxIdNumber(getCellValue(row.getCell(3)));
                consumer.setPassportNumber(getCellValue(row.getCell(4)));
                consumer.setPassportIssueDate(getCellValue(row.getCell(5)));
                consumer.setPassportExpiryDate(getCellValue(row.getCell(6)));
                consumer.setVoterIdNumber(getCellValue(row.getCell(7)));
                consumer.setDrivingLicenseNumber(getCellValue(row.getCell(8)));
                consumer.setDrivingLicenseIssueDate(getCellValue(row.getCell(9)));
                consumer.setDrivingLicenseExpiryDate(getCellValue(row.getCell(10)));
                consumer.setRationCardNumber(getCellValue(row.getCell(11)));
                consumer.setUniversalIdNumber(getCellValue(row.getCell(12)));
                consumer.setAdditionalId1(getCellValue(row.getCell(13)));
                consumer.setAdditionalId2(getCellValue(row.getCell(14)));
                consumer.setTelephoneNoMobile(getCellValue(row.getCell(15)));
                consumer.setTelephoneNoResidence(getCellValue(row.getCell(16)));
                consumer.setTelephoneNoOffice(getCellValue(row.getCell(17)));
                consumer.setExtensionOffice(getCellValue(row.getCell(18)));
                consumer.setTelephoneNoOther(getCellValue(row.getCell(19)));
                consumer.setExtensionOther(getCellValue(row.getCell(20)));
                consumer.setEmailId1(getCellValue(row.getCell(21)));
                consumer.setEmailId2(getCellValue(row.getCell(22)));
                consumer.setAddress1(getCellValue(row.getCell(23)));
                consumer.setStateCode1(getCellValue(row.getCell(24)));
                consumer.setPinCode1(getCellValue(row.getCell(25)));
                consumer.setAddressCategory1(getCellValue(row.getCell(26)));
                consumer.setResidenceCode1(getCellValue(row.getCell(27)));
                consumer.setAddress2(getCellValue(row.getCell(28)));
                consumer.setStateCode2(getCellValue(row.getCell(29)));
                consumer.setPinCode2(getCellValue(row.getCell(30)));
                consumer.setAddressCategory2(getCellValue(row.getCell(31)));
                consumer.setResidenceCode2(getCellValue(row.getCell(32)));
                consumer.setCurrentNewMemberCode(getCellValue(row.getCell(33)));
                consumer.setCurrentNewMemberShortName(getCellValue(row.getCell(34)));
                consumer.setCurrNewAccountNo(getCellValue(row.getCell(35)));
                consumer.setAccountType(getCellValue(row.getCell(36)));
                consumer.setOwnershipIndicator(getCellValue(row.getCell(37)));
                consumer.setDateOpenedDisbursed(getCellValue(row.getCell(38)));
                consumer.setDateOfLastPayment(getCellValue(row.getCell(39)));
                consumer.setDateClosed(getCellValue(row.getCell(40)));
                consumer.setDateReported(getCellValue(row.getCell(41)));
                consumer.setHighCreditSanctionedAmt(getCellValue(row.getCell(42)));
                consumer.setCurrentBalance(getCellValue(row.getCell(43)));
                consumer.setAmountOverdue(getCellValue(row.getCell(44)));
                consumer.setNoOfDaysPastDue(getCellValue(row.getCell(45)));
                consumer.setOldMbrCode(getCellValue(row.getCell(46)));
                consumer.setOldMbrShortName(getCellValue(row.getCell(47)));
                consumer.setOldAccNo(getCellValue(row.getCell(48)));
                consumer.setOldAccType(getCellValue(row.getCell(49)));
                consumer.setOldOwnershipIndicator(getCellValue(row.getCell(50)));
                consumer.setSuitFiledWilfulDefault(getCellValue(row.getCell(51)));
                consumer.setCreditFacilityStatus(getCellValue(row.getCell(52)));
                consumer.setAssetClassification(getCellValue(row.getCell(53)));
                consumer.setValueOfCollateral(getCellValue(row.getCell(54)));
                consumer.setTypeOfCollateral(getCellValue(row.getCell(55)));
                consumer.setCreditLimit(getCellValue(row.getCell(56)));
                consumer.setCashLimit(getCellValue(row.getCell(57)));
                consumer.setRateOfInterest(getCellValue(row.getCell(58)));
                consumer.setRepaymentTenure(getCellValue(row.getCell(59)));
                consumer.setEmiAmount(getCellValue(row.getCell(60)));
                consumer.setWrittenOffAmountTotal(getCellValue(row.getCell(61)));
                consumer.setWrittenOffPrincipalAmount(getCellValue(row.getCell(62)));
                consumer.setSettlementAmt(getCellValue(row.getCell(63)));
                consumer.setPaymentFrequency(getCellValue(row.getCell(64)));
                consumer.setActualPaymentAmt(getCellValue(row.getCell(65)));
                consumer.setOccupationCode(getCellValue(row.getCell(66)));
                consumer.setIncome(getCellValue(row.getCell(67)));
                consumer.setNetGrossIncomeIndicator(getCellValue(row.getCell(68)));
                consumer.setMonthlyAnnualIncomeIndicator(getCellValue(row.getCell(69)));
                consumer.setCkyc(getCellValue(row.getCell(70)));
                consumer.setNregaCardNumber(getCellValue(row.getCell(71)));

                consumerList.add(consumer);
            }
            
            // Print consumer data for verification
            for (ConsumerInfoDTO consumer : consumerList) {
                System.out.println(consumer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";

        DataFormatter dataFormatter = new DataFormatter();

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat excelDateFormat = new SimpleDateFormat("ddMMyyyy");
                    return excelDateFormat.format(cell.getDateCellValue());
                }
                return dataFormatter.formatCellValue(cell);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return dataFormatter.formatCellValue(cell);
            default:
                return "";
        }
    }
}
