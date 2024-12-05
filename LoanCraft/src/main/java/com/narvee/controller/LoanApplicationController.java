package com.narvee.controller;

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

import com.narvee.Service.LoanApplicationService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.request.dto.LoanApplicationDto;
import com.narvee.serviceimpl.CustomerServiceImpl;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/app")
public class LoanApplicationController {
	public static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	private LoanApplicationService loanApplicationService;


	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> saveLoanApplication(@RequestBody LoanApplicationDto loanApplication) {
		logger.info("!!! class :LoanApplicationController method : saveLoanApplication ");
		boolean isDuplicate = loanApplicationService.existsByAadharNumber(loanApplication.getAadharNumber());

		if (isDuplicate) {
			return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("failed ",
					"A loan application already exists for the provided Aadhaar number.",
					isDuplicate), HttpStatus.OK);
		} else {
			return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success ", "Application Submitted Successfully",
					loanApplicationService.saveLoanApplication(loanApplication)), HttpStatus.OK);

		}

	}

	@RequestMapping(value = "/getById/{loanId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getById(@PathVariable Long loanId) {
		logger.info("!!! class :LoanApplicationController method : getById ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "saved loan Application Successfully",
				loanApplicationService.getById(loanId)), HttpStatus.OK);
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAll() {
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("succuss",
				"fetched all loan application details ", loanApplicationService.getAll()), HttpStatus.OK);
	}

	@RequestMapping(value = "/delete/{loanId}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deleteById(@PathVariable Long loanId) {
		logger.info("!!! class :LoanApplicationController method : deleteById ");
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("success", "delete by id successfully", loanApplicationService.deltedById(loanId)),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/updateLoanApplication", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<RestAPIResponse> updateLoanApplication(@RequestBody LoanApplicationDto loanApplication) {
		logger.info("!!! inside class: LoanApplicationController, !! method : updateLoanApplication");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success",
				"updated loneapplication Successfully", loanApplicationService.updateLoanApplication(loanApplication)),
				HttpStatus.OK);

	}

	@RequestMapping(value = "/duplicateCheck/{aadhar}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> duplicateCheck(@PathVariable String aadhar) {
		logger.info("!!! inside class: LoanApplicationController, !! method : duplicateCheck");

		boolean isDuplicate = loanApplicationService.existsByAadharNumber(aadhar);
		if (isDuplicate) {
			return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("failed ",
					"A loan application already exists for the provided Aadhaar number. above give User friendly",
					isDuplicate), HttpStatus.OK);
		} else {
			return new ResponseEntity<RestAPIResponse>(
					new RestAPIResponse("success ", "Aadhar validated successfully", isDuplicate), HttpStatus.OK);

		}

	}

}
