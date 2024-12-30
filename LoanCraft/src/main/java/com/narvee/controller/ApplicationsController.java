package com.narvee.controller;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.ApplicationsService;
import com.narvee.Service.SortingRequestDto;
import com.narvee.commons.RestAPIResponse;
import com.narvee.entity.Applications;
import com.narvee.request.dto.LoanApplicationDto;
import com.narvee.request.dto.LoanApplicationUpdateDto;
import com.narvee.util.EncryptionUtil;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/app")
public class ApplicationsController {
	public static final Logger logger = LoggerFactory.getLogger(ApplicationsController.class);

	@Autowired
	private ApplicationsService applicationService;
	
	@Autowired
	private ModelMapper modelMapper;

	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> saveLoanApplication(@RequestBody LoanApplicationDto loanApplicationDto) {
		logger.info("!!! Class: LoanApplicationController, Method: saveLoanApplication");

		try {
			// Decrypt the customerId from the request
			long decryptedCustomerId = EncryptionUtil.decrypt(loanApplicationDto.getCustomerId());
			loanApplicationDto.setCustomerId(String.valueOf(decryptedCustomerId));

			// Save the loan application
			Applications savedApplication = applicationService.saveLoanApplication(loanApplicationDto);

			logger.info("Loan application saved successfully.");
			return ResponseEntity.ok(new RestAPIResponse("success", "Application Submitted Successfully", savedApplication));
		} catch (Exception e) {
			logger.error("Error saving loan application: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new RestAPIResponse("fail", "Error submitting application."));
		}
	}

	@RequestMapping(value = "/getById/{loanId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getById(@PathVariable Long loanId) {
		logger.info("!!! class :LoanApplicationController method : getById ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "saved loan Application Successfully",
				applicationService.getById(loanId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAll() {
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("succuss", "fetched all loan application details ", applicationService.getAll()),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/delete/{loanId}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deleteById(@PathVariable Long loanId) {
		logger.info("!!! class :LoanApplicationController method : deleteById ");
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("success", "delete by id successfully", applicationService.deltedById(loanId)),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/updateLoanApplication", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<RestAPIResponse> updateLoanApplication(
			@RequestBody LoanApplicationUpdateDto loanApplication) {
		logger.info("!!! inside class: LoanApplicationController, !! method : updateLoanApplication");
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("success", "updated loneapplication Successfully",
						applicationService.updateLoanApplication(loanApplication)), // .updateLoanApplication(loanApplication)),
				HttpStatus.OK);

	}

	@RequestMapping(value = "/duplicateCheck/{aadhar}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> duplicateCheck(@PathVariable String aadhar) {
		logger.info("!!! inside class: LoanApplicationController, !! method : duplicateCheck");

		boolean isDuplicate = applicationService.existsByAadharNumber(aadhar);
		if (isDuplicate) {
			return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("failed ",
					"A loan application already exists for the provided Aadhaar number. above give User friendly",
					isDuplicate), HttpStatus.OK);
		} else {
			return new ResponseEntity<RestAPIResponse>(
					new RestAPIResponse("success ", "Aadhar validated successfully", isDuplicate), HttpStatus.OK);

		}

	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllLoanApplicationWithSearching(
			@RequestBody SortingRequestDto sortingRequestDto) {
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("Success", "successfully fetched laonapplication with searching",
						applicationService.getAllLoanApplicationPagination(sortingRequestDto)),
				HttpStatus.OK);

	}

}
