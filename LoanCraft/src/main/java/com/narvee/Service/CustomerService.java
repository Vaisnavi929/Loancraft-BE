package com.narvee.Service;

import java.util.List;
import com.narvee.entity.CustomerDetails;


public interface CustomerService {
	
	public CustomerDetails save(CustomerDetails customerDetails);

	public CustomerDetails getById(Long id);
	public List<CustomerDetails> getAllApplicantes();
	public boolean deletedById(Long id);

}
