package com.narvee.Service;

import org.springframework.data.domain.Page;

import com.narvee.request.dto.ApplicantDto;
import com.narvee.request.dto.SortingDTO;

public interface ApplicantService {

	ApplicantDto save(ApplicantDto applicantDto);

    ApplicantDto getById(Long id);

    boolean deleteById(Long id);

    ApplicantDto update(Long id, ApplicantDto updatedApplicantDto);

	Page<ApplicantDto> getAllApplicants(SortingDTO sortingDTO); 

}
