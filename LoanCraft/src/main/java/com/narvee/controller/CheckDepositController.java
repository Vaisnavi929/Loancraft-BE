package com.narvee.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.CheckDepositService;
import com.narvee.Service.SortingRequestDto;
import com.narvee.commons.RestAPIResponse;
import com.narvee.entity.CheckDeposit;
import com.narvee.request.dto.CheckDepositDTO;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth/checkDeposit")
public class CheckDepositController {
	
	private static final Logger logger=LoggerFactory.getLogger(CheckDepositController.class);
	
	@Autowired
	private CheckDepositService depositService;
	
	@RequestMapping(value ="/saveCheck",method =RequestMethod.POST,produces = "application/json")
	public ResponseEntity<RestAPIResponse> saveCheckDeposit(@RequestBody CheckDepositDTO checkDepositDTO ){
		logger.info("!!! inside class:CheckDepositController, !! method: saveCheckDeposit() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "succesfully saved CheckDeposit details", depositService.saveDeposit(checkDepositDTO)), HttpStatus.OK);
		
	}
	@RequestMapping(value ="/updateCheck",method =RequestMethod.PUT,produces = "application/json")
	public ResponseEntity<RestAPIResponse> updateCheckDeposit(@RequestBody CheckDepositDTO checkDepositDTO){
		logger.info("!!! inside class:CheckDepositController,!! method: updateCheckDeposit() ");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "succesfully updated CheckDeposit details", depositService.updateDeposit(checkDepositDTO)), HttpStatus.OK);
		
	}
	@RequestMapping(value ="/getAll",method =RequestMethod.POST,produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllCheckDeposit(@RequestBody SortingRequestDto sortingRequestDto){
		logger.info("!!! inside class:CheckDepositController,!! method:getAllCheckBounce()");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully getall checkDeposit details", depositService.getAllCheckDeposit(sortingRequestDto)), HttpStatus.OK) ;		
	}
	
	@RequestMapping(value = "/UpdateStatus/{id}/{value}/{reason}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> UpdateStatus(@PathVariable("id") Long id,@PathVariable("value") String value,@PathVariable("reason") String reason){
		logger.info("!!! inside class:CheckDepositController,!! method:UpdateStatus()");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully Updated status", depositService.updateStatus(id, value, reason)), HttpStatus.OK);
		
	}
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getById(@PathVariable("id") Long id){
		logger.info("!!! inside class:CheckDepositController,!! method:getById()");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully fetched ById status", depositService.getById(id)), HttpStatus.OK);
		
	}
}
