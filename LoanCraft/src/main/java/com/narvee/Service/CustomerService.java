package com.narvee.Service;

import org.springframework.data.domain.Page;
import com.narvee.entity.Customers;
import com.narvee.request.dto.ApplicantDto;
import com.narvee.request.dto.CustomerUpdateDto;
import com.narvee.request.dto.RegisterVO;
import com.narvee.request.dto.SortingDTO;

public interface CustomerService {

	Customers save(RegisterVO register);

	Customers getById(Long id);

	boolean deleteById(Long id);

	CustomerUpdateDto update(CustomerUpdateDto costomerUpdateDto);

	Page<ApplicantDto> getAllApplicants(SortingDTO sortingDTO);

}
