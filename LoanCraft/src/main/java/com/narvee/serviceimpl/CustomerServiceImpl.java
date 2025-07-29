package com.narvee.serviceimpl;

import java.util.List;
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
import com.narvee.request.dto.ApplicationResponseDto;
import com.narvee.request.dto.CustomerResponseDTO;
import com.narvee.request.dto.CustomerResponseDTOS;
import com.narvee.request.dto.CustomerUpdateRequest;
import com.narvee.request.dto.RegisterVO;
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
		Optional<CustomersInfo> existingApplicant = customerRepository.findByEmailOrMobileNumber(register.getEmail(),
				register.getEmail());

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
	public Page<CustomerResponseDTOS> getAllCustomers(SortingDTO sortingDTO) {

		logger.info("Inside ApplicantServiceImpl: getAllApplicants");

		String sortField = sortingDTO.getSortField();
		String sortOrder = sortingDTO.getSortOrder();
		String keyword = sortingDTO.getKeyword();
		Integer pageNumber = sortingDTO.getPageNumber();
		Integer pageSize = sortingDTO.getPageSize();

		if (sortField.equalsIgnoreCase("firstName")) {
			sortField = "full_name";
		} else if (sortField.equalsIgnoreCase("Email")) {
			sortField = "email";
		} else if (sortField.equalsIgnoreCase("MobileNumber")) {
			sortField = "mobile_number";
		} else if (sortField.equalsIgnoreCase("AccountNnumber")) {
			sortField = "account_number";
		} else {
			sortField = "updateddate";
		}

		Sort.Direction sortDirection = Sort.Direction.ASC;
		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
		}

		Sort sort = Sort.by(sortDirection, sortField);
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
		Page<CustomerResponseDTO> customerPage;
		if (keyword.equalsIgnoreCase("empty")) {
			customerPage = customerRepository.findAllApplicants(pageable);
		} else {
			logger.info("Inside ApplicantServiceImpl: getAllApplicants, searching ");
			customerPage = customerRepository.findApplicantsByKeyword(keyword, pageable);
		}
		Page<CustomerResponseDTOS> encryptedPage = customerPage.map(dto -> {
			// Encrypt the customerId (convert to String first)
			String encryptedId = null;
			try {
				encryptedId = EncryptionUtil.encrypt(dto.getCustomerId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new CustomerResponseDTOS(encryptedId, dto.getEmail(), dto.getAccountNumber(), dto.getFullName(),
					dto.getMobileNumber());
		});

		return encryptedPage;
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

	@Override
	public boolean isEmailDuplicate(String email) {

		return customerRepository.existsByEmail(email);
	}

	@Override
	public List<ApplicationResponseDto> getApplicationsByCustomerId(String encryptedCustomerId) {
		try {
			long decryptedId = EncryptionUtil.decrypt(encryptedCustomerId);

			return customerRepository.findApplicationsByCustomerId(decryptedId);
		} catch (Exception e) {
			e.printStackTrace(); // Detailed stack trace
			throw new RuntimeException("Invalid encrypted customer ID", e);
		}
	}

}
