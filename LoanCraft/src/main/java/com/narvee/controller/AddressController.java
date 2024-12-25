package com.narvee.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.AddressServices;
import com.narvee.commons.RestAPIResponse;
import com.narvee.request.dto.AddressDto;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/address")
public class AddressController {
	
	
	public static final Logger logger=LoggerFactory.getLogger(AddressController.class);
	
	@Autowired
	private AddressServices AddressService;
	
	@RequestMapping(value = "/saveCurrentAddress",method = RequestMethod.POST,produces ="application/json")
	public ResponseEntity<RestAPIResponse> saveCurrentAddress(@RequestBody AddressDto  currentAddress){
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "Successfully current address saved ", AddressService.saveCurrentAddress(currentAddress)) , HttpStatus.OK);
		
	}

@PostMapping(value = "/savePermanentAddress")
	public ResponseEntity<RestAPIResponse>  savePermanentAddress(@RequestBody AddressDto addressDto){
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully saved permanentAddress", AddressService.savePermanentAddress(addressDto)), HttpStatus.OK);
		
	}
	
}
