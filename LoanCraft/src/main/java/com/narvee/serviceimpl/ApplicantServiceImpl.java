package com.narvee.serviceimpl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.narvee.Service.ApplicantService;
import com.narvee.entity.Applicants;
import com.narvee.repository.ApplicantsRepository;
import com.narvee.request.dto.ApplicantDto;
import com.narvee.request.dto.SortingDTO;

@Service
public class ApplicantServiceImpl implements ApplicantService {

	private static final Logger logger = LoggerFactory.getLogger(ApplicantServiceImpl.class);

	@Autowired
	private ApplicantsRepository applicantsRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public ApplicantDto save(ApplicantDto applicantDto) {
		logger.info("!!! inside class: ApplicantServiceImpl, method: save()");
		
		Optional<Applicants> existingApplicant = applicantsRepository.findByEmail(applicantDto.getEmail());
	    if (existingApplicant.isPresent()) {
	        throw new IllegalArgumentException("Email already exists!");
	    }
		Applicants applicant = modelMapper.map(applicantDto, Applicants.class);
		if (applicant.getPassword() != null && !applicant.getPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(applicant.getPassword());
			applicant.setPassword(encodedPassword);
		}
		Applicants savedApplicant = applicantsRepository.save(applicant);

		return modelMapper.map(savedApplicant, ApplicantDto.class);
	}

	@Override
	public ApplicantDto getById(Long id) {
		logger.info("!!! inside class: ApplicantServiceImpl, method: getById()");
		return applicantsRepository.findById(id).map(applicant -> modelMapper.map(applicant, ApplicantDto.class))
				.orElse(null);
	}

	@Override
	public Page<ApplicantDto> getAllApplicants(SortingDTO sortingDTO) {
		logger.info("Inside ApplicantServiceImpl: getAllApplicants");

		String sortField = sortingDTO.getSortField();
		String sortOrder = sortingDTO.getSortOrder();
		String keyword = sortingDTO.getKeyword();
		Integer pageNumber = sortingDTO.getPageNumber();
		Integer pageSize = sortingDTO.getPageSize();

		if (sortField.equalsIgnoreCase("Name")) {
			sortField = "firstName";
		} else if (sortField.equalsIgnoreCase("Lastname")) {
			sortField = "lastName";
		} else if (sortField.equalsIgnoreCase("Email")) {
			sortField = "email";
		} else {
			sortField = "applicantId";
		}

		Sort.Direction sortDirection = Sort.Direction.ASC;
		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
		}

		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(sortDirection, sortField));

		Page<Applicants> applicantsPage;

		if (keyword == null || keyword.isEmpty()) {
			applicantsPage = applicantsRepository.findAllApplicants(pageable);
		} else {
			applicantsPage = applicantsRepository.findApplicantsByKeyword(keyword, pageable);
		}
		return applicantsPage.map(applicant -> modelMapper.map(applicant, ApplicantDto.class));
	}
	 
	@Override
	public boolean deleteById(Long id) {
		logger.info("!!! inside class: ApplicantServiceImpl, method: deleteById()");
		if (applicantsRepository.existsById(id)) {
			applicantsRepository.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public ApplicantDto update(Long id, ApplicantDto updatedApplicantDto) {
		logger.info("!!! inside class: ApplicantServiceImpl, method: update()");
		return applicantsRepository.findById(id).map(existingApplicant -> {
			// Update fields
			modelMapper.map(updatedApplicantDto, existingApplicant);
			Applicants updatedApplicant = applicantsRepository.save(existingApplicant);
			return modelMapper.map(updatedApplicant, ApplicantDto.class);
		}).orElse(null);
	}

}
