package com.narvee.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.AddressServices;
import com.narvee.commons.RestAPIResponse;
import com.narvee.request.dto.AddressUpdateRequest;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/address")
public class AddressController {

	public static final Logger logger = LoggerFactory.getLogger(AddressController.class);

	@Autowired
	private AddressServices AddressService;

	@PutMapping(value = "/updateAddress/{customerId}")
	public ResponseEntity<RestAPIResponse> updateAddress(@RequestBody AddressUpdateRequest addressUpdateRequest,
			@PathVariable String customerId) throws Exception {
		logger.info("!!! inside class: AddressServiceImpl, method: updateAddress");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully updated address",
				AddressService.updateAddress(addressUpdateRequest, customerId)), HttpStatus.OK);

	}

}
