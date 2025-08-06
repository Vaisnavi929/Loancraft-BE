package com.narvee.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.narvee.Service.CustomerRelationshipMangementService;
import com.narvee.entity.CustomerRelationshipMangement;
import com.narvee.repository.CustomerRelationshipMangementRepo;
import com.narvee.request.dto.RequestDTO;

@Service
public class CustomerRelationshipMangementImpl implements CustomerRelationshipMangementService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerRelationshipMangementImpl.class);

	@Autowired
	private CustomerRelationshipMangementRepo customerRelationshipMangementRepo;

	@Override
	public CustomerRelationshipMangement saveCrm(CustomerRelationshipMangement crm) {
		logger.info("!!! inside class : CustomerRelationshipMangementImpl,!! method: saveCrm()  ");
		return customerRelationshipMangementRepo.save(crm);
	}

//	@Override
//	public List<CustomerRelationshipMangement> getCrm() {
//		logger.info("!!! inside class : CustomerRelationshipMangementImpl,!! method: getcrm()  ");
//		return customerRelationshipMangementRepo.findAll();
//	}

	@Override
	public boolean existsByEmail(String email) {

		return customerRelationshipMangementRepo.existsByEmailIgnoreCase(email);
	}

	@Override
	public boolean existsByMoblieNumber(String mobileNumber) {

		return customerRelationshipMangementRepo.existsByMobileNumber(mobileNumber);
	}

	@Override
	public Page<CustomerRelationshipMangement> findAllCrm(RequestDTO CrmRequestDto) {

		int pageNo = CrmRequestDto.getPageNumber();
		int pageSize = CrmRequestDto.getPageSize();
		String sortOrder = CrmRequestDto.getSortOrder();
		String sortField = CrmRequestDto.getSortField();
		String keyword = CrmRequestDto.getKeyword();
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		
		if (sortField.equalsIgnoreCase("customerName")) {
			sortField = "customer_name";
		} else if (sortField.equalsIgnoreCase("email")) {
			sortField = "email";
		} else if (sortField.equalsIgnoreCase("mobileNumber")) {
			sortField = "mobile_number";
		} else {
			sortField = "crm_id";
		}
		
		Sort.Direction sortDirection = Sort.Direction.ASC;

		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
	}

		Sort sort = Sort.by(sortDirection, sortField);
	pageable = PageRequest.of(pageNo - 1, pageSize, sort);
	      
		if (keyword.equalsIgnoreCase("empty")) {
			return customerRelationshipMangementRepo.getcrminfo(pageable);
		} else {
			return customerRelationshipMangementRepo.getAllCrmWithSearching(keyword, pageable);
		}

	}

	@Override
	public CustomerRelationshipMangement update(CustomerRelationshipMangement crm) {

		return customerRelationshipMangementRepo.save(crm);
	}

	@Override
	public boolean deleteById(Long crmId) {
		customerRelationshipMangementRepo.deleteById(crmId);
       return true;
	}

	@Override
	public Optional<CustomerRelationshipMangement> getById(Long crmId) {
		return customerRelationshipMangementRepo.findById(crmId);
		
	}

}
