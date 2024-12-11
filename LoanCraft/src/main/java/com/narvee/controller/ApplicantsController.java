package com.narvee.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.ApplicantService;
import com.narvee.Service.IAuthenticationService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.request.dto.ApplicantDto;
import com.narvee.request.dto.SortingDTO;

@RestController
@RequestMapping("/api/applicants")
@CrossOrigin
public class ApplicantsController {

	private static final Logger logger = LoggerFactory.getLogger(ApplicantsController.class);

	@Autowired
	private ApplicantService applicantService;

	@Autowired
	private IAuthenticationService authenticationService;

	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> saveApplicant(@RequestBody ApplicantDto applicantDto) {
		logger.info("Saving applicant: {}", applicantDto.getEmail());
		try {
			applicantService.save(applicantDto);
			Object authenticate = authenticationService.authenticate(applicantDto.getEmail(),
					applicantDto.getPassword());
			logger.info("Applicant saved and authenticated successfully: {}", applicantDto.getEmail());
			return ResponseEntity
					.ok(new RestAPIResponse("success", "Applicant saved and authenticated successfully", authenticate));
		} catch (Exception e) {
			logger.error("Error saving applicant: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new RestAPIResponse("fail", "Error saving applicant"));
		}
	}

	@RequestMapping(value = "/findById/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getApplicantById(@PathVariable Long id) {
		logger.info("Fetching applicant by ID: {}", id);
		ApplicantDto applicant = applicantService.getById(id);
		if (applicant != null) {
			logger.info("Applicant found: {}", applicant.getEmail());
			return ResponseEntity.ok(new RestAPIResponse("success", "Applicant fetched successfully", applicant));
		} else {
			logger.warn("Applicant not found for ID: {}", id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestAPIResponse("fail", "Applicant not found"));
		}
	}

	@RequestMapping(value = "/getAll", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllApplicants(@RequestBody SortingDTO sortingDTO) {
		logger.info("Fetching all applicants with sorting: {}", sortingDTO);
		Page<ApplicantDto> allApplicants = applicantService.getAllApplicants(sortingDTO);
		if (allApplicants.hasContent()) {
			logger.info("Applicants fetched successfully");
			return ResponseEntity.ok(new RestAPIResponse("success", "Applicants fetched successfully", allApplicants));
		} else {
			logger.warn("No applicants found with the provided sorting parameters");
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body(new RestAPIResponse("fail", "No applicants found"));
		}
	}

	@RequestMapping(value = "/deleteById/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deleteApplicantById(@PathVariable Long id) {
		logger.info("Deleting applicant by ID: {}", id);
		boolean isDeleted = applicantService.deleteById(id);
		if (isDeleted) {
			logger.info("Applicant deleted successfully: {}", id);
			return ResponseEntity.ok(new RestAPIResponse("success", "Applicant deleted successfully"));
		} else {
			logger.warn("Applicant not found for ID: {}", id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestAPIResponse("fail", "Applicant not found"));
		}
	}

	@RequestMapping(value = "/updateById/{id}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<RestAPIResponse> updateApplicant(@PathVariable Long id,
			@RequestBody ApplicantDto updatedApplicantDto) {
		logger.info("Updating applicant with ID: {}", id);
		ApplicantDto updatedApplicant = applicantService.update(id, updatedApplicantDto);
		if (updatedApplicant != null) {
			logger.info("Applicant updated successfully: {}", updatedApplicant.getEmail());
			return ResponseEntity
					.ok(new RestAPIResponse("success", "Applicant updated successfully", updatedApplicant));
		} else {
			logger.warn("Applicant not found for ID: {}", id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestAPIResponse("fail", "Applicant not found"));
		}
	}
}
