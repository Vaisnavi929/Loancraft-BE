package com.narvee.serviceimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.narvee.Service.CustomerCallTrackingService;
import com.narvee.entity.CustomerCallTracking;
import com.narvee.repository.CustomerCallTrackingRepo;

@Repository
public class CustomerCallTrackingServicesImpl implements CustomerCallTrackingService {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerCallTrackingServicesImpl.class);
	
	@Autowired
	private CustomerCallTrackingRepo customerCallTrackingRepo;

	@Override
	public CustomerCallTracking saveCctAll(CustomerCallTracking cct) {
		logger.info("!!! inside class : CustomerCallTrackingServicesImpl,!! method: saveCctAll()  ");
		return customerCallTrackingRepo.save(cct);
	}

	@Override
	public List<CustomerCallTracking> getCctAll() {
		logger.info("!!! inside class : CustomerCallTrackingServicesImpl,!! method: getCctAll()  ");
		return customerCallTrackingRepo.findAll();
	}

}
