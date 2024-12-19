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

import com.narvee.Service.AccountSequenceService;
import com.narvee.Service.CustomerService;
import com.narvee.entity.Customers;
import com.narvee.repository.CustomersRepository;
import com.narvee.request.dto.ApplicantDto;
import com.narvee.request.dto.RegisterVO;
import com.narvee.request.dto.SortingDTO;

@Service
public class CustomerServiceImpl implements CustomerService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	private CustomersRepository customerRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AccountSequenceService accountSequenceService;

	@Override
	public ApplicantDto save(RegisterVO register) {
		logger.info("!!! inside class: ApplicantServiceImpl, method: save()");
		
		Optional<Customers> existingApplicant = customerRepository.findByEmail(register.getEmail());
	    if (existingApplicant.isPresent()) {
	        throw new IllegalArgumentException("Email already exists!");
	    }
	    Customers applicant = modelMapper.map(register, Customers.class);
		if (applicant.getPassword() != null && !applicant.getPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(applicant.getPassword());
			applicant.setPassword(encodedPassword);
		String accountNumber=	accountSequenceService.generateAccountNumber();
			
			applicant.setAccountNumber(accountNumber);
		}
		Customers savedApplicant = customerRepository.save(applicant);

		return modelMapper.map(savedApplicant, ApplicantDto.class);
	}

	@Override
	public Customers getById(Long id) {
		logger.info("!!! inside class: ApplicantServiceImpl, method: getById()");		
		return customerRepository.findById(id).orElse(null);
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
			sortField = "first_name";
		} else if (sortField.equalsIgnoreCase("Lastname")) {
			sortField = "last_name";
		} else if (sortField.equalsIgnoreCase("Email")) {
			sortField = "email";
		} else if (sortField.equalsIgnoreCase("MobileNo")) {
			sortField = "mobile_no";
		} else if (sortField.equalsIgnoreCase("Password")) {
			sortField = "password";
		} else {
			sortField = "applicant_id";
		}

		Sort.Direction sortDirection = Sort.Direction.ASC;
		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
		}

		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(sortDirection, sortField));

		Page<Customers> applicantsPage;

		if (keyword == null || keyword.isEmpty()) {
			applicantsPage = customerRepository.findAllApplicants(pageable);
		} else {
			applicantsPage = customerRepository.findApplicantsByKeyword(keyword, pageable);
		}
		return applicantsPage.map(applicant -> modelMapper.map(applicant, ApplicantDto.class));
	}

	@Override
	public boolean deleteById(Long id) {
		logger.info("!!! inside class: ApplicantServiceImpl, method: deleteById()");
		if (customerRepository.existsById(id)) {
			customerRepository.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public ApplicantDto update(Long id, ApplicantDto updatedApplicantDto) {
		logger.info("!!! inside class: ApplicantServiceImpl, method: update()");
		return customerRepository.findById(id).map(existingApplicant -> {
			// Update fields
			modelMapper.map(updatedApplicantDto, existingApplicant);
			Customers updatedApplicant = customerRepository.save(existingApplicant);
			return modelMapper.map(updatedApplicant, ApplicantDto.class);
		}).orElse(null);
	}

}
