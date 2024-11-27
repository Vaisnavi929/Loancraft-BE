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

import com.narvee.Service.PaymentService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.request.dto.PaymentDTO;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/payment")
public class PaymentController {
	private static final Logger logger=LoggerFactory.getLogger(PaymentController.class);
	
	
	@Autowired
	private PaymentService paymentService;
	
	@RequestMapping(value ="/savePayment",method=RequestMethod.POST,produces = "application/json")
	public ResponseEntity<RestAPIResponse> savePayment(@RequestBody PaymentDTO payment){
		logger.info("!!! inside class : PaymentController , !! method : savePayment ");
		
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", " saved payment successfully", paymentService.savePayment(payment)), HttpStatus.OK);
		
	}
	
	@RequestMapping(value ="/updatePayment",method = RequestMethod.PUT,produces ="application/json")
	public ResponseEntity<RestAPIResponse> updatePayment(@RequestBody PaymentDTO payment){
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "updated payment successfully", paymentService.updatePayment(payment)), HttpStatus.OK);
	}

	@RequestMapping(value ="/getbyId/{payamentId}",method = RequestMethod.GET,produces ="application/json")
	public ResponseEntity<RestAPIResponse> getById(@PathVariable Long payamentId){
		logger.info("!!! inside class : PaymentController , !! method : getById");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "fetched by paymentid successfully", paymentService.getById(payamentId)), HttpStatus.OK);
		
	}
	
	@RequestMapping(value ="/All",method = RequestMethod.GET,produces ="application/json")
	public ResponseEntity<RestAPIResponse> getAllPayments(){
		logger.info("!!! inside class : PaymentController ,!! method : getAllPayments");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse(" ","fetched All payments details", paymentService.getAll()), HttpStatus.OK);
		
	}
	@RequestMapping(value = "/delete/{paymentId}",method=RequestMethod.DELETE)
	public ResponseEntity<RestAPIResponse> deletedById(@PathVariable Long payamentId){
		logger.info("!!! inside class : PaymentController , !! method : getAllPayments");
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "deleted by paymentid successfullly", paymentService.deleteById(payamentId)), HttpStatus.OK);
		
	}
	
}
