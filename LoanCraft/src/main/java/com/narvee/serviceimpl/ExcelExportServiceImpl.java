//package com.narvee.serviceimpl;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.CellType;
//import org.apache.poi.ss.usermodel.DataFormatter;
//import org.apache.poi.ss.usermodel.Font;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.InputStreamResource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.narvee.Service.GeneratexcelService;
//import com.narvee.entity.Applicant;
//import com.narvee.entity.BankingDetails;
//import com.narvee.entity.CurrentAddress;
//import com.narvee.entity.DetailsOfLoanAppliedFor;
//import com.narvee.entity.EmploymentDetails;
//import com.narvee.entity.ExistingLoanDetails;
//import com.narvee.entity.IncomeDetails;
//import com.narvee.entity.LoanApplication;
//import com.narvee.entity.PermanentAddress;
//import com.narvee.entity.PropertyDetails;
//import com.narvee.entity.Reference;
//import com.narvee.helper.ExcelExport;
//import com.narvee.repository.LoanApplicationRepository;
//import com.narvee.request.dto.GenerationExcelDTO;
//
//@Service
//public class ExcelExportServiceImpl implements GeneratexcelService {
//
//	@Autowired
//	private ExcelExport excelExport;
//
//	@Autowired
//	private LoanApplicationRepository loanApplicationRepository;
//
//	@Override
//	public ResponseEntity<InputStreamResource> generateExcel(Long loanId) throws IOException {
//		Optional<LoanApplication> loanAppOpt = loanApplicationRepository.findById(loanId);
//
//		if (loanAppOpt.isEmpty()) {
//			throw new IllegalArgumentException("Loan Application with ID " + loanId + " not found.");
//		}
//
//		LoanApplication loanApplication = loanAppOpt.get();
//		BankingDetails bankingDetails = loanApplication.getBankingDetails();
//		PermanentAddress permanentAddress = loanApplication.getPermanentAddress();
//
//		GenerationExcelDTO excelDTO = new GenerationExcelDTO();
//		excelDTO.setDrivingLicenseNumber(loanApplication.getDrivingLicenseNumber());
//		excelDTO.setPassportNumber(loanApplication.getPassportNumber());
//		excelDTO.setConsumerName(loanApplication.getFullname());
//		excelDTO.setDateOfBirth(loanApplication.getDateOfBirth());
//		excelDTO.setGender(loanApplication.getGender());
//		excelDTO.setVoterIdNumber(loanApplication.getVoterIdNumber());
////		excelDTO.setTelephoneNoMobile(loanApplication.getMobileNumber());
//		excelDTO.setEmailId1(loanApplication.getMailId());
//		excelDTO.setCurrNewAccountNo(bankingDetails.getAccountNo());
//		excelDTO.setDateOpenedDisbursed(bankingDetails.getDateOfOpening());
////		excelDTO.setCurrentBalance(bankingDetails.getCurrent());
//		excelDTO.setAddress1(permanentAddress.getAddress());
//		excelDTO.setPinCode1(permanentAddress.getPincode());
////		excelDTO.setStateCode1(permanentAddress.getStatecode());
//		ByteArrayOutputStream out = excelExport.exportToExcel(excelDTO);
//		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Content-Disposition", "attachment filename=loan_application.xlsx");
//
//		return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
//				.body(new InputStreamResource(in));
//	}
//
//	@Override
//	public ResponseEntity<InputStreamResource> generateExcelSheet(Long loanId) throws IOException {
//		Optional<LoanApplication> loanAppOpt = loanApplicationRepository.findById(loanId);
//
//		if (loanAppOpt.isEmpty()) {
//			throw new IllegalArgumentException("Loan Application with ID " + loanId + " not found.");
//		}
//
//		LoanApplication loanApplication = loanAppOpt.get();
//		BankingDetails bankingDetails = loanApplication.getBankingDetails();
//		PermanentAddress permanentAddress = loanApplication.getPermanentAddress();
//
//		GenerationExcelDTO excelDTOs = new GenerationExcelDTO();
//		excelDTOs.setDrivingLicenseNumber(loanApplication.getDrivingLicenseNumber());
//		excelDTOs.setPassportNumber(loanApplication.getPassportNumber());
//		excelDTOs.setConsumerName(loanApplication.getFullname());
//		excelDTOs.setDateOfBirth(loanApplication.getDateOfBirth());
//		excelDTOs.setGender(loanApplication.getGender());
//		excelDTOs.setVoterIdNumber(loanApplication.getVoterIdNumber());
////		excelDTOs.setTelephoneNoMobile(loanApplication.getMobileNumber());
//		excelDTOs.setEmailId1(loanApplication.getMailId());
//		excelDTOs.setCurrNewAccountNo(bankingDetails.getAccountNo());
//		excelDTOs.setDateOpenedDisbursed(bankingDetails.getDateOfOpening());
////		excelDTOs.setCurrentBalance(bankingDetails.getCurrent());
//		excelDTOs.setAddress1(permanentAddress.getAddress());
//		excelDTOs.setPinCode1(permanentAddress.getPincode());
////		excelDTOs.setStateCode1(permanentAddress.getStatecode());
//		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//			ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
//			Workbook workbook = new XSSFWorkbook();
//
//			Sheet sheet = workbook.createSheet("Loan Application Data");
//			CellStyle boldStyle = workbook.createCellStyle();
//			Font boldFont = workbook.createFont();
//			boldFont.setBold(true);
//			boldStyle.setFont(boldFont);
//
//			String[][] data = { { "Consumer Name", excelDTOs.getConsumerName() },
//					{ "Date of Birth",
//							excelDTOs.getDateOfBirth() != null ? excelDTOs.getDateOfBirth().toString() : "" },
//					{ "Gender", excelDTOs.getGender() }, { "Passport Number", excelDTOs.getPassportNumber() },
//					{ "Passport Issue Date",
//							excelDTOs.getPassportIssueDate() != null ? excelDTOs.getPassportIssueDate().toString()
//									: "" },
//					{ "Voter ID Number", excelDTOs.getVoterIdNumber() },
//					{ "Driving License Number", excelDTOs.getDrivingLicenseNumber() },
//					{ "Driving License Issue Date",
//							excelDTOs.getDrivingLicenseIssueDate() != null
//									? excelDTOs.getDrivingLicenseIssueDate().toString()
//									: "" },
//					{ "Driving License Expiry Date",
//							excelDTOs.getDrivingLicenseExpiryDate() != null
//									? excelDTOs.getDrivingLicenseExpiryDate().toString()
//									: "" },
//					{ "Ration Card Number", excelDTOs.getRationCardNumber() },
//					{ "Additional ID 1", excelDTOs.getAdditionalId1() },
//					{ "Additional ID 2", excelDTOs.getAdditionalId2() },
//					{ "Mobile Number", excelDTOs.getTelephoneNoMobile()!=null ? excelDTOs.getTelephoneNoMobile().toString():" "},
//					{ "Telephone Residence", excelDTOs.getTelephoneNoResidence() },
//					{ "Telephone Office", excelDTOs.getTelephoneNoOffice() },
//					{ "Extension Office", excelDTOs.getExtensionOffice() },
//					{ "Telephone Other", excelDTOs.getTelephoneNoOther() },
//					{ "Extension Other", excelDTOs.getExtensionOther() }, { "Mail ID", excelDTOs.getEmailId1() },
//					{ "Mail ID 2", excelDTOs.getEmailId2() }, { "Address", excelDTOs.getAddress1() },
//					{ "State Code", excelDTOs.getStateCode1() != null ? excelDTOs.getStateCode1().toString() : "" },
//					{ "Pincode", excelDTOs.getPinCode1() != null ? excelDTOs.getPinCode1().toString() : "" },
//					{ "Address Category", excelDTOs.getAddressCategory1() },
//					{ "Residence Code", excelDTOs.getResidenceCode1() }, { "Address 2", excelDTOs.getAddress2() },
//					{ "State Code 2", excelDTOs.getStateCode2() },
//					{ "Pincode 2", excelDTOs.getPinCode2() != null ? excelDTOs.getPinCode2().toString() : "" },
//					{ "Address Category 2", excelDTOs.getAddressCategory2() },
//					{ "Residence Code 2", excelDTOs.getResidenceCode2() },
//					{ "Current Member Code", excelDTOs.getCurrentNewMemberCode() },
//					{ "Member Short Name", excelDTOs.getCurrentNewMemberShortName() },
//					{ "Account No",
//							excelDTOs.getCurrNewAccountNo() != null ? excelDTOs.getCurrNewAccountNo().toString() : "" },
//					{ "Account Type", excelDTOs.getAccountType() },
//					{ "Ownership Indicator", excelDTOs.getOwnershipIndicator() },
//					{ "Date Opened/Disbursed",
//							excelDTOs.getDateOpenedDisbursed() != null ? excelDTOs.getDateOpenedDisbursed().toString()
//									: "" },
//					{ "Last Payment Date",
//							excelDTOs.getDateOfLastPayment() != null ? excelDTOs.getDateOfLastPayment().toString()
//									: "" },
//					{ "Date Closed", excelDTOs.getDateClosed() != null ? excelDTOs.getDateClosed().toString() : "" },
//					{ "Date Reported",
//							excelDTOs.getDateReported() != null ? excelDTOs.getDateReported().toString() : "" },
//					{ "High Credit Amount",
//							excelDTOs.getHighCreditSanctionedAmt() != null
//									? excelDTOs.getHighCreditSanctionedAmt().toString()
//									: "" },
//					{ "Current Balance",
//							excelDTOs.getCurrentBalance() != null ? excelDTOs.getCurrentBalance().toString() : "" },
//					{ "Amount Overdue", excelDTOs.getAmtOverdue() != null ? excelDTOs.getAmtOverdue().toString() : "" },
//					{ "Days Past Due",
//							excelDTOs.getNoOfDaysPastDue() != null ? excelDTOs.getNoOfDaysPastDue().toString() : "" },
//					{ "Old Member Code", excelDTOs.getOldMbrCode() },
//					{ "Old Member Short Name", excelDTOs.getOldMbrShortName() },
//					{ "Old Account No", excelDTOs.getOldAccNo() }, { "Old Account Type", excelDTOs.getOldAccType() },
//					{ "Old Ownership Indicator", excelDTOs.getOldOwnershipIndicator() },
//					{ "Suit Filed/Wilful Default", excelDTOs.getSuitFiledWilfulDefault() },
//					{ "Credit Facility Status", excelDTOs.getCreditFacilityStatus() },
//					{ "Asset Classification", excelDTOs.getAssetClassification() },
//					{ "Collateral Value",
//							excelDTOs.getValueOfCollateral() != null ? excelDTOs.getValueOfCollateral().toString()
//									: "" },
//					{ "Collateral Type", excelDTOs.getTypeOfCollateral() },
//					{ "Credit Limit", excelDTOs.getCreditLimit() != null ? excelDTOs.getCreditLimit().toString() : "" },
//					{ "Cash Limit", excelDTOs.getCashLimit() != null ? excelDTOs.getCashLimit().toString() : "" },
//					{ "Interest Rate",
//							excelDTOs.getRateOfInterest() != null ? excelDTOs.getRateOfInterest().toString() : "" },
//					{ "Repayment Tenure",
//							excelDTOs.getRepaymentTenure() != null ? excelDTOs.getRepaymentTenure().toString() : "" },
//					{ "EMI Amount", excelDTOs.getEmiAmount() != null ? excelDTOs.getEmiAmount().toString() : "" },
//					{ "Written-Off Total Amount",
//							excelDTOs.getWrittenOffAmountTotal() != null
//									? excelDTOs.getWrittenOffAmountTotal().toString()
//									: "" },
//					{ "Written-Off Principal Amount",
//							excelDTOs.getWrittenOffPrincipalAmount() != null
//									? excelDTOs.getWrittenOffPrincipalAmount().toString()
//									: "" },
//					{ "Settlement Amount",
//							excelDTOs.getSettlementAmt() != null ? excelDTOs.getSettlementAmt().toString() : "" },
//					{ "Payment Frequency", excelDTOs.getPaymentFrequency() },
//					{ "Actual Payment Amount",
//							excelDTOs.getActualPaymentAmt() != null ? excelDTOs.getActualPaymentAmt().toString() : "" },
//					{ "Occupation Code", excelDTOs.getOccupationCode() },
//					{ "Monthly Income", excelDTOs.getIncome() != null ? excelDTOs.getIncome().toString() : "" },
//					{ "Net/Gross Income Indicator", excelDTOs.getNetGrossIncomeIndicator() },
//					{ "Monthly/Annual Income Indicator", excelDTOs.getMonthlyAnnualIncomeIndicator() } };
//			for (int i = 0; i < data.length; i++) {
//				Row row = sheet.createRow(i);
//				Cell headerCell = row.createCell(0);
//				headerCell.setCellValue(data[i][0]);
//				headerCell.setCellStyle(boldStyle);
//				row.createCell(1).setCellValue(data[i][1]);
//			}
//
//			workbook.write(out);
//			ByteArrayInputStream input = new ByteArrayInputStream(out.toByteArray());
//
//			HttpHeaders headers = new HttpHeaders();
//			headers.add("Content-Disposition", "attachment; filename=LoanApplicationData.xlsx");
//
//			return ResponseEntity.ok().headers(headers)
//					.contentType(MediaType
//							.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
//					.body(new InputStreamResource(input));
//		}
//
//	}
//
//	;
//
//	@Override
//	public String processExcel(MultipartFile file) throws IOException {
//		Workbook workbook = new XSSFWorkbook(file.getInputStream());
//		Sheet sheet = workbook.getSheetAt(0); // Assuming the first sheet
//
//		Row headerRow = sheet.getRow(0); // First row as header
//		Map<String, Integer> columnMapping = new HashMap<>();
//		for (Cell cell : headerRow) {
//			columnMapping.put(cell.getStringCellValue(), cell.getColumnIndex());
//		}
//
//		DataFormatter dataFormatter = new DataFormatter(); // Formatter for converting cell values to strings
//		List<LoanApplication> loanApplications = new ArrayList<>();
//
//		// Process rows starting from the second row (index 1)
//		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//			Row row = sheet.getRow(i);
//			
//			LoanApplication loanApplication = new LoanApplication();
//			CurrentAddress currentAddress = new CurrentAddress();
//			PermanentAddress permanentAddress = new PermanentAddress();
//			BankingDetails bankingDetails = new BankingDetails();
//			Applicant applicants = new Applicant();
//			ArrayList<Applicant> applicants2=new ArrayList<>();
//			DetailsOfLoanAppliedFor detailsOfLoanAppliedFor = new DetailsOfLoanAppliedFor();
//			EmploymentDetails employmentDetails = new EmploymentDetails();
//			IncomeDetails incomeDetails = new IncomeDetails();
//			PropertyDetails propertyDetails = new PropertyDetails();
//			ArrayList<PropertyDetails> propertyDetails2=new ArrayList<>();
//			Reference reference = new Reference();
//			ArrayList<Reference> reference1=new ArrayList<>();
//			ExistingLoanDetails existingLoanDetails = new ExistingLoanDetails();
//			ArrayList<ExistingLoanDetails> existingLoanDetails2=new ArrayList<>();
//
//			// Access columns by name
//			String title = getCellValue(row, columnMapping.get("title"));
//		
//			String name = getCellValue(row, columnMapping.get("full Name"));
//		
//			String fathername = getCellValue(row, columnMapping.get("father Name"));
//			Date dateOfBirth = getDateCellValue(row, columnMapping.get("dateOfBirth"));
//			String gender = getCellValue(row, columnMapping.get("gender"));
//
//			String maritalstatus = getCellValue(row, columnMapping.get("maritalStatus"));
//			String mailid = getCellValue(row, columnMapping.get("mailid"));
//			String higherqualification = getCellValue(row, columnMapping.get("higherQualification"));
//			Long noofdependents = getLongCellValue(row, columnMapping.get("noOfDependents"));
//			String category = getCellValue(row, columnMapping.get("category"));
//			String religion = getCellValue(row, columnMapping.get("religion"));
//			String panCardNumber = getCellValue(row, columnMapping.get("panCardNumber"));
//			String aadharNumber = getCellValue(row, columnMapping.get("aadharNumber"));
//		
//			String passportNumber = getCellValue(row, columnMapping.get("passportNumber"));
//			String voterIdNumber = getCellValue(row, columnMapping.get("voterIdNumber"));
//			String drivingLicenseNumber = getCellValue(row, columnMapping.get("drivingLicenseNumber"));
//			String gstNumber = getCellValue(row, columnMapping.get("gstNumber"));
//			String mobileNumber = getCellValue(row, columnMapping.get("mobileNumber"));
//			
//			String status = getCellValue(row, columnMapping.get("status"));
//			loanApplication.setTitle(title);
//			loanApplication.setFullname(name);
//			loanApplication.setFatherName(fathername);
//			loanApplication.setDateOfBirth(dateOfBirth);
//			loanApplication.setGender(gender);
//			loanApplication.setMaritalStatus(maritalstatus);
//			loanApplication.setMailId(mailid);
//			loanApplication.setHigherQualification(higherqualification);
//			loanApplication.setNoOfDependents(noofdependents);
//			loanApplication.setCategory(category);
//			loanApplication.setReligion(religion);
//			loanApplication.setPanCardNumber(panCardNumber);
//			loanApplication.setAadharNumber(aadharNumber);
//			loanApplication.setPassportNumber(passportNumber);
//			loanApplication.setVoterIdNumber(voterIdNumber);
//			loanApplication.setDrivingLicenseNumber(drivingLicenseNumber);
//			loanApplication.setGstNumber(gstNumber);
//			loanApplication.setMobileNumber(mobileNumber);
//			loanApplication.setStatus(status);
//
//			String address = getCellValue(row, columnMapping.get("address"));
//			String city = getCellValue(row, columnMapping.get("city"));
//			String state = getCellValue(row, columnMapping.get("state"));
//			String country = getCellValue(row, columnMapping.get("country"));
//			String landmark = getCellValue(row, columnMapping.get("landmark"));
//			String location = getCellValue(row, columnMapping.get("location"));
//			Long stayedOfYears = getLongCellValue(row, columnMapping.get("stayedOfYears"));
//			Long pincode = getLongCellValue(row, columnMapping.get("pincode"));
//			Long statecode = getLongCellValue(row, columnMapping.get("statecode"));
//			currentAddress.setAddress(address);
//			currentAddress.setCity(city);
//			currentAddress.setState(state);
//			currentAddress.setCountry(country);
//			currentAddress.setLandmark(landmark);
//			currentAddress.setLocation(location);
//			currentAddress.setStayedOfYears(stayedOfYears);
//			currentAddress.setPincode(pincode);
//			currentAddress.setStatecode(statecode);
//			loanApplication.setCurrentAddress(currentAddress);
//
//			
//			String Paddress = getCellValue(row, columnMapping.get("Paddress"));
//			String Pcity = getCellValue(row, columnMapping.get("Pcity"));
//			String Pstate = getCellValue(row, columnMapping.get("Pstate"));
//			String Pcountry = getCellValue(row, columnMapping.get("Pcountry"));
//			String Plocation = getCellValue(row, columnMapping.get("Plocation"));
//			String Plandmark = getCellValue(row, columnMapping.get("Plandmark"));
//			Long PstayedOfYears = getLongCellValue(row, columnMapping.get("PstayedOfYears"));
//			Long Ppincode = getLongCellValue(row, columnMapping.get("Ppincode"));
//			Long Pstatecode = getLongCellValue(row, columnMapping.get("Pstatecode"));
//			permanentAddress.setAddress(Paddress);
//			permanentAddress.setCity(Pcity);
//			permanentAddress.setState(Pstate);
//			permanentAddress.setCountry(Pcountry);
//			permanentAddress.setLocation(Plocation);
//			permanentAddress.setLandmark(Plandmark);
//			permanentAddress.setStayedOfYears(PstayedOfYears);
//			permanentAddress.setPincode(Ppincode);
//			permanentAddress.setStatecode(Pstatecode);
//			loanApplication.setPermanentAddress(permanentAddress);
//
//			
//			String accountHolder = getCellValue(row, columnMapping.get("accountHolder"));
//			String branchName = getCellValue(row, columnMapping.get("branchName"));
//			Long accountNo = getLongCellValue(row, columnMapping.get("accountNo"));
//			Double salary = getDoubleCellValue(row, columnMapping.get("salary"));
//			Date dateOfOpening = getDateCellValue(row, columnMapping.get("dateOfOpening"));
//			String applicant = getCellValue(row, columnMapping.get("applicant"));
//			String ifscCode = getCellValue(row, columnMapping.get("ifscCode"));
//			String current = getCellValue(row, columnMapping.get("current"));
//			bankingDetails.setAccountHolder(accountHolder);
//			bankingDetails.setBranchName(branchName);
//			bankingDetails.setAccountNo(accountNo);
////			bankingDetails.setSalary(salary);
//			bankingDetails.setDateOfOpening(dateOfOpening);
////			bankingDetails.setApplicant(applicant);
//			bankingDetails.setIfscCode(ifscCode);
////			bankingDetails.setCurrent(current);
//			loanApplication.setBankingDetails(bankingDetails);
//
//			String proof = getCellValue(row, columnMapping.get("proof"));
//			String document = getCellValue(row, columnMapping.get("document"));
//			Long number = getLongCellValue(row, columnMapping.get("number"));
//			Date issueDate = getDateCellValue(row, columnMapping.get("issueDate"));
//			Date expDate = getDateCellValue(row, columnMapping.get("expDate"));
//			applicants.setProof(proof);
//			applicants.setDocument(document);
//			applicants.setNumber(number);
//			applicants.setIssueDate(issueDate);
//			applicants.setExpDate(expDate);
//			applicants2.add(applicants);
//			loanApplication.setApplicant(applicants2);
//
//			String purposeOfLoan = getCellValue(row, columnMapping.get("purposeOfLoan"));
//			Double loanAmountRequired = getDoubleCellValue(row, columnMapping.get("loanAmountRequired"));
//			Long tenure = getLongCellValue(row, columnMapping.get("tenure"));
//			String repaymentMode = getCellValue(row, columnMapping.get("repaymentMode"));
//			String earlierLoanInBeacon = getCellValue(row, columnMapping.get("earlierLoanInBeacon"));
//
//			detailsOfLoanAppliedFor.setPurposeOfLoan(purposeOfLoan);
//			detailsOfLoanAppliedFor.setLoanAmountRequired(loanAmountRequired);
//			detailsOfLoanAppliedFor.setTenure(tenure);
//			detailsOfLoanAppliedFor.setRepaymentMode(repaymentMode);
//			detailsOfLoanAppliedFor.setEarlierLoanInBeacon(earlierLoanInBeacon);
//			System.err.println(detailsOfLoanAppliedFor);
//			loanApplication.setDetailsOfLoanAppliedFor(detailsOfLoanAppliedFor);
//
//			String employmentType = getCellValue(row, columnMapping.get("employmentType"));
//			Double empsalary = getDoubleCellValue(row, columnMapping.get("empsalary"));
//			Long yearsinEmployment = getLongCellValue(row, columnMapping.get("yearsinEmployment"));
//			String companyName = getCellValue(row, columnMapping.get("companyName"));
//			String companyAdrees = getCellValue(row, columnMapping.get("companyAdrees"));
//			Long companypincode = getLongCellValue(row, columnMapping.get("companypincode"));
//			String gstDetails = getCellValue(row, columnMapping.get("gstDetails"));
//
//			employmentDetails.setEmploymentType(employmentType);
//			employmentDetails.setSalary(empsalary);
//			employmentDetails.setYearsinEmployment(yearsinEmployment);
//			employmentDetails.setCompanyName(companyName);
//			employmentDetails.setCompanyAdrees(companyAdrees);
//			employmentDetails.setPincode(companypincode);
//			employmentDetails.setGstDetails(gstDetails);
//			loanApplication.setEmploymentDetails(employmentDetails);
//			
//
//			
//			Double applicantIncome = getDoubleCellValue(row, columnMapping.get("applicantIncome"));
//			Double otherIncome1 = getDoubleCellValue(row, columnMapping.get("otherIncome1"));
//			Double otherIncome2 = getDoubleCellValue(row, columnMapping.get("otherIncome2"));
//			Double otherIncome3 = getDoubleCellValue(row, columnMapping.get("otherIncome3"));
//			Double totalIncome = getDoubleCellValue(row, columnMapping.get("totalIncome"));
//			Double householdTotal = getDoubleCellValue(row, columnMapping.get("householdTotal"));
//			Double expenditureRent = getDoubleCellValue(row, columnMapping.get("expenditureRent"));			
//			Double expenditureLoan = getDoubleCellValue(row, columnMapping.get("expenditureLoan"));
//			Double householdExpenditure = getDoubleCellValue(row, columnMapping.get("householdExpenditure"));
//			String education = getCellValue(row, columnMapping.get("education"));
//			String others = getCellValue(row, columnMapping.get("others"));
//			Double netIncome = getDoubleCellValue(row, columnMapping.get("netIncome"));
//			
//
//		
//			incomeDetails.setApplicantIncome(applicantIncome);
//			incomeDetails.setOtherIncome1(otherIncome1);
//			incomeDetails.setOtherIncome2(otherIncome2);
//			incomeDetails.setOtherIncome3(otherIncome3);
//			incomeDetails.setTotalIncome(totalIncome);
//			incomeDetails.setHouseholdTotal(householdTotal);
//			incomeDetails.setExpenditureRent(expenditureRent);
//			incomeDetails.setExpenditureLoan(expenditureLoan);
//			incomeDetails.setApplicantIncome(householdExpenditure);
//			incomeDetails.setEducation(education);
//			incomeDetails.setOthers(others);
//			incomeDetails.setNetIncome(netIncome);
//			loanApplication.setIncomeDetails(incomeDetails);
//			
//
//			String ownerName = getCellValue(row, columnMapping.get("ownerName"));
//			String propertyAddress = getCellValue(row, columnMapping.get("propertyAddress"));
//			Double plotArea = getDoubleCellValue(row, columnMapping.get("plotArea"));
//			Double builtUpArea = getDoubleCellValue(row, columnMapping.get("builtUpArea"));
//			Double buildingAge = getDoubleCellValue(row, columnMapping.get("buildingAge"));
//
//			propertyDetails.setOwnerName(ownerName);
//			propertyDetails.setPropertyAddress(propertyAddress);
//			propertyDetails.setPlotArea(plotArea);
//			propertyDetails.setBuiltUpArea(builtUpArea);
//			propertyDetails.setBuildingAge(buildingAge);
//	        propertyDetails2.add(propertyDetails);
//			loanApplication.setPropertyDetails(propertyDetails2);
//
//			String initial = getCellValue(row, columnMapping.get("initial"));
//			String fullName = getCellValue(row, columnMapping.get("fullName"));
//			Long knownForYears = getNumberCellValue1(row, columnMapping.get("knownForYears"));
//			
//			String relationship = getCellValue(row, columnMapping.get("relationship"));
//		Long refpincode = getLongCellValue(row, columnMapping.get("refpincode"));
//		Long mobilenumber = getLongCellValue(row, columnMapping.get("mobilenumber"));
//	
//
//			reference.setInitial(initial);
//			reference.setFullName(fullName);
//			reference.setKnownForYears(knownForYears);
//			reference.setRelationship(relationship);
//			reference.setPincode(refpincode);
//			reference.setMobilenumber(mobilenumber);
//			reference1.add(reference);
//			loanApplication.setReferences(reference1);
//
//			String nameOfInstitution = getCellValue(row, columnMapping.get("nameOfInstitution"));
//		    String purposeOfLoans = getCellValue(row, columnMapping.get("purposeOfLoans"));
//    		Double loanAmount=getDoubleCellValue(row, columnMapping.get("loanAmount"));
//	        Double tenureOfLoan=getDoubleCellValue(row, columnMapping.get("tenureOfLoan"));
//		    Long monthlyInstallment = getLongCellValue(row, columnMapping.get("monthlyInstallment"));
//        	Double currentOutstanding=getDoubleCellValue(row, columnMapping.get("currentOutstanding"));
//		    Long balanceTenure=getNumberCellValue1(row, columnMapping.get("balanceTenure"));
//
//		
//			existingLoanDetails.setNameOfInstitution(nameOfInstitution);
//			existingLoanDetails.setPurposeOfLoan(purposeOfLoans);
//			existingLoanDetails.setLoanAmount(loanAmount);
//			existingLoanDetails.setTenureOfLoan(tenureOfLoan);
//			existingLoanDetails.setMonthlyInstallment(monthlyInstallment);
//			existingLoanDetails.setCurrentOutstanding(currentOutstanding);
//			existingLoanDetails.setBalanceTenure(balanceTenure);
//            existingLoanDetails2.add(existingLoanDetails);
//            loanApplication.setExistingLoanDetails(existingLoanDetails2);
//			
////           boolean duplicateCheckwithAadhar =loanApplicationRepository.existsByAadharNumber(aadharNumber) ;
////           if(!duplicateCheckwithAadhar) {
////        	   loanApplications.add(loanApplication);
////           }
//            
////			System.out.println("title :" + title + ",Name: " + name + ", father Name: " + fathername + ", Phone: " + phone);
//		}
//		
//		for (LoanApplication loanApplication2 : loanApplications) {
//			System.err.println(loanApplication2);
//			
//			
//			
//			
//		}
//		
//		
//		System.err.println(loanApplications);
//	loanApplicationRepository.saveAll(loanApplications);
//		workbook.close();
//		return "File processed successfully!";
//
//	}
//	
//
//	private Long getLongCellValue(Row row, Integer cellIndex) {
//		DataFormatter dataFormatter = new DataFormatter();
//		if (row.getCell(cellIndex) != null) {
//			return Long.parseLong(dataFormatter.formatCellValue(row.getCell(cellIndex)).trim());
//		} else {
//			return 0L; // Return 0 if the cell is empty or invalid
//		}
//
//	}
//
//	private Double getDoubleCellValue(Row row, Integer cellIndex) {
//		DataFormatter dataFormatter = new DataFormatter();
//		if (row.getCell(cellIndex) != null) {
//			return Double.parseDouble(dataFormatter.formatCellValue(row.getCell(cellIndex)).trim());
//		} else {
//			return null; // Return 0 if the cell is empty or invalid
//		}
//
//	}
//
//	private String getCellValue(Row row, Integer cellIndex) {
//		if (row.getCell(cellIndex) != null) {
//			return row.getCell(cellIndex).getStringCellValue().trim();
//		} else {
//			return null; // Return empty string for null or empty cell
//		}
//	}
//
//	private Date getDateCellValue(Row row, Integer cellIndex) {
//		if (row.getCell(cellIndex) != null && row.getCell(cellIndex).getCellType() == CellType.NUMERIC) {
//			return row.getCell(cellIndex).getDateCellValue();
//		} else {
//			return null; // Return null if the date cell is empty or not a date
//		}
//	}
//
//	public Long getNumberCellValue1(Row row, Integer columnIndex) {
//		if (columnIndex == null) {
//			return null; // Return null if columnIndex is null
//		}
//
//		Cell cell = row.getCell(columnIndex);
//		if (cell != null) {
//			if (cell.getCellType() == CellType.NUMERIC) {
//				return (long) cell.getNumericCellValue(); // Convert to Long
//			} else if (cell.getCellType() == CellType.STRING) {
//				try {
//					return Long.parseLong(cell.getStringCellValue().trim()); // Handle if the value is stored as a
//																				// string
//				} catch (NumberFormatException e) {
//					throw new IllegalArgumentException("Invalid long value in cell: " + cell.getStringCellValue());
//				}
//			}
//		}
//		return null; // Return null if the cell is empty or invalid
//	}
//
//}
