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

import com.narvee.Service.LowCheckDepositService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.request.dto.LowCheckDepositDTO;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth/LowCheckApproval")
public class LowcheckDepositController {

	private static final Logger logger = LoggerFactory.getLogger(LowcheckDepositController.class);

	@Autowired
	private LowCheckDepositService checkDepositService;

	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> saveLowDepositEntity(@RequestBody LowCheckDepositDTO LowCheckDepositDTO) {
		logger.info("!!! inside class :LowcheckDepositController , !! method :saveLowDepositEntity() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully saved LowCheckApproval",
				checkDepositService.saveCheckDepositApproval(LowCheckDepositDTO)), HttpStatus.OK);

	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<RestAPIResponse> updateLowDepositEntity(@RequestBody LowCheckDepositDTO LowCheckDepositDTO) {
		logger.info("!!! inside class :LowcheckDepositController , !! method :updateLowDepositEntity() ");
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("success", "successfully update LowCheckApproval",
						checkDepositService.updateCheckDepositApproval(LowCheckDepositDTO)),
				HttpStatus.OK);

	}

	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getById(@PathVariable Long id) {
		logger.info("!!! inside class :LowcheckDepositController , !! method :getById() ");
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("success", "successfully fetchedById", checkDepositService.getById(id)),
				HttpStatus.OK);

	}

	@RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllLowCheckDepositArppoval() {
		logger.info("!!! inside class :LowcheckDepositController , !! method :getAllLowCheckDepositArppoval() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success",
				"successfully all LowCheckDepositArppoval ", checkDepositService.getAllLowCheckDepositApproval()),
				HttpStatus.OK);

	}

	@RequestMapping(value = "/getLowCheckDepositDetails", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getLowCheckDepositDetails() {
		logger.info("!!! inside class :LowcheckDepositController , !! method :getAllLowCheckDepositArppoval() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success",
				"successfully all LowCheckDepositArppoval ", checkDepositService.getLowCheckDepositDetails()),
				HttpStatus.OK);

	}

	@RequestMapping(value = "/updateIsApproved/{id}/{value}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> updateIsApproved(@PathVariable("id") Long id,
			@PathVariable("value") boolean value) {
		logger.info("!!! inside class :LowcheckDepositController , !! method :updateIsApproved() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully updated IsApproved ",
				checkDepositService.updateIsApproved(id, value)), HttpStatus.OK);

	}
	
	@RequestMapping(value = "/updateRemarks/{id}/{value}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> updateRemarks(@PathVariable("id") Long id,
			@PathVariable("value") String value) {
		logger.info("!!! inside class :LowcheckDepositController , !! method :updateIsApproved() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully updated IsApproved ",
				checkDepositService.updateRemarks(id, value)), HttpStatus.OK);

		
		
		
	}
}
