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
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.narvee.Service.CustomerCallTrackingService;
import com.narvee.entity.CustomerCallTracking;
import com.narvee.entity.CustomerRelationshipMangement;
import com.narvee.entity.ReferenceInfo;
import com.narvee.repository.CustomerCallTrackingRepo;
import com.narvee.repository.CustomerRelationshipMangementRepo;
import com.narvee.repository.ReferenceRepository;
import com.narvee.request.dto.CctResponsesDTO;
import com.narvee.request.dto.CrmRquestDto;
import com.narvee.request.dto.RequestDTO;

@Service
public class CustomerCallTrackingServicesImpl implements CustomerCallTrackingService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerCallTrackingServicesImpl.class);

	@Autowired
	private CustomerCallTrackingRepo customerCallTrackingRepo;
	
	@Autowired
	private  ReferenceRepository  referenceRepository;

	@Override
	public CustomerCallTracking saveCct(CustomerCallTracking callTracking) {
		logger.info("!!! inside class : CustomerRelationshipMangementImpl,!! method:saveCct()  ");
		return customerCallTrackingRepo.save(callTracking);
	} 

	@Override
	public CustomerCallTracking updateCct(CustomerCallTracking callTracking) {
		logger.info("!!! inside class : CustomerRelationshipMangementImpl,!! method: updateCct()  ");
		return customerCallTrackingRepo.save(callTracking);
	}

	@Override
	public Optional<CustomerCallTracking> getById(Long cctId) {
		logger.info("!!! inside class : CustomerRelationshipMangementImpl,!! method: getById()  ");
		return customerCallTrackingRepo.findById(cctId);
	}

	@Override
	public List<CustomerCallTracking> getAll() {
		logger.info("!!! inside class : CustomerRelationshipMangementImpl,!! method: getAll()");
		return customerCallTrackingRepo.findAll();
	}

	@Override
	public Page<CctResponsesDTO> getAllCallTracking(RequestDTO requestDTO) {
		logger.info("!!! inside class : CustomerRelationshipMangementImpl,!! method: paginationCct()");

		int pageNo = requestDTO.getPageNumber();
		int pageSize = requestDTO.getPageSize();
		String sortOrder = requestDTO.getSortOrder();
		String sortField = requestDTO.getSortField();
		String keyword = requestDTO.getKeyword();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		
		if (sortField.equalsIgnoreCase("cctId")) {
			sortField = "cct_id";
		} else if (sortField.equalsIgnoreCase("agentName")) {
			sortField = "agent_name";
		} else if (sortField.equalsIgnoreCase("customerName")) {
			sortField = "customer_name";
		} else if (sortField.equalsIgnoreCase("feedBACK")) {
			sortField = "feed_back";
		} else if (sortField.equalsIgnoreCase("status")) {
			sortField = "status";
		}  {
			sortField = "date";
		}
		Sort.Direction sortDirection = Sort.Direction.ASC;

		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
		}
		
		Sort sort = Sort.by(sortDirection, sortField);
		pageable = PageRequest.of(pageNo - 1, pageSize, sort);

		if (keyword.equalsIgnoreCase("empty")) {

			return customerCallTrackingRepo.getcctinfo(pageable);
		} else {
			return customerCallTrackingRepo.getAllCctWithSearching(keyword, pageable);
		}

		
	}

	
}
