package com.narvee.Service;

import org.springframework.data.domain.Page;
import com.narvee.entity.Customers;
import com.narvee.request.dto.ApplicantDto;
import com.narvee.request.dto.CustomerUpdateDto;
import com.narvee.request.dto.RegisterVO;
import com.narvee.request.dto.SortingDTO;

public interface CustomerService {

	Customers save(RegisterVO register);

	Customers getById(String encryptedId) throws Exception;

	boolean deleteById(Long id);

	CustomerUpdateDto update(CustomerUpdateDto customerUpdateDto) ;

	Page<ApplicantDto> getAllApplicants(SortingDTO sortingDTO);

}
