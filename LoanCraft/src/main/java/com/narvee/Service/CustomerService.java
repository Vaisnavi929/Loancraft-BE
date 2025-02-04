package com.narvee.Service;

import org.springframework.data.domain.Page;

import com.narvee.entity.Customers;
import com.narvee.request.dto.ApplicantDto;
import com.narvee.request.dto.CustomerResponseDTO;
import com.narvee.request.dto.CustomerUpdateRequest;
import com.narvee.request.dto.RegisterVO;
import com.narvee.request.dto.SortingDTO;

public interface CustomerService {

	Customers save(RegisterVO register);

	Customers getById(String encryptedId) throws Exception;

	boolean deleteById(Long id);

	CustomerUpdateRequest update(CustomerUpdateRequest customerUpdateDto) throws Exception;

	Page<CustomerResponseDTO> getAllApplicants(SortingDTO sortingDTO);

}
