package com.narvee.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.narvee.Service.ApplicationsService;
import com.narvee.Service.SortingRequestDto;
import com.narvee.config.MapperConfig;
import com.narvee.entity.Applications;
import com.narvee.entity.CustomersInfo;
import com.narvee.entity.Reference;
import com.narvee.repository.ApplicationsRepository;
import com.narvee.repository.CustomersRepository;
import com.narvee.repository.ReferenceRepository;
import com.narvee.request.dto.LoanApplicationDTOS;
import com.narvee.request.dto.LoanApplicationDto;
import com.narvee.request.dto.LoanApplicationUpdateDto;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LoanApplicationServiceImpl implements ApplicationsService {

	public static final Logger logger = LoggerFactory.getLogger(CustomerS.class);

	@Autowired
	private ApplicationsRepository applicationRepository;

	@Autowired
	private ReferenceRepository referenceRepository;

	@Autowired
	private CustomersRepository customerRepository;

	private static final int DIGIT_PADDING = 4;

	@Override
	public Applications saveLoanApplication(LoanApplicationDto loanApplicationDto) {
		logger.info("Inside saveLoanApplication() method");
		Applications loan = DTOtoApplication(loanApplicationDto);

		CustomersInfo customersInfo = customerRepository.findById(Long.parseLong(loanApplicationDto.getCustomerId()))
				.orElseThrow(() -> new RuntimeException(
						"Customer not found with ID: " + loanApplicationDto.getCustomerId()));

		customersInfo.setCustomerId(Long.parseLong(loanApplicationDto.getCustomerId()));
		loan.setCustomers(customersInfo);

		// Save references and add them to the loan
		List<Reference> references = referenceRepository.saveAll(loanApplicationDto.getReference());
		loan.setReference(references);

		Long amaxnumber = applicationRepository.appmaxNumber();
		if (amaxnumber == null) {
			amaxnumber = 0L;
		}

		String valueWithPadding = String.format("%0" + DIGIT_PADDING + "d", amaxnumber + 1);
		String value = "BEACON" + valueWithPadding;
		loan.setApplicationReferenceId(value);
		loan.setStatus("Applied");

		loan.setAmaxnum(amaxnumber + 1);

		// Save the loan application
		return applicationRepository.save(loan);
	}

	@Override
	public Applications getById(Long loanId) {
		logger.info("!!! inside class: LoanApplicationServiceImpl , !! method: getById()");
		return applicationRepository.findById(loanId).get();
	}

	@Override
	public List<Applications> getAll() {
		logger.info("!!! inside class: LoanApplicationServiceImpl , !! method: getAll()");
		return applicationRepository.findAll();
	}

	@Override
	public boolean deltedById(Long loanId) {
		logger.info("!!! inside class: LoanApplicationServiceImpl , !! method: deltedById()");
		applicationRepository.deleteById(loanId);
		return true;
	}

	@Override
	public LoanApplicationUpdateDto updateLoanApplication(LoanApplicationUpdateDto loanApplication) {
		logger.info("!!! inside class: LoanApplicationserviceimpl , !! method: updateLoanApplication");
		ModelMapper mapper = MapperConfig.getModelMapper();

		// Fetch the existing loan application entity using the loanId
		Applications existingLoan = applicationRepository.findById(loanApplication.getLoanId()).orElseThrow(
				() -> new RuntimeException("Loan Application not found with ID: " + loanApplication.getLoanId()));

		mapper.map(loanApplication, existingLoan);

		// If customerId is provided, validate and fetch the customer
		if (loanApplication.getCustomerId() != null && !loanApplication.getCustomerId().isEmpty()) {
			CustomersInfo customersInfo = customerRepository.findById(Long.parseLong(loanApplication.getCustomerId()))
					.orElseThrow(() -> new RuntimeException(
							"Customer not found with ID: " + loanApplication.getCustomerId()));
			existingLoan.setCustomers(customersInfo); // Update the customer reference in the loan
		}

		// Save the updated loan application
		Applications updatedLoan = applicationRepository.save(existingLoan);

		// Convert the updated entity back to a DTO
		LoanApplicationUpdateDto applicationUpdateDto = mapper.map(updatedLoan, LoanApplicationUpdateDto.class);

		// Map customerId explicitly
		if (updatedLoan.getCustomers() != null) {
			applicationUpdateDto.setCustomerId(String.valueOf(updatedLoan.getCustomers().getCustomerId()));
		}

		return applicationUpdateDto;
	}

	@Override
	public boolean existsByAadharNumber(String aadharNumber) {
		logger.info("!!! inside class: LoanApplicationserviceimpl , !! method: updateLoanApplication");
		return false; // loanApplicationRepository.existsByAadharNumber(aadharNumber);
	}

	@Override
	public Page<LoanApplicationDTOS> getAllLoanApplicationPagination(SortingRequestDto sortingRequestDto) {
		String sortField = sortingRequestDto.getSortField();
		String sortOrder = sortingRequestDto.getSortOrder();
		String keyword = sortingRequestDto.getKeyword();
		Integer pageNumber = sortingRequestDto.getPageNumber();
		Integer pageSize = sortingRequestDto.getPageSize();
		if (sortField.equalsIgnoreCase("employment_type"))
			sortField = "employment_type";
		else if (sortField.equalsIgnoreCase("amaxnum"))
			sortField = "amaxnum";
		else if (sortField.equalsIgnoreCase("application_reference_id"))
			sortField = "application_reference_id";
		else if (sortField.equalsIgnoreCase("company_name"))
			sortField = "company_name";
		else if (sortField.equalsIgnoreCase("salary"))
			sortField = "salary";
		else if (sortField.equalsIgnoreCase("yearsin_employment"))
			sortField = "yearsin_employment";
		else if (sortField.equalsIgnoreCase("company_address"))
			sortField = "company_address";
		else if (sortField.equalsIgnoreCase("pin_code"))
			sortField = "pin_code";
		else if (sortField.equalsIgnoreCase("loan_amount"))
			sortField = "loan_amount";
		else if (sortField.equalsIgnoreCase("repayment_mode"))
			sortField = "repayment_mode";
		else if (sortField.equalsIgnoreCase("loan_tenure"))
			sortField = "loan_tenure";
		else
			sortField = "status";
		Sort.Direction sortDirection = Sort.Direction.ASC;
		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
		}

		Sort sort = Sort.by(sortDirection, sortField);
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
		if (keyword.equalsIgnoreCase("empty")) {
			return applicationRepository.getAllLoanApplication(pageable);
		} else {

			return applicationRepository.getAllLoanApplicationWithKeyword(pageable, keyword);

		}

	}

	public Applications DTOtoApplication(LoanApplicationDto applicationDto) {
		Applications application = new Applications();
		application.setStatus(applicationDto.getStatus());
		application.setEmploymentType(applicationDto.getEmploymentType());
		application.setSalary(applicationDto.getSalary());
		application.setYearsinEmployment(applicationDto.getYearsinEmployment());
		application.setCompanyName(applicationDto.getCompanyName());
		application.setCompanyAddress(applicationDto.getCompanyAddress());
		application.setPinCode(applicationDto.getPinCode());
		application.setLoanAmount(applicationDto.getLoanAmount());
		application.setRepaymentMode(applicationDto.getRepaymentMode());
		application.setLoanTenure(applicationDto.getLoanTenure());
		application.setReference(applicationDto.getReference());
		application.setIdentityProof(applicationDto.getIdentityProof());
		return application;

	}

}
