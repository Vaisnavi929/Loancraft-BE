package com.narvee.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.CheckBounceService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.entity.CheckBounceEntry;
import com.narvee.request.dto.CheckBounceDTO;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth/checkBounce")
public class CheckBounceController {
	
	private static final Logger logger=LoggerFactory.getLogger(CheckBounceController.class);
	
	@Autowired
	private CheckBounceService bounceService;
	
	@RequestMapping(value ="/saveCheck",method =RequestMethod.POST,produces = "application/json")
	public ResponseEntity<RestAPIResponse> saveCheckBounce(@RequestBody CheckBounceDTO checkBounceDTO ){
		logger.info("!!! inside class:CheckBounceController,method: saveCheckBounce ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "succesfully saved CheckBounce details", bounceService.saveBounceEntry(checkBounceDTO)), HttpStatus.OK);
		
	}
	@RequestMapping(value ="/updateCheck",method =RequestMethod.PUT,produces = "application/json")
	public ResponseEntity<RestAPIResponse> updateCheckBounce(@RequestBody CheckBounceDTO checkBounceDTO){
		logger.info("!!! inside class:CheckBounceController,method: saveCheckBounce ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "succesfully updated CheckBounce details", bounceService.updateBounceEntry(checkBounceDTO)), HttpStatus.OK);
		
	}
	@RequestMapping(value ="/getAll",method =RequestMethod.GET,produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllCheckBounce(){
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully getall checkBounce details", bounceService.getAllCheckBounce()), HttpStatus.OK) ;
		
		
	}
	
	
}
