package com.narvee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.Service.CustomerService;
import com.narvee.entity.CustomerDetails;

@RestController
@CrossOrigin
@RequestMapping("/api/cust")
public class CustomerController {
//	public static final Logger logger = LoggerFactory.getLogger(ApplicantesController.class);
	
	@Autowired
	private CustomerService customerService;
	
	@RequestMapping(value ="/saveApplicantes",method = RequestMethod.POST,produces ="application/json")
	public CustomerDetails saveApplicantes(@RequestBody CustomerDetails customerDetails){
//		logger.info("!!! inside class: ApplicationController, !! method :saveApplicantes()");
		return customerService.save(customerDetails);
		
	}
	
}
