package com.narvee.Service;

import java.util.Map;

import com.narvee.entity.NBFC;

public interface NBFCService {

	public void save(NBFC nbfc);

	public void update(NBFC nbfc);

	public void delete(Long id);

	public Map<String, Object> getAllNBFC(SortingRequestDto requestDto);

	public NBFC getById(Long id);

	public boolean EmailDuplicateCheck(String email);

	public boolean NbfcDuplicateCheck(String nbfc);
	
	public boolean NbfcDuplicateCheckCorporateIdentificationNo(String identificationNo);

}
