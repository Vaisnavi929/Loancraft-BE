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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.CustomerCallTrackingService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.entity.CustomerCallTracking;
import com.narvee.request.dto.RequestDTO;
import com.narvee.request.dto.callRequestDTO;

@RestController
@RequestMapping("/auth/cct")
public class CctController {
	private static final Logger logger = LoggerFactory.getLogger(CctController.class);

	@Autowired
	private CustomerCallTrackingService cCts;

	@PostMapping("/saveCct")
	public ResponseEntity<RestAPIResponse> saveCct (@RequestBody callRequestDTO cCt) {
		logger.info("!!! inside class:CctController,!! method:saveCct() ");
		try {
			return new ResponseEntity<RestAPIResponse>(
					new RestAPIResponse("success.", "Saved successfully.", cCts.saveCct(cCt)), HttpStatus.OK);
		} catch (RuntimeException e) {
			return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("failed.", e.getMessage()), HttpStatus.OK);
		}
	}
//
//	@PostMapping("/cCtBySearching")
//	public ResponseEntity<RestAPIResponse> getAllCct(@RequestBody RequestDTO request) {
//		logger.info("!!! inside class:CrmController,!! method:getAllCct() ");
//		return new ResponseEntity<RestAPIResponse>(
//				new RestAPIResponse("success", "CCt searching successfully ", cCts.getCctAll()), HttpStatus.OK);
//	}

	@PutMapping("/updateCct")
	  public ResponseEntity<RestAPIResponse> updatecct(@RequestBody callRequestDTO dto) {
        return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "updated successfuly", cCts.updateCct(dto)),HttpStatus.OK);
	}

	@GetMapping("/findByCct/{cctId}")
	public ResponseEntity<RestAPIResponse> findByCctId(@PathVariable Long cctId) {
		logger.info("!!! inside class:CrmController,!! method:findByCctId()");
		System.err.println(cctId);
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("success", "customer detalis fetched successfully", cCts.getById(cctId)),
				HttpStatus.OK);

	}

	@PostMapping("/getCustomerCallTracking")
	public ResponseEntity<RestAPIResponse> getAllCctBySorting(@RequestBody RequestDTO request) {
		logger.info("!!! inside class:CrmController,!! method:getAllCctBySorting() ");
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("success", "cct details fetched successfully", cCts.getAllCallTracking(request)),
				HttpStatus.OK);
	}

}
