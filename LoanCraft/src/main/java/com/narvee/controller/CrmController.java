package com.narvee.controller;

import java.util.List;

import org.apache.bcel.generic.NEW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.CustomerRelationshipMangementService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.entity.CustomerRelationshipMangement;
import com.narvee.request.dto.RequestDTO;

@RestController
@RequestMapping("/api/crm")
public class CrmController {

	private static final Logger logger = LoggerFactory.getLogger(CrmController.class);

	@Autowired
	private CustomerRelationshipMangementService cRms;
//	@Autowired
//	private CustomerCallTrackingService  ccTs;

	@PostMapping("/saveCrm")
	public ResponseEntity<RestAPIResponse> saveCrm(@RequestBody CustomerRelationshipMangement crmEntity) {
		logger.info("!!! inside class:CrmController,!! method:saveCrm() ");
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("success", "added successfully", cRms.saveCrm(crmEntity)), HttpStatus.OK);

//		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success",
//				"succesfully updated CheckDeposit details", depositService.updateDeposit(checkDepositDTO)),
//				HttpStatus.OK);
//		

	}
	
//
//	@GetMapping("/getCrm")
//	public ResponseEntity<RestAPIResponse> getAllcrm() {
//		logger.info("!!! inside class:CrmController,!! method:getAllcrm() ");
//		List<CustomerRelationshipMangement> crmList = cRms.getCrm();
//		RestAPIResponse rp = new RestAPIResponse("success", "list of data", crmList);
//		return new ResponseEntity<>(rp, HttpStatus.OK);
//	}

//	@GetMapping("/getApplicationsForLoggedInCustomer/{customerId}/{remarks}")
//	public ResponseEntity<RestAPIResponse> getApplicationsForLoggedInCustomer(@PathVariable String customerId,String remarks) {
//		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully fetched all details",
//				customerService.getApplicationsByCustomerId(customerId, remarks)), HttpStatus.OK);
//
//	}

	@GetMapping("/duplicateCheckEmail/{email}")
	public ResponseEntity<RestAPIResponse> checkDuplicateEmail(@PathVariable String email) {

		logger.info("!!! inside class:CrmController,!! method:checkDuplicateEmail() ");
		boolean exists = cRms.existsByEmail(email);
		System.err.println(email);

		if (exists) {
			return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("failed", "Email already exists ", exists),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<RestAPIResponse>(
					new RestAPIResponse("success.", "Email verified successfully  ", exists), HttpStatus.OK);
		}
	}

	@GetMapping("/duplicateCheckMobleNumber/{moblieNumber}")
	public ResponseEntity<RestAPIResponse> checkDuplicateMoblieNumber(@PathVariable String moblieNumber) {

		logger.info("!!! inside class:CrmController,!! method:checkDuplicateMoblieNumber() ");
		boolean existsMoblieNum = cRms.existsByMoblieNumber(moblieNumber);

		if (existsMoblieNum) {
			return new ResponseEntity<RestAPIResponse>(
					new RestAPIResponse("failed", "MoblieNumber already exists ", existsMoblieNum), HttpStatus.OK);
		} else {
			return new ResponseEntity<RestAPIResponse>(
					new RestAPIResponse("success.", "Email verified successfully  ", existsMoblieNum), HttpStatus.OK);
		}

	}

	@PostMapping("/sortingBySearching")
	public ResponseEntity<RestAPIResponse> getAllCrm(@RequestBody RequestDTO request) {
		logger.info("!!! inside class:CrmController,!! method:getAllCrm() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("done", "searching successfully ", cRms.findAllCrm(request)),
				HttpStatus.OK);
	}

	@PutMapping("/updateCrm")
	public ResponseEntity<RestAPIResponse>updateByIdCrm(@RequestBody CustomerRelationshipMangement crm){
		logger.info("!!! inside class:CrmController,!! method:updateByIdCrm() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("done", "updated successfuly",cRms.update(crm)),HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteById/{crmId}")
	public ResponseEntity<RestAPIResponse>deleteByIdcrm(@PathVariable Long crmId ){
		logger.info("!!! inside class:CrmController,!! method:deleteByIdcrm() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("done", "deleted successfuly",cRms.deleteById(crmId)),HttpStatus.OK);
	}
	
	@GetMapping("/findByCrm/{crmId}")
	public ResponseEntity<RestAPIResponse>findByCrm(@PathVariable Long crmId){
		logger.info("!!! inside class:CrmController,!! method:findByCrm()");
		System.err.println(crmId);
		return new ResponseEntity<RestAPIResponse> (new RestAPIResponse("fetched", "customer detalis", cRms.getById(crmId)),HttpStatus.OK);
		
	}
}
