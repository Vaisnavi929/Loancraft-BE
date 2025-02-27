package com.narvee.controller;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.CustomerService;
import com.narvee.Service.IAuthenticationService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.entity.CustomersInfo;
import com.narvee.request.dto.CustomerUpdateRequest;
import com.narvee.request.dto.CustomerUpdateResponse;
import com.narvee.request.dto.RegisterVO;
import com.narvee.request.dto.SortingDTO;
import com.narvee.util.EncryptionUtil;

import jakarta.validation.Valid;

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
	public ResponseEntity<RestAPIResponse> saveCustomers(@Valid @RequestBody RegisterVO register) {
		logger.info("Saving customer: {}", register.getEmail());
		try {
			customerService.save(register);

			Object authenticate = authenticationService.authenticate(register.getEmail(), register.getPassword());
			logger.info("customer saved and authenticated successfully: {}", register.getEmail());

			return ResponseEntity
					.ok(new RestAPIResponse("success", "Customer saved and authenticated successfully", authenticate));

		} 
		catch (IllegalArgumentException e) {
			logger.warn("Validation error: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new RestAPIResponse("fail", "Validation error", e.getMessage()));
		} catch (Exception e) {
			logger.error("Error saving customer: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new RestAPIResponse("fail", "Error saving customer Please try again later", e.getMessage()));
		}
	}

	@RequestMapping(value = "/findById/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getCustomerById(@PathVariable String id) throws Exception {
		logger.info("Fetching customer by encrypted ID: {}", id);
		// Call service method to fetch the customer by decrypted ID
		CustomersInfo customer = customerService.getById(id);
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
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully fetched all details",
				customerService.getAllCustomers(sortingDTO)), HttpStatus.OK);
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
	public ResponseEntity<RestAPIResponse> updateApplicant(
			@Valid @RequestBody CustomerUpdateRequest customerUpdateDto) {
		logger.info("Updating customer with encrypted ID: {}", customerUpdateDto.getCustomerId());

		try {
			// Decrypt the customer ID before passing it to the service
			long decryptedCustomerId = EncryptionUtil.decrypt(customerUpdateDto.getCustomerId());
			customerUpdateDto.setCustomerId(String.valueOf(decryptedCustomerId)); // Set the decrypted ID in the DTO

			// Call the service to update the customer
			CustomersInfo updatedCustomer = customerService.update(customerUpdateDto);

			// Encrypt the customerId for the response
			String encryptedCustomerId = EncryptionUtil.encrypt(decryptedCustomerId);

			CustomerUpdateResponse responseCustomer = modelMapper.map(updatedCustomer, CustomerUpdateResponse.class);

			// Set the encrypted customerId in the response DTO
			responseCustomer.setCustomerId(encryptedCustomerId);

			logger.info("Customer updated successfully with encrypted customerId: {}", encryptedCustomerId);

			// Return the success response with updated customer data
			return ResponseEntity.ok(new RestAPIResponse("success", "Customer updated successfully", responseCustomer));

		} catch (RuntimeException e) {
			logger.error("Error updating customer: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new RestAPIResponse("fail", "Error updating customer", e.getMessage()));
		} catch (Exception e) {
			logger.error("Unexpected error during update: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new RestAPIResponse("fail", "Unexpected error occurred during customer update", e.getMessage()));
		}
	}

	@RequestMapping(value = "/checkEligibility/{customerId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> checkAllColumnsNotNull(@PathVariable String customerId) {
		logger.info("Starting eligibility check for customerId: {}", customerId);

		try {
			Long checkCustomerEligibility = customerService.checkCustomerEligibility(customerId);

			if (checkCustomerEligibility == 1) {
				logger.info("Customer with customerId: {} is eligible to apply.", customerId);
				return ResponseEntity.ok(new RestAPIResponse("Sucess", "Eligible to apply for the application.", true));
			} else {
				logger.warn("Customer profile is incomplete for customerId: {}. Returning forbidden response.",
						customerId);
				return ResponseEntity.status(HttpStatus.OK).body(new RestAPIResponse("Fail",
						"Profile is incomplete. Please update your profile before applying.", false));
			}
		} catch (Exception e) {
			logger.error("An error occurred while checking eligibility for customerId: {}. Error: {}", customerId,
					e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new RestAPIResponse(
					"An error occurred while checking eligibility. Please try again later.", e.getMessage()));
		}
	}

	@GetMapping("/check-email/{email}")
    public ResponseEntity<RestAPIResponse> checkDuplicateEmail(@PathVariable String email) {
        boolean isDuplicate = customerService.isEmailDuplicate(email);

        if (isDuplicate) {
         new RestAPIResponse("fail", "Email already exists", true);
        } else {
          new RestAPIResponse("success", "Email is available", false);
        }
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully duplicate check email", isDuplicate), HttpStatus.OK);

       
    }
}
