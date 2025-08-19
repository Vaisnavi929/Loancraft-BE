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
import com.narvee.Service.SortingRequestDto;
import com.narvee.commons.RestAPIResponse;
import com.narvee.request.dto.LowCheckDepositDTO;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/LowCheckApproval")
public class LowcheckDepositController {

	private static final Logger logger = LoggerFactory.getLogger(LowcheckDepositController.class);

	@Autowired
	private LowCheckDepositService checkDepositService;

	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> saveLowDepositEntity(@RequestBody LowCheckDepositDTO LowCheckDepositDTO) {
		logger.info("!!! inside class :LowcheckDepositController , !! method :saveLowDepositEntity() ");
		checkDepositService.saveCheckDepositApproval(LowCheckDepositDTO);
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("success", "successfully saved LowCheckApproval"), HttpStatus.OK);

	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<RestAPIResponse> updateLowDepositEntity(@RequestBody LowCheckDepositDTO LowCheckDepositDTO) {
		logger.info("!!! inside class :LowcheckDepositController , !! method :updateLowDepositEntity() ");
		checkDepositService.updateCheckDepositApproval(LowCheckDepositDTO);
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("success", "successfully update LowCheckApproval"), HttpStatus.OK);

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

	@RequestMapping(value = "/getLowCheckDepositDetails", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getLowCheckDepositDetails(@RequestBody SortingRequestDto sortingRequestDto) {
		logger.info("!!! inside class :LowcheckDepositController , !! method :getAllLowCheckDepositArppoval() ");
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("success", "successfully all LowCheckDepositArppoval ",
						checkDepositService.getLowCheckDepositDetails(sortingRequestDto)),
				HttpStatus.OK);
	}
//	@PostMapping("/sortingBySearching")
//	public ResponseEntity<RestAPIResponse> getAllCrm(@RequestBody RequestDTO request) {
//		logger.info("!!! inside class:CrmController,!! method:getAllCrm() ");
//		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "crm details fetched successfully", cRms.findAllCrm(request)),
//				HttpStatus.OK);
//	}

	@RequestMapping(value = "/updateIsApproved/{id}/{value}/{remarks}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> updateApprovalStatusAndRemarks(@PathVariable Long id,
			@PathVariable Boolean value, @PathVariable String remarks) {
		logger.info("!!! inside class :LowcheckDepositController , !! method :updateIsApproved() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully updated IsApproved ",
				checkDepositService.updateApprovalStatusAndRemarks(id, value, remarks)), HttpStatus.OK);

	}

}
