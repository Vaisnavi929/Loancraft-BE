package com.narvee.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.narvee.entity.LoanApplication;

public class ExcelUpload {
	
	public static boolean checkExcelFormat(MultipartFile file) {

		String contentType = file.getContentType();
		if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			return true;
		} else {
			return false;
		}
	}

	public static List<LoanApplication> convertExcelToLoanApplication(InputStream is) throws IOException{	
		List<LoanApplication> list=new ArrayList<>();		
		XSSFWorkbook workbook=new XSSFWorkbook(is);
		XSSFSheet sheet=workbook.getSheet("data");
		Iterator<Row> iterator=sheet.iterator();
		iterator.next();
	while (iterator.hasNext()) {
		Row nextRow=iterator.next();
		Iterator<Cell> cellIterator=nextRow.cellIterator();
		LoanApplication loanApplication=new LoanApplication();
		while (cellIterator.hasNext()) {
			Cell cell=cellIterator.next();
			int columnindex=cell.getColumnIndex();
			switch (columnindex) {
			case 0:
//				loanApplication.setAadharNumber((long)cell.getNumericCellValue());
				break;
			case 1:
				loanApplication.setAmaxnum((long)cell.getNumericCellValue());
				break;
			case 2:
				loanApplication.setApplicationReferenceId(cell.getStringCellValue());
				break;	
			case 3:
				loanApplication.setCategory(cell.getStringCellValue());
				break;	
			case 4:
				loanApplication.setDateOfBirth(cell.getDateCellValue());
				break;		
			case 5:
				loanApplication.setDrivingLicenseNumber(cell.getStringCellValue());
				break;
			case 6:
				loanApplication.setFatherName(cell.getStringCellValue());
				break;
			case 7:
				loanApplication.setFullname(cell.getStringCellValue());
				break;
			case 8:
				loanApplication.setGender(cell.getStringCellValue());
				break;
			case 9:
				loanApplication.setGstNumber(cell.getStringCellValue());
				break;
			case 10:
				loanApplication.setHigherQualification(cell.getStringCellValue());
				break;				
			case 11:
				loanApplication.setMailId(cell.getStringCellValue());
				break;
			case 12:
				loanApplication.setMaritalStatus(cell.getStringCellValue());
				break;
			case 13:
//				loanApplication.setMobileNumber((long)cell.getNumericCellValue());
				break;
			case 14:
//				loanApplication.setNoOfDependents(cell.getNumericCellValue());
				break;
			case 15:
				loanApplication.setPanCardNumber(cell.getStringCellValue());
				break;
			case 16:
				loanApplication.setPassportNumber(cell.getStringCellValue());
				break;
			case 17:
				loanApplication.setPassportNumber(cell.getStringCellValue());
				break;
			case 18:
				loanApplication.setReligion(cell.getStringCellValue());
				break;
			case 19:
				loanApplication.setStatus(cell.getStringCellValue());
				break;
			case 20:
				loanApplication.setTitle(cell.getStringCellValue());
				break;
			case 21:
				loanApplication.setVoterIdNumber(cell.getStringCellValue());
				break;
			}
				if (!list.contains(loanApplication)) {
					list.add(loanApplication);
				}
			}
			workbook.close();
			return list;
			}
	return list;
		
	}
}	
		

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
