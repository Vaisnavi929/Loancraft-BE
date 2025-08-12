package com.narvee.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.narvee.Service.CustomerCallTrackingService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.entity.CustomerCallTracking;
import com.narvee.request.dto.RequestDTO;

@RestController
@RequestMapping("/auth/cct")
public class CctController {
	private static final Logger logger = LoggerFactory.getLogger(CctController.class);

	@Autowired
	private CustomerCallTrackingService cCts;

	@PostMapping("/saveCct")
	public ResponseEntity<RestAPIResponse> saveCct(@RequestBody CustomerCallTracking cCt) {
		logger.info("!!! inside class:CctController,!! method:saveCct() ");
		try {
			return new ResponseEntity<RestAPIResponse>(
					new RestAPIResponse("success.", "Saved successfully.", cCts.saveCctAll(cCt)), HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("failed.", e.getMessage()), HttpStatus.OK);
		}
	}

//	@GetMapping("/getCct")
//	public ResponseEntity<RestAPIResponse> getAllCct() {
//		logger.info("!!! inside class:CctController,!! method:getAllCct() ");
//		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "the list of cct", cCts.getCctAll()),
//				HttpStatus.OK);
//	}
//
//	@PostMapping("/cCtBySearching")
//	public ResponseEntity<RestAPIResponse> getAllCct(@RequestBody RequestDTO request) {
//		logger.info("!!! inside class:CrmController,!! method:getAllCct() ");
//		return new ResponseEntity<RestAPIResponse>(
//				new RestAPIResponse("success", "CCt searching successfully ", cCts.getCctAll()), HttpStatus.OK);
//	}

	@PutMapping("/updateCct")
	public ResponseEntity<RestAPIResponse> updateCct(@RequestBody CustomerCallTracking callTracking) {
		logger.info("!!! inside class:CrmController,!! method:updateCct() ");
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("success", "updated successfully", cCts.updateCct(callTracking)), HttpStatus.OK);
	}

	@DeleteMapping("/deleteCct/{cCtid}")
	public ResponseEntity<RestAPIResponse> deleteById(@PathVariable Long cCtid) {
		logger.info("!!! inside class:CrmController,!! method:deleteById() ");
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("success", "deleted successfully", cCts.deleteById(cCtid)), HttpStatus.OK);
	}

	@PostMapping("/getCctByCrmId")
	public ResponseEntity<RestAPIResponse> getById(@RequestBody RequestDTO request) {
		logger.info("!!! inside class:CrmController,!! method:getById() ");
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("success", "fetched customer call Tracking", cCts.getCctByCrmId(request)), HttpStatus.OK);
	}

}
