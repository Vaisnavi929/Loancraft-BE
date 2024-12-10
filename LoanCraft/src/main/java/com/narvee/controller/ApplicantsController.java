package com.narvee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.ApplicantService;
import com.narvee.Service.IAuthenticationService;
import com.narvee.request.dto.ApplicantDto;

@RestController
@RequestMapping("/api/applicants")
@CrossOrigin
public class ApplicantsController {

	@Autowired
	private ApplicantService applicantService;

	@Autowired
	private IAuthenticationService authenticationService;

	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> saveApplicant(@RequestBody ApplicantDto applicantDto) {
		applicantService.save(applicantDto);

		Object authenticate = authenticationService.authenticate(applicantDto.getEmail(), applicantDto.getPassword());

		return ResponseEntity.ok(authenticate);
	}

	@RequestMapping(value = "/findById/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<ApplicantDto> getApplicantById(@PathVariable Long id) {
		ApplicantDto applicant = applicantService.getById(id);
		return applicant != null ? ResponseEntity.ok(applicant) : ResponseEntity.notFound().build();
	}

	@RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<ApplicantDto>> getAllApplicants() {
		List<ApplicantDto> applicants = applicantService.getAllApplicants();
		return ResponseEntity.ok(applicants);
	}

	@RequestMapping(value = "/deleteById/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<String> deleteApplicantById(@PathVariable Long id) {
		boolean isDeleted = applicantService.deleteById(id);
		return isDeleted ? ResponseEntity.ok("Applicant deleted successfully.")
				: ResponseEntity.status(404).body("Applicant not found.");
	}

	@RequestMapping(value = "/updateById/{id}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<ApplicantDto> updateApplicant(@PathVariable Long id,
			@RequestBody ApplicantDto updatedApplicantDto) {
		ApplicantDto applicant = applicantService.update(id, updatedApplicantDto);
		return applicant != null ? ResponseEntity.ok(applicant) : ResponseEntity.notFound().build();
	}
}
