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
import com.narvee.entity.CustomersInfo;
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

	public CustomersInfo save(RegisterVO register) {
		logger.info("!!! inside class: CustomerServiceImpl, method: save()");
		Optional<CustomersInfo> existingApplicant = customerRepository.findByEmail(register.getEmail());

		if (existingApplicant.isPresent()) {
			throw new IllegalArgumentException("Email already exists!");
		}
		CustomersInfo customer = modelMapper.map(register, CustomersInfo.class);
		if (customer.getPassword() != null && !customer.getPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(customer.getPassword());
			customer.setPassword(encodedPassword);
			String accountNumber = accountSequenceService.generateAccountNumber();
			customer.setAccountNumber(accountNumber);
		}
//		CustomersInfo saveCustomers = customerRepository.save(customer);

		customer.setAccountNumber(accountSequenceService.generateAccountNumber());

		CustomersInfo savedCustomer = customerRepository.save(customer);

		logger.info("Successfully saved customer with ID: {}", savedCustomer.getCustomerId());

		return savedCustomer;
	}

	@Override
	public CustomersInfo getById(String encryptedId) throws Exception {
		logger.info("!!! inside class: CustomerServiceImpl, method: getById()");

		long decryptedCustomerId = EncryptionUtil.decrypt(encryptedId);
		logger.info("Decrypted customer ID: {}", decryptedCustomerId);
		// Fetch the customer details from the repository using the decrypted ID
		CustomersInfo customer = customerRepository.findById(decryptedCustomerId).orElse(null);

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
	public CustomersInfo update(CustomerUpdateRequest customerUpdateDto) {
		logger.info("!!! inside class: CustomerServiceImpl, method: update()");

		try {
			// Parse the customer ID from the request DTO
			long decryptedCustomerId = Long.parseLong(customerUpdateDto.getCustomerId());
			logger.info("Decrypted customer ID: {}", decryptedCustomerId);


			// Fetch the customer by ID or throw an exception if not found
			CustomersInfo customer = customerRepository.findById(decryptedCustomerId)
					.orElseThrow(() -> new RuntimeException("Customer not found with ID: " + decryptedCustomerId));


			logger.info("Fetched customer: {}", customer);

			// Retain sensitive fields (e.g., password and account number)
			String originalPassword = customer.getPassword();
			String originalAccountNumber = customer.getAccountNumber();

			// Map updated fields from DTO to the customer entity
			modelMapper.map(customerUpdateDto, customer);

			// Restore sensitive fields to prevent overwriting them
			customer.setPassword(originalPassword);
			customer.setAccountNumber(originalAccountNumber);

			logger.info("Updated customer object: {}", customer);

			// Save and return the updated customer
			return customerRepository.save(customer);

		} catch (Exception ex) {
			logger.error("Unexpected error during update: {}", ex.getMessage(), ex);
			throw new RuntimeException("An unexpected error occurred during customer update: " + ex.getMessage(), ex);
		}
	}

	@Override
	public Long checkCustomerEligibility(String customerId) throws Exception {
		logger.info("!!! inside class: CustomerServiceImpl, method: checkCustomerEligibility()");
		long decryptedCustomerId = EncryptionUtil.decrypt(customerId);
		logger.info("Decrypted customer ID: {}", decryptedCustomerId);

		return customerRepository.checkEligibility(decryptedCustomerId);
	}

}
