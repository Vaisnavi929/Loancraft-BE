package com.narvee.controller;

import org.modelmapper.ModelMapper;
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

import com.narvee.Service.CustomerService;
import com.narvee.Service.IAuthenticationService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.entity.Customers;
import com.narvee.request.dto.ApplicantDto;
import com.narvee.request.dto.CustomerUpdateRequest;
import com.narvee.request.dto.CustomerUpdateResponse;
import com.narvee.request.dto.RegisterVO;
import com.narvee.request.dto.SortingDTO;
import com.narvee.util.EncryptionUtil;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin("*")
public class CustomersController {

	private static final Logger logger = LoggerFactory.getLogger(CustomersController.class);

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private IAuthenticationService authenticationService;

	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Object> saveApplicant(@RequestBody RegisterVO register) {
		logger.info("Saving applicant: {}", register.getEmail());
		try {
			customerService.save(register);
			Object authenticate = authenticationService.authenticate(register.getEmail(), register.getPassword());
			logger.info("customer saved and authenticated successfully: {}", register.getEmail());
			return ResponseEntity
					.ok(new RestAPIResponse("success", "Customer saved and authenticated successfully", authenticate));
		} catch (Exception e) {
			logger.error("Error saving customer: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new RestAPIResponse("fail", "Error saving customer"));
		}
	}

	@RequestMapping(value = "/findById/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getCustomerById(@PathVariable String id) throws Exception {
		logger.info("Fetching customer by encrypted ID: {}", id);

		// Call service method to fetch the customer by decrypted ID
		Customers customer = customerService.getById(id);
		System.out.println();

		if (customer != null) {
			String encryptedCustomerId = EncryptionUtil.encrypt(customer.getCustomerId());
			CustomerUpdateResponse responseData = modelMapper.map(customer, CustomerUpdateResponse.class);
			responseData.setCustomerId(encryptedCustomerId);
			return ResponseEntity.ok(new RestAPIResponse("success", "Customer fetched successfully", responseData));
		} else {
			// If customer not found, return not found response
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestAPIResponse("fail", "Customer not found"));
		}
	}

	@RequestMapping(value = "/getAll", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllApplicants(@RequestBody SortingDTO sortingDTO) {
		logger.info("Fetching all customer with sorting: {}", sortingDTO);	
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully fetched all details", customerService.getAllApplicants(sortingDTO)), HttpStatus.OK);
		
	}

	@RequestMapping(value = "/deleteById/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deleteApplicantById(@PathVariable String id) throws Exception {
		logger.info("Deleting customer by ID: {}", id);

		// Decrypt the customerId
		long decryptedCustomerId = EncryptionUtil.decrypt(id);
		logger.info("Decrypted customer ID: {}", decryptedCustomerId);

		boolean isDeleted = customerService.deleteById(decryptedCustomerId);
		if (isDeleted) {
			logger.info("customer deleted successfully: {}", id);
			return ResponseEntity.ok(new RestAPIResponse("success", "customer deleted successfully"));
		} else {
			logger.warn("customer not found for ID: {}", id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new RestAPIResponse("fail", "customer not found"));
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<RestAPIResponse> updateApplicant(@RequestBody CustomerUpdateRequest customerUpdateDto) {
		logger.info("Updating customer with encrypted ID: {}", customerUpdateDto.getCustomerId());

		try {
			// Decrypt the customer ID before passing it to the service
			long decryptedCustomerId = EncryptionUtil.decrypt(customerUpdateDto.getCustomerId());
			customerUpdateDto.setCustomerId(String.valueOf(decryptedCustomerId)); // Set the decrypted ID in the DTO

			// Call the service to update customer
			CustomerUpdateRequest updatedCustomer = customerService.update(customerUpdateDto);

			// Encrypt the customerId for the response
			updatedCustomer.setCustomerId(EncryptionUtil.encrypt(Long.parseLong(updatedCustomer.getCustomerId())));

			logger.info("Customer updated successfully");
			return ResponseEntity.ok(new RestAPIResponse("success", "Customer updated successfully", updatedCustomer));
		} catch (Exception e) {
			logger.error("Error updating customer: {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new RestAPIResponse("fail", "Error updating customer"));
		}
	}

}
