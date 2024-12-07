package com.narvee.Service;

import java.util.List;

import com.narvee.request.dto.ApplicantDto;

public interface ApplicantService {

	ApplicantDto save(ApplicantDto applicantDto);

    ApplicantDto getById(Long id);

    List<ApplicantDto> getAllApplicants();

    boolean deleteById(Long id);

    ApplicantDto update(Long id, ApplicantDto updatedApplicantDto); 

}
