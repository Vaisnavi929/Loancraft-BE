package com.narvee.serviceimpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.narvee.Service.NBFCService;
import com.narvee.Service.SortingRequestDto;
import com.narvee.entity.NBFC;
import com.narvee.repository.NBFCRepository;

@Service
public class NBFCServiceImpl implements NBFCService {
	private static final Logger logger = LoggerFactory.getLogger(AccountSequenceServiceImpl.class);

	@Autowired
	private NBFCRepository nbfcRepository;

	@Override
	public void save(NBFC nbfc) {
		logger.info("!!! inside class: NBFCServiceImpl, method: save");
		nbfcRepository.save(nbfc);

	}

	@Override
	public void update(NBFC nbfc) {
		logger.info("!!! inside class: NBFCServiceImpl, method: update");
		nbfcRepository.save(nbfc);

	}

	@Override
	public void delete(Long id) {
		logger.info("!!! inside class: NBFCServiceImpl, method: delete");
		nbfcRepository.deleteById(id);
	}

	@Override
	public NBFC getById(Long id) {
		logger.info("!!! inside class: NBFCServiceImpl, method: getById");
		return nbfcRepository.findById(id).get();
	}

	@Override
	public boolean EmailDuplicateCheck(String email) {
		logger.info("!!! inside class: NBFCServiceImpl, method: EmailDuplicateCheck");
		return nbfcRepository.existsByEmailIgnoreCase(email);
	}

	@Override
	public boolean NbfcDuplicateCheck(String nbfc) {
		logger.info("!!! inside class: NBFCServiceImpl, method: NbfcDuplicateCheck");
		return nbfcRepository.existsByNbfcNameIgnoreCase(nbfc);
	}

	@Override
	public Map<String, Object> getAllNBFC(SortingRequestDto requestDto) {
		String sortField = requestDto.getSortField();
		String sortOrder = requestDto.getSortOrder();
		String keyword = requestDto.getKeyword();
		Integer pageNumber = requestDto.getPageNumber();
		Integer pageSize = requestDto.getPageSize();

		if (sortField.equalsIgnoreCase("nbfcName")) {
			sortField = "nbfc_name";
		} else if (sortField.equalsIgnoreCase("regionalOffice")) {
			sortField = "regional_office";
		} else if (sortField.equalsIgnoreCase("classification")) {
			sortField = "classification";
		} else if (sortField.equalsIgnoreCase("corporateIdentificationNo")) {
			sortField = "corporate_identification_no";
		} else if (sortField.equalsIgnoreCase("layer")) {
			sortField = "layer";
		} else if (sortField.equalsIgnoreCase("address")) {
			sortField = "address";
		} else if (sortField.equalsIgnoreCase("email")) {
			sortField = "email";
		} else {
			sortField = "updateddate";
		}

		Sort.Direction sortDirection = Sort.Direction.ASC;
		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
		}

		Sort sort = Sort.by(sortDirection, sortField);
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

		List<Map<String, Object>> nbfcs = new ArrayList<Map<String, Object>>();
		Map<String, Object> response = new LinkedHashMap<String, Object>();

		Page<Object[]> nbfclist = null;
		
		if (keyword.equalsIgnoreCase("empty")) {
			nbfclist = nbfcRepository.getAllNBFC(pageable);
			
			
			for (Object[] object : nbfclist) {
				logger.info("Inside NBFCServiceImpl: getAllNBFC ,Empty ");
				Map<String, Object> nbfc = new LinkedHashMap<String, Object>();
				nbfc.put("id", object[0]);
				nbfc.put("nbfcName", object[1]);
				nbfc.put("regionalOffice", object[2]);
				nbfc.put("corporateIdentificationNo", object[4]);
				nbfc.put("classification", object[3]);
				nbfc.put("layer", object[5]);
				nbfc.put("address", object[6]);
				nbfc.put("email", object[7]);
				nbfc.put("updateddate", object[8]);
				nbfc.put("products" , object[9]);
	     		nbfc.put("partners", object[10]);
	     		nbfc.put("typeOfOrganization", object[11]);
				nbfcs.add(nbfc);

			}

		} else {
			logger.info("Inside NBFCServiceImpl: getAllNBFC ,Keyword ");
			nbfclist = nbfcRepository.searchNBFC(keyword, pageable);
			for (Object[] object : nbfclist) {
				Map<String, Object> nbfc = new LinkedHashMap<String, Object>();
				nbfc.put("id", object[0]);
				nbfc.put("nbfcName", object[1]);
				nbfc.put("regionalOffice", object[2]);
				nbfc.put("corporateIdentificationNo", object[3]);
				nbfc.put("classification", object[4]);
				nbfc.put("layer", object[5]);
				nbfc.put("address", object[6]);
				nbfc.put("email", object[7]);
				nbfc.put("updateddate", object[8]);
				nbfcs.add(nbfc);
			}
		}
		response.put("currentPage", nbfclist.getNumber());
		response.put("totalItems", nbfclist.getTotalElements());
		response.put("totalPages", nbfclist.getTotalPages());
		response.put("pageSize", nbfclist.getSize());
		response.put("isLast", nbfclist.isLast());
		response.put("content", nbfcs);
		return response;
	}

	@Override
	public boolean NbfcDuplicateCheckCorporateIdentificationNo(String identificationNo) {
		logger.info("!!! inside class: NBFCServiceImpl, method: NbfcDuplicateCheckCorporateIdentificationNo");
		return nbfcRepository.existsByCorporateIdentificationNoIgnoreCase(identificationNo);
	}

}
