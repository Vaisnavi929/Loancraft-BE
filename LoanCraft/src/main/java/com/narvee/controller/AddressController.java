package com.narvee.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.narvee.Service.AddressServices;
import com.narvee.commons.RestAPIResponse;
import com.narvee.request.dto.AddressDto;

import com.narvee.util.EncryptionUtil;

import com.narvee.request.dto.AddressUpdateRequest;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/address")
public class AddressController {

	public static final Logger logger = LoggerFactory.getLogger(AddressController.class);

	@Autowired
	private AddressServices AddressService;

//	@RequestMapping(value = "/saveCurrentAddress", method = RequestMethod.POST, produces = "application/json")
//	public ResponseEntity<RestAPIResponse> saveCurrentAddress(@RequestBody AddressDto currentAddress) {
//		logger.info("!!! inside class: AddressServiceImpl, method: saveCurrentAddress");
//
//		try {
//			// Decrypt customerId
//			long decryptedCustomerId = EncryptionUtil.decrypt(currentAddress.getCustomerId());
//			currentAddress.setCustomerId(String.valueOf(decryptedCustomerId));
//
//			// Save current address
//			AddressDto savedAddress = AddressService.saveCurrentAddress(currentAddress);
//
//			// Re-encrypt customerId before sending the response
//			savedAddress.setCustomerId(EncryptionUtil.encrypt(Long.parseLong(savedAddress.getCustomerId())));
//			logger.info("Address saved successfully for Customer ID: {}", currentAddress.getCustomerId());
//
//			return ResponseEntity.ok(new RestAPIResponse("success", "Address saved successfully", savedAddress));
//		} catch (Exception e) {
//			logger.error("Error saving address: {}", e.getMessage(), e);
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body(new RestAPIResponse("fail", "Error saving address"));
//		}
//
//	}
//
//	@PostMapping(value = "/savePermanentAddress")
//	public ResponseEntity<RestAPIResponse> savePermanentAddress(@RequestBody AddressDto permanentAddress) {
//		logger.info("!!! inside class: AddressServiceImpl, method: savePermanentAddress");
//
//		try {
//			// Decrypt customerId
//			long decryptedCustomerId = EncryptionUtil.decrypt(permanentAddress.getCustomerId());
//			permanentAddress.setCustomerId(String.valueOf(decryptedCustomerId));
//
//			// Save current address
//			AddressDto savePermanentAddress = AddressService.savePermanentAddress(permanentAddress);
//
//			// Re-encrypt customerId before sending the response
//			savePermanentAddress
//					.setCustomerId(EncryptionUtil.encrypt(Long.parseLong(savePermanentAddress.getCustomerId())));
//			logger.info("Address saved successfully for Customer ID: {}", savePermanentAddress.getCustomerId());
//
//			return ResponseEntity
//					.ok(new RestAPIResponse("success", "Address saved successfully", savePermanentAddress));
//		} catch (Exception e) {
//			logger.error("Error saving address: {}", e.getMessage(), e);
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body(new RestAPIResponse("fail", "Error saving address"));
//		}
//	}

	@PutMapping(value = "/updateAddress/{customerId}")
	public ResponseEntity<RestAPIResponse> updateAddress(@RequestBody AddressUpdateRequest addressUpdateRequest,
			@PathVariable String customerId) throws Exception {
		logger.info("!!! inside class: AddressServiceImpl, method: updateAddress");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully updated address",
				AddressService.updateAddress(addressUpdateRequest, customerId)), HttpStatus.OK);

	}

}
