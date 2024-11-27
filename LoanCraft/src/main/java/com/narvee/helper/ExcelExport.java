package com.narvee.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.narvee.request.dto.GenerationExcelDTO;

@Component
public class ExcelExport {

    public ByteArrayOutputStream exportToExcel(GenerationExcelDTO excelDTO) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
        	
            Sheet sheet = workbook.createSheet("Loan Application Data");
            

            String[] headers = {
                    "Consumer Name", "Date of Birth", "Gender", "Pan Card Number", "Passport Number",
                    "Passport Issue Date", "Voter ID Number", "Driving License Number",
                    "Driving License Issue Date", "Driving License Expiry Date", "Ration Card Number",
                    "Aadhar Number", "Additional ID 1", "Additional ID 2", "Mobile Number",
                    "Telephone Residence", "Telephone Office", "Extension Office", "Telephone Other",
                    "Extension Other", "Mail ID", "Mail ID 2", "Address", "State Code",
                    "Pincode", "Address Category", "Residence Code", "Address 2", "State Code 2",
                    "Pincode 2", "Address Category 2", "Residence Code 2", "Current Member Code",
                    "Member Short Name", "Account No", "Account Type", "Ownership Indicator",
                    "Date Opened/Disbursed", "Last Payment Date", "Date Closed", "Date Reported",
                    "High Credit Amount", "Current Balance", "Amount Overdue", "Days Past Due",
                    "Old Member Code", "Old Member Short Name", "Old Account No", "Old Account Type",
                    "Old Ownership Indicator", "Suit Filed/Wilful Default", "Credit Facility Status",
                    "Asset Classification", "Collateral Value", "Collateral Type", "Credit Limit",
                    "Cash Limit", "Interest Rate", "Repayment Tenure", "EMI Amount",
                    "Written-Off Total Amount", "Written-Off Principal Amount", "Settlement Amount",
                    "Payment Frequency", "Actual Payment Amount", "Occupation Code", "Monthly Income",
                    "Net/Gross Income Indicator", "Monthly/Annual Income Indicator"
                };
            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(getHeaderCellStyle(workbook));
            }

            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue(excelDTO.getConsumerName());
            dataRow.createCell(1).setCellValue(excelDTO.getDateOfBirth().toString());
            dataRow.createCell(2).setCellValue(excelDTO.getGender());
            dataRow.createCell(3).setCellValue(excelDTO.getIncomeTaxIdNumber());
            dataRow.createCell(4).setCellValue(excelDTO.getPassportNumber());
            dataRow.createCell(5).setCellValue(excelDTO.getPassportIssueDate());
            dataRow.createCell(6).setCellValue(excelDTO.getVoterIdNumber());
            dataRow.createCell(7).setCellValue(excelDTO.getDrivingLicenseNumber());
            dataRow.createCell(8).setCellValue(excelDTO.getDrivingLicenseIssueDate());
            dataRow.createCell(9).setCellValue(excelDTO.getDrivingLicenseExpiryDate());
            dataRow.createCell(10).setCellValue(excelDTO.getRationCardNumber());
            dataRow.createCell(11).setCellValue(excelDTO.getUniversalIdNumber());
            dataRow.createCell(12).setCellValue(excelDTO.getAdditionalId1());
            dataRow.createCell(13).setCellValue(excelDTO.getAdditionalId2());
            dataRow.createCell(14).setCellValue(excelDTO.getTelephoneNoMobile());
            dataRow.createCell(15).setCellValue(excelDTO.getTelephoneNoResidence());
            dataRow.createCell(16).setCellValue(excelDTO.getTelephoneNoOffice());
            dataRow.createCell(17).setCellValue(excelDTO.getExtensionOffice());
            dataRow.createCell(18).setCellValue(excelDTO.getTelephoneNoOther());
            dataRow.createCell(19).setCellValue(excelDTO.getExtensionOther());
            dataRow.createCell(20).setCellValue(excelDTO.getEmailId1());
            dataRow.createCell(21).setCellValue(excelDTO.getEmailId2());
            dataRow.createCell(22).setCellValue(excelDTO.getAddress1());
            dataRow.createCell(23).setCellValue(excelDTO.getStateCode1());
            dataRow.createCell(24).setCellValue(excelDTO.getPinCode1());
            dataRow.createCell(25).setCellValue(excelDTO.getAddressCategory1());
            dataRow.createCell(26).setCellValue(excelDTO.getResidenceCode1());
            dataRow.createCell(27).setCellValue(excelDTO.getAddress2());
            dataRow.createCell(28).setCellValue(excelDTO.getStateCode2());
            dataRow.createCell(29).setCellValue(excelDTO.getPinCode2());
            dataRow.createCell(30).setCellValue(excelDTO.getAddressCategory2());
            dataRow.createCell(31).setCellValue(excelDTO.getResidenceCode2());
            dataRow.createCell(32).setCellValue(excelDTO.getCurrentNewMemberCode());
            dataRow.createCell(33).setCellValue(excelDTO.getCurrentNewMemberShortName());
            dataRow.createCell(34).setCellValue(excelDTO.getCurrNewAccountNo());
            dataRow.createCell(35).setCellValue(excelDTO.getAccountType());
            dataRow.createCell(36).setCellValue(excelDTO.getOwnershipIndicator());
            dataRow.createCell(37).setCellValue(excelDTO.getDateOpenedDisbursed().toString());
            dataRow.createCell(38).setCellValue(excelDTO.getDateOfLastPayment());
            dataRow.createCell(39).setCellValue(excelDTO.getDateClosed());
            dataRow.createCell(40).setCellValue(excelDTO.getDateReported());
            dataRow.createCell(41).setCellValue(excelDTO.getHighCreditSanctionedAmt());
            dataRow.createCell(42).setCellValue(excelDTO.getCurrentBalance());
            dataRow.createCell(43).setCellValue(excelDTO.getAmtOverdue());
            dataRow.createCell(44).setCellValue(excelDTO.getNoOfDaysPastDue());
            dataRow.createCell(45).setCellValue(excelDTO.getOldMbrCode());
            dataRow.createCell(46).setCellValue(excelDTO.getOldMbrShortName());
            dataRow.createCell(47).setCellValue(excelDTO.getOldAccNo());
            dataRow.createCell(48).setCellValue(excelDTO.getOldAccType());
            dataRow.createCell(49).setCellValue(excelDTO.getOldOwnershipIndicator());
            dataRow.createCell(50).setCellValue(excelDTO.getSuitFiledWilfulDefault());
            dataRow.createCell(51).setCellValue(excelDTO.getCreditFacilityStatus());
            dataRow.createCell(52).setCellValue(excelDTO.getAssetClassification());
            dataRow.createCell(53).setCellValue(excelDTO.getValueOfCollateral());
            dataRow.createCell(54).setCellValue(excelDTO.getTypeOfCollateral());
            dataRow.createCell(55).setCellValue(excelDTO.getCreditLimit());
            dataRow.createCell(56).setCellValue(excelDTO.getCashLimit());
            dataRow.createCell(57).setCellValue(excelDTO.getRateOfInterest());
            dataRow.createCell(58).setCellValue(excelDTO.getRepaymentTenure());
            dataRow.createCell(59).setCellValue(excelDTO.getEmiAmount());
            dataRow.createCell(60).setCellValue(excelDTO.getWrittenOffAmountTotal());
            dataRow.createCell(61).setCellValue(excelDTO.getWrittenOffPrincipalAmount());
            dataRow.createCell(62).setCellValue(excelDTO.getSettlementAmt());
            dataRow.createCell(63).setCellValue(excelDTO.getPaymentFrequency());
            dataRow.createCell(64).setCellValue(excelDTO.getActualPaymentAmt());
            dataRow.createCell(65).setCellValue(excelDTO.getOccupationCode());
            dataRow.createCell(66).setCellValue(excelDTO.getIncome());
            dataRow.createCell(67).setCellValue(excelDTO.getNetGrossIncomeIndicator());
            dataRow.createCell(68).setCellValue(excelDTO.getMonthlyAnnualIncomeIndicator());


            // Adjust column widths
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to ByteArrayOutputStream
            workbook.write(out);
            return out;
        }
    }

    private CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }
   
   
}