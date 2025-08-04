package com.narvee.Service;

import java.util.List;

import com.narvee.entity.CustomerCallTracking;

public interface CustomerCallTrackingService {

	public CustomerCallTracking saveCctAll(CustomerCallTracking customerCallTracking);

	public List<CustomerCallTracking> getCctAll();

}
