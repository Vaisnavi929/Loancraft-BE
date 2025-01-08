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
import com.narvee.entity.CurrentAddress;
import com.narvee.entity.Customers;
import com.narvee.entity.PermanentAddress;
import com.narvee.entity.States;
import com.narvee.repository.CustomersRepository;
import com.narvee.request.dto.CustomerUpdateRequest;
import com.narvee.request.dto.RegisterVO;
import com.narvee.request.dto.SearchingDTO;
import com.narvee.request.dto.SortingDTO;
import com.narvee.util.EncryptionUtil;

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
	public Customers save(RegisterVO register) {
		logger.info("!!! inside class: CustomerServiceImpl, method: save()");
		Optional<Customers> existingApplicant = customerRepository.findByEmail(register.getEmail());
		if (existingApplicant.isPresent()) {
			throw new IllegalArgumentException("Email already exists!");
		}

		Customers applicant = modelMapper.map(register, Customers.class);
		if (applicant.getPassword() != null && !applicant.getPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(applicant.getPassword());
			applicant.setPassword(encodedPassword);
			String accountNumber = accountSequenceService.generateAccountNumber();
			applicant.setAccountNumber(accountNumber);
		}
		Customers savedApplicant = customerRepository.save(applicant);

		return savedApplicant;
	}

	@Override
	public Customers getById(String encryptedId) throws Exception {
		logger.info("!!! inside class: CustomerServiceImpl, method: getById()");

		long decryptedCustomerId = EncryptionUtil.decrypt(encryptedId);
		logger.info("Decrypted customer ID: {}", decryptedCustomerId);
		// Fetch the customer details from the repository using the decrypted ID
		Customers customer = customerRepository.findById(decryptedCustomerId).orElse(null);

		if (customer.getPermanentAddress() == null) {
			PermanentAddress permanentAddress = new PermanentAddress();
			customer.setPermanentAddress(permanentAddress);
			States states = new States();
			permanentAddress.setState(states);

		}

		if (customer.getCurrentAddress() == null) {
			CurrentAddress currentAddress = new CurrentAddress();
			customer.setCurrentAddress(currentAddress);
			States states = new States();
			currentAddress.setState(states);

		}

		return customer;
	}

	@Override
	public Page<SearchingDTO> getAllCustomers(SortingDTO sortingDTO) {
		logger.info("Inside ApplicantServiceImpl: getAllApplicants");

		String sortField = sortingDTO.getSortField();
		String sortOrder = sortingDTO.getSortOrder();
		String keyword = sortingDTO.getKeyword();
		Integer pageNumber = sortingDTO.getPageNumber();
		Integer pageSize = sortingDTO.getPageSize();

		if (sortField.equalsIgnoreCase("HighestQualification")) {
			sortField = "highest_qualification";
		} else if (sortField.equalsIgnoreCase("Lastname")) {
			sortField = "last_name";
		} else if (sortField.equalsIgnoreCase("Email")) {
			sortField = "email";
		} else if (sortField.equalsIgnoreCase("MobileNo")) {
			sortField = "mobile_no";
		} else if (sortField.equalsIgnoreCase("Dob")) {
			sortField = "dob";
		} else if (sortField.equalsIgnoreCase("FathersOrSpousesName")) {
			sortField = "fathers_or_spouses_name";
		} else if (sortField.equalsIgnoreCase("AdhaarNumber")) {
			sortField = "adhaar_number";
		} else if (sortField.equalsIgnoreCase("AccountNumber")) {
			sortField = "account_number";
		} else if (sortField.equalsIgnoreCase("MiddleName")) {
			sortField = "middle_name";
		} else {
			sortField = "first_name";
		}

		Sort.Direction sortDirection = Sort.Direction.ASC;
		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
		}

		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(sortDirection, sortField));

		if (keyword == null || keyword.isEmpty()) {
			return customerRepository.findAllCustomers(pageable);
		} else {
			return customerRepository.findCustomersByKeyword(keyword, pageable);
		}
	}

	@Override
	public boolean deleteById(Long id) {
		logger.info("!!! inside class: CustomerServiceImpl, method: deleteById()");
		if (customerRepository.existsById(id)) {
			customerRepository.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public CustomerUpdateRequest update(CustomerUpdateRequest customerUpdateDto) throws Exception {

		logger.info("!!! inside class: CustomerServiceImpl, method: update()");

		// Decrypt the encrypted customerId
		long decryptedCustomerId = Long.parseLong(customerUpdateDto.getCustomerId());
		logger.info("Decrypted customer ID: {}", decryptedCustomerId);

		// Fetch the customer by decrypted ID
		Customers customer = customerRepository.findById(decryptedCustomerId)
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		// Retain sensitive fields
		String password = customer.getPassword();
		String accountNumber = customer.getAccountNumber();

		// Map updated fields
		modelMapper.map(customerUpdateDto, customer);
		customer.setPassword(password);
		customer.setAccountNumber(accountNumber);

		// Save the updated customer
		customer = customerRepository.save(customer);

		// Map to DTO for response
		CustomerUpdateRequest updatedDto = modelMapper.map(customer, CustomerUpdateRequest.class);

		return updatedDto;
	}

	@Override
	public Long checkCustomerEligibility(String customerId) throws Exception {
		logger.info("!!! inside class: CustomerServiceImpl, method: checkCustomerEligibility()");
		long decryptedCustomerId = EncryptionUtil.decrypt(customerId);
		logger.info("Decrypted customer ID: {}", decryptedCustomerId);

		return customerRepository.checkEligibility(decryptedCustomerId);
	}

}
