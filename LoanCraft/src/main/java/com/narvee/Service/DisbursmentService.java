package com.narvee.Service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.narvee.entity.Disbursment;
import com.narvee.request.dto.DisbursmentDTO;
import com.narvee.request.dto.DisbursmentResponseDTO;

public interface DisbursmentService {
	
	public Disbursment saveDisbursment(DisbursmentDTO disbursmentDTO);
	public Disbursment updateDisbursment(DisbursmentDTO disbursmentDTO);
	public Disbursment getById(Long disbursmentId);
	public List<Disbursment> getAll();
	public Boolean deleteById(Long disbursmentId);
	public Page<DisbursmentResponseDTO> getAllDisbursmentWithPagination(SortingRequestDto sortingRequestDto);

}
