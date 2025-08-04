package com.narvee.controller;

import org.apache.bcel.generic.NEW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.CustomerCallTrackingService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.entity.CustomerCallTracking;

@RestController
@RequestMapping("/api/cct")
public class CctController {
	private static final Logger logger = LoggerFactory.getLogger(CctController.class);
	
	@Autowired
	private CustomerCallTrackingService cCts;
	
	@PostMapping("/saveCct")
	public ResponseEntity<RestAPIResponse>saveCct(@RequestBody CustomerCallTracking  cCt){
		logger.info("!!! inside class:CctController,!! method:saveCct() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success.", "Saved successfully.",cCts.saveCctAll(cCt) ),HttpStatus.OK);
	}
	
	@GetMapping("/getCct")
	public ResponseEntity<RestAPIResponse>getAllCct(){
		logger.info("!!! inside class:CctController,!! method:getAllCct() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("got it", "the list of cct", cCts.getCctAll()),HttpStatus.OK);
	}
	

	
}
