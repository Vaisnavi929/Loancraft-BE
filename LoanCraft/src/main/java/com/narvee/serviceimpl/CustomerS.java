package com.narvee.serviceimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.Service.CustomerSer;
import com.narvee.entity.CustomerDetails;
import com.narvee.repository.CustomerRepository;

@Service
public class CustomerS implements CustomerSer{
	
	public static final Logger logger = LoggerFactory.getLogger(CustomerS.class);
	
	@Autowired
	private CustomerRepository customerRepository; 

	@Override
	public CustomerDetails save(CustomerDetails customerDetails) {	
		logger.info("!!! inside class: CustomerServiceImpl , !! method: save()");
		
		return customerRepository.save(customerDetails);
	}

	@Override
	public CustomerDetails getById(Long id) {
		logger.info("!!! inside class: CustomerServiceImpl , !! method: getById()");
		return customerRepository.findById(id).get();
	}

	@Override
	public List<CustomerDetails> getAllApplicantes() {
		logger.info("!!! inside class: CustomerServiceImpl , !! method: getAllApplicantes()");
		return customerRepository.findAll();
	}
	@Override
	public boolean deletedById(Long id) {
	logger.info("!!! inside class: CustomerServiceImpl , !! method: deletedById()");
	customerRepository.deleteById(id);
	return true;
	}

}
