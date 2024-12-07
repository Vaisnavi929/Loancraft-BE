package com.narvee.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.narvee.Service.ApplicantService;
import com.narvee.entity.Applicants;
import com.narvee.repository.ApplicantsRepository;
import com.narvee.request.dto.ApplicantDto;

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
		Applicants applicant = modelMapper.map(applicantDto, Applicants.class);

		if (applicant.getEmailPassword() != null && !applicant.getEmailPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(applicant.getEmailPassword());
			applicant.setEmailPassword(encodedPassword);
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
	public List<ApplicantDto> getAllApplicants() {
		logger.info("!!! inside class: ApplicantServiceImpl, method: getAllApplicants()");
		return applicantsRepository.findAll().stream().map(applicant -> modelMapper.map(applicant, ApplicantDto.class))
				.collect(Collectors.toList());
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
