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

import com.narvee.Service.LoanSanctionservice;
import com.narvee.commons.RestAPIResponse;
import com.narvee.request.dto.LoanSanctionDTO;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/loanSanction")
public class LoanSactionController {
	
	Logger logger=LoggerFactory.getLogger(LoanSactionController.class);
	
	@Autowired
	private LoanSanctionservice loanSanctionservice;

	
	@RequestMapping(value="/saveLoanSanction",method=RequestMethod.POST,produces ="application/json")
	public ResponseEntity<RestAPIResponse> saveLoanSanction(@RequestBody LoanSanctionDTO loanSanctionDTO ){
		logger.info("!!! inside class: LoanSactionController , !! method : saveLoanSanction ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "saved loanSanction Successfully", loanSanctionservice.saveLoanSanction(loanSanctionDTO)), HttpStatus.OK);
		
	}
	
	@RequestMapping(value ="/updateLoanSanction",method = RequestMethod.PUT,produces ="application/json")
	public ResponseEntity<RestAPIResponse> updateLoanSanction(@RequestBody LoanSanctionDTO loanSanction){
		logger.info("!!! inside class :LoanSactionController , !! method : updateLoanSaction");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "updated loanSanction Successfully", loanSanctionservice.updateLoanSanction(loanSanction)), HttpStatus.OK);
		
	}
	
	@RequestMapping(value ="/getByid/{loanSactionId}",method = RequestMethod.GET,produces ="application/json")
	public ResponseEntity<RestAPIResponse> getById(@PathVariable Long loanSactionId){
		logger.info(" !!! inside class : LoanSanctionController, !! method : getById");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "fetched by id Successfully", loanSanctionservice.getById(loanSactionId)), HttpStatus.OK);
		
	}
	
	@RequestMapping(value ="/getAll",method =RequestMethod.GET,produces ="application/json")
	public ResponseEntity<RestAPIResponse> getAllLoanApplication(){
		logger.info("!!! inside class : LoanSanctionController, !! method : getAllLoanApplication");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "fetched All LoanSanction Details", loanSanctionservice.getAllLoanSanction()), HttpStatus.OK);
		
	}
	
	@RequestMapping(value ="/delete/{loanSactionId}",method=RequestMethod.DELETE,produces ="application/json")
	public ResponseEntity<RestAPIResponse> deleteById(@PathVariable Long loanSactionId){
		logger.info("!!! inside class : LoanSactionController, !! method : deleteById");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "deleted by Id Successfully", loanSanctionservice.deleteById(loanSactionId)), HttpStatus.OK);
		
	}
}
