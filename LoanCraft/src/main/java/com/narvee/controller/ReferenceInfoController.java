package com.narvee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.ReferenceService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.entity.ReferenceInfo;
import com.narvee.request.dto.ReferenceRequestDto;
import com.narvee.request.dto.RequestDTO;

@RestController
@RequestMapping("/api/referenceinfo")
public class ReferenceInfoController {
	@Autowired
	private ReferenceService referenceService;

	@PostMapping("/save")
	public ResponseEntity<RestAPIResponse> saveReference(@RequestBody ReferenceRequestDto info) {
		try {
			return new ResponseEntity<RestAPIResponse>(
					new RestAPIResponse("success", "saved successfully", referenceService.saveRi(info)), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("faild", e.getMessage()), HttpStatus.OK);
		}
	}

	@PutMapping("/update")
	public ResponseEntity<RestAPIResponse> updateCct(@RequestBody ReferenceInfo info) {
		

		try {
			return new ResponseEntity<RestAPIResponse>(
					new RestAPIResponse("success", "updated successfully", referenceService.updateRi(info)),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("failed", e.getMessage()), HttpStatus.OK);
		}
	}

	@GetMapping("/getAll")
	public ResponseEntity<RestAPIResponse> getAll() {
		try {
			return new ResponseEntity<RestAPIResponse>(
					new RestAPIResponse("success", "Reference details", referenceService.getAllRi()), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("failed", e.getMessage()), HttpStatus.OK);
		}
	}

	@GetMapping("/getById/{cctId}")
	public ResponseEntity<RestAPIResponse> getById(@PathVariable Long cctId) {
		try {
			return new ResponseEntity<RestAPIResponse>(
					new RestAPIResponse("success", "Reference details", referenceService.getByIdRi(cctId)),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("failed", e.getMessage()), HttpStatus.OK);
		}
	}

	@PostMapping("/getReferenceinfo")
	public ResponseEntity<RestAPIResponse> getAllCct(@RequestBody RequestDTO request) {

		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "crm details fetched successfully",
				referenceService.paginationReferenceInfo(request)), HttpStatus.OK);
	}
}
