package com.narvee.Service;

import org.springframework.data.domain.Page;

import com.narvee.entity.CustomersInfo;
import com.narvee.request.dto.ApplicantDto;
import com.narvee.request.dto.CustomerUpdateRequest;
import com.narvee.request.dto.RegisterVO;
import com.narvee.request.dto.SortingDTO;

public interface CustomerService {

	CustomersInfo save(RegisterVO register);

	CustomersInfo getById(String encryptedId) throws Exception;

	boolean deleteById(Long id);

	CustomerUpdateRequest update(CustomerUpdateRequest customerUpdateDto) throws Exception;

	Page<ApplicantDto> getAllApplicants(SortingDTO sortingDTO);

	Long checkCustomerEligibility(String customerId) throws Exception;

}
