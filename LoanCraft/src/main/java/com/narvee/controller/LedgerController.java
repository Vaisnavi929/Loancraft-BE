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

import com.narvee.Service.LedgerService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.entity.Ledger;
import com.narvee.request.dto.LedgerDTO;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/ledger")
public class LedgerController {
	
	public static final Logger logger=LoggerFactory.getLogger(LedgerController.class);
	
	@Autowired
	private LedgerService ledgerService;
	
	@RequestMapping(value = "/saveledger",method = RequestMethod.POST,produces ="application/json")
	public ResponseEntity<RestAPIResponse> saveLedger(@RequestBody LedgerDTO ledger){
		logger.info("!!! inside class : LedgerController , method : saveLedger");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("Success","saved Ledger Successfully", ledgerService.saveLedger(ledger)), HttpStatus.OK);
		
	}
	@RequestMapping(value ="/updateLedger",method = RequestMethod.PUT,produces ="application/json")
	public ResponseEntity<RestAPIResponse> updateLedger(@RequestBody Ledger ledger){
		logger.info("!!! inside class : LedgerController, method : updateLedger");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("Success", "updated ledger successfully", ledgerService.updateLedger(ledger)), HttpStatus.OK);
		
	}
	                                          
	@RequestMapping(value ="/get/{ledgerId}",method = RequestMethod.GET,produces = "application/json")
	public ResponseEntity<RestAPIResponse> getByIdledger(@PathVariable Long  ledgerId){
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "fetched by id successfully", ledgerService.getById(ledgerId)), HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/getAll",method =RequestMethod.GET,produces ="application/json")
	public ResponseEntity<RestAPIResponse> getllLedger(){
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "fetched all ledger details successfully", ledgerService.getAll()), HttpStatus.OK);
		
	}
	
	@RequestMapping(value ="/delete/{ledgerId}",method =RequestMethod.DELETE,produces ="application/json")
	public ResponseEntity<RestAPIResponse> deleteById(@PathVariable Long ledgerId){
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "deleted by id successfully", ledgerService.deleteById(ledgerId)), HttpStatus.OK);		
	}
}
