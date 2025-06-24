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

import com.narvee.Service.DisbursmentService;
import com.narvee.Service.SortingRequestDto;
import com.narvee.commons.RestAPIResponse;
import com.narvee.request.dto.DisbursmentDTO;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth/disbursment")
public class DisbursmentController {

	private static final Logger logger = LoggerFactory.getLogger(DisbursmentController.class);

	@Autowired
	private DisbursmentService disbursmentService;

	@RequestMapping(value = "/saveDisbursment", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> saveDisbursment(@RequestBody DisbursmentDTO disbursmentDTO) {
		logger.info("!!! inside class :DisbursmentController , method :saveDisbursment() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully save disbursment! ",
				disbursmentService.saveDisbursment(disbursmentDTO)), HttpStatus.OK);

	}

	@RequestMapping(value = "/updateDisbursment", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<RestAPIResponse> updateDisbursment(@RequestBody DisbursmentDTO disbursmentDTO) {
		logger.info("!!! inside class :DisbursmentController , method :saveDisbursment() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully Updated Disbursment!",
				disbursmentService.updateDisbursment(disbursmentDTO)), HttpStatus.OK);

	}

	@RequestMapping(value = "/getById/{disbursmentId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getByDisbursmentId(@PathVariable Long disbursmentId) {
		logger.info("!!! inside class :DisbursmentController , method :getByDisbursmentId() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully fetched  disbursmentId",
				disbursmentService.getById(disbursmentId)), HttpStatus.OK);

	}

	@RequestMapping(value = "/allDisbursment", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllDisbursment() {
		logger.info("!!! inside class :DisbursmentController , method :getAllDisbursment() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success",
				"successfully fetched All disbursment details ", disbursmentService.getAll()), HttpStatus.OK);

	}

	@RequestMapping(value = "/deleteById/{disbursmentId}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deleteByDisbursmentId(@PathVariable Long disbursmentId) {
		logger.info("!!! inside class :DisbursmentController , method :deleteByDisbursmentId() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success",
				"successfully Deleted deleteByDisbursmentId", disbursmentService.deleteById(disbursmentId)),
				HttpStatus.OK);

	}

	@RequestMapping(value = "/getAllWithSearching", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllDisbursmentWithPagination(
			@RequestBody SortingRequestDto sortingRequestDto) {
		logger.info("!!! inside class :DisbursmentController , method :getAllDisbursmentWithPagination() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully fetched Disbursments",
				disbursmentService.getAllDisbursmentWithPagination(sortingRequestDto)), HttpStatus.OK);

	}

	@RequestMapping(value = "/getCustomerDetails", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getCustomerDetailsWithDisbursmentId() {
		logger.info("!!! inside class :DisbursmentController , method :getAllDisbursment() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success",
				"successfully fetched customer details ", disbursmentService.getCustomerDetailsWithDisbursmentId()),
				HttpStatus.OK);

	}

}
