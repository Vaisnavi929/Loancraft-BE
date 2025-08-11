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

import com.narvee.Service.NBFCService;
import com.narvee.Service.SortingRequestDto;
import com.narvee.commons.RestAPIResponse;
import com.narvee.entity.NBFC;

@RequestMapping("/nbfc")
@RestController
public class NBFCController {
	public static final Logger logger = LoggerFactory.getLogger(NBFCController.class);

	@Autowired
	private NBFCService nbfcService;

	@PostMapping(value = "/save")
	public ResponseEntity<RestAPIResponse> save(@RequestBody NBFC nbfc) throws Exception {
		logger.info("!!! inside class: NBFCController, method: save");
		nbfcService.save(nbfc);
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "NBFC saved successfully"),
				HttpStatus.OK);

	}

	@PutMapping(value = "/update")
	public ResponseEntity<RestAPIResponse> update(@RequestBody NBFC nbfc) throws Exception {
		logger.info("!!! inside class: NBFCController, method: update");
		nbfcService.update(nbfc);
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "NBFC updated successfully"),
				HttpStatus.OK);
	}

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<RestAPIResponse> delete(@PathVariable Long id) throws Exception {
		logger.info("!!! inside class: NBFCController, method: delete");
		nbfcService.delete(id);
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "NBFC deleted successfully"),
				HttpStatus.OK);
	}

	@GetMapping(value = "/getById/{id}")
	public ResponseEntity<RestAPIResponse> getById(@PathVariable Long id) throws Exception {
		logger.info("!!! inside class: NBFCController, method: getById");
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("success", "NBFC deleted successfully", nbfcService.getById(id)), HttpStatus.OK);
	}

	@GetMapping(value = "/emailDuplicateCheck/{email}")
	public ResponseEntity<RestAPIResponse> emailDuplicateCheck(@PathVariable String email) throws Exception {
		logger.info("!!! inside class: NBFCController, method: emailDuplicateCheck");
		boolean checkDuplicate = nbfcService.EmailDuplicateCheck(email);

		if (checkDuplicate) {
			return new ResponseEntity<RestAPIResponse>(
					new RestAPIResponse("failed", "Email already Exists", checkDuplicate), HttpStatus.OK);

		} else {
			return new ResponseEntity<RestAPIResponse>(
					new RestAPIResponse("success", "Email verified successfully", checkDuplicate), HttpStatus.OK);

		}

	}

	@GetMapping(value = "/nbfc/nbfcDuplicateCheck/{nbfc}")
	public ResponseEntity<RestAPIResponse> DuplicateCheck(@PathVariable String nbfc) throws Exception {
		logger.info("!!! inside class: NBFCController, method: DuplicateCheck");
		boolean checkDuplicate = nbfcService.NbfcDuplicateCheck(nbfc);

		if (checkDuplicate) {
			return new ResponseEntity<RestAPIResponse>(
					new RestAPIResponse("failed", "NBFC already Exists", checkDuplicate), HttpStatus.OK);

		} else {
			return new ResponseEntity<RestAPIResponse>(
					new RestAPIResponse("success", "NBFC verified successfully", checkDuplicate), HttpStatus.OK);

		}

	}
	
	@GetMapping(value = "/nbfc/DuplicateCheckIdentificationNumber/{corporateIdentificationNo}")
	public ResponseEntity<RestAPIResponse> DuplicateCheckIdentificationNumber(@PathVariable String corporateIdentificationNo) throws Exception {
		logger.info("!!! inside class: NBFCController, method: DuplicateCheck");
		boolean checkDuplicate = nbfcService.NbfcDuplicateCheckCorporateIdentificationNo(corporateIdentificationNo);

		if (checkDuplicate) {
			return new ResponseEntity<RestAPIResponse>(
					new RestAPIResponse("failed", "CorporateIdentificationNo already Exists", checkDuplicate), HttpStatus.OK);

		} else {
			return new ResponseEntity<RestAPIResponse>(
					new RestAPIResponse("success", "CorporateIdentificationNo verified successfully", checkDuplicate), HttpStatus.OK);

		}

	}

	@PostMapping(value = "/nbfc/getAllNbfcs")
	public ResponseEntity<RestAPIResponse> getAllNbfcs(@RequestBody SortingRequestDto dto) throws Exception {
		logger.info("!!! inside class: NBFCController, method: getAllNbfcs");
		return new ResponseEntity<RestAPIResponse>(
				new RestAPIResponse("success", "NBFC's fetched successfully", nbfcService.getAllNBFC(dto)),
				HttpStatus.OK);

	}
}
