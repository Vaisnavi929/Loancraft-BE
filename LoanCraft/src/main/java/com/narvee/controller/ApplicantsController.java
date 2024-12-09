package com.narvee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.ApplicantService;
import com.narvee.request.dto.ApplicantDto;

@RestController
@RequestMapping("/api/applicants")
@CrossOrigin
public class ApplicantsController {

	@Autowired
	private ApplicantService applicantService;

	@PostMapping
	public ResponseEntity<ApplicantDto> saveApplicant(@RequestBody ApplicantDto applicantDto) {
		ApplicantDto savedApplicant = applicantService.save(applicantDto);
		return ResponseEntity.ok(savedApplicant);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApplicantDto> getApplicantById(@PathVariable Long id) {
		ApplicantDto applicant = applicantService.getById(id);
		return applicant != null ? ResponseEntity.ok(applicant) : ResponseEntity.notFound().build();
	}

	@GetMapping
	public ResponseEntity<List<ApplicantDto>> getAllApplicants() {
		List<ApplicantDto> applicants = applicantService.getAllApplicants();
		return ResponseEntity.ok(applicants);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteApplicantById(@PathVariable Long id) {
		boolean isDeleted = applicantService.deleteById(id);
		return isDeleted ? ResponseEntity.ok("Applicant deleted successfully.")
				: ResponseEntity.status(404).body("Applicant not found.");
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApplicantDto> updateApplicant(@PathVariable Long id,
			@RequestBody ApplicantDto updatedApplicantDto) {
		ApplicantDto applicant = applicantService.update(id, updatedApplicantDto);
		return applicant != null ? ResponseEntity.ok(applicant) : ResponseEntity.notFound().build();
	}
}
