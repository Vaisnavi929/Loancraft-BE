package com.narvee.Service;

import org.springframework.data.domain.Page;

import com.narvee.entity.Customers;
import com.narvee.request.dto.ApplicantDto;
import com.narvee.request.dto.RegisterVO;
import com.narvee.request.dto.SortingDTO;

public interface CustomerService {

	ApplicantDto save(RegisterVO register);

    Customers getById(Long id);

    boolean deleteById(Long id);

    ApplicantDto update(Long id, ApplicantDto updatedApplicantDto);

	Page<ApplicantDto> getAllApplicants(SortingDTO sortingDTO); 

}
