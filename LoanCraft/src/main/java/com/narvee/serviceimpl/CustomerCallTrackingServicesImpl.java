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
import com.narvee.Service.CustomerCallTrackingService;
import com.narvee.entity.CustomerCallTracking;
import com.narvee.entity.CustomerRelationshipMangement;
import com.narvee.repository.CustomerCallTrackingRepo;
import com.narvee.repository.CustomerRelationshipMangementRepo;
import com.narvee.request.dto.RequestDTO;

@Repository
public class CustomerCallTrackingServicesImpl implements CustomerCallTrackingService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerCallTrackingServicesImpl.class);

	@Autowired
	private CustomerCallTrackingRepo customerCallTrackingRepo;
	
	@Autowired
	private  CustomerRelationshipMangementRepo mangementRepo;

	@Override
	public CustomerCallTracking saveCctAll(CustomerCallTracking cct) {
		logger.info("!!! inside class : CustomerCallTrackingServicesImpl,!! method: saveCctAll()  ");
		Long crmId = cct.getCustomerRelationshipMangement().getCrmId();
        Optional<CustomerRelationshipMangement> crmOpt = mangementRepo.findById(crmId);

        if (crmOpt.isEmpty()) {
            throw new RuntimeException("Customer not found with ID: " + crmId);
        }

        // Set the actual managed entity
        cct.setCustomerRelationshipMangement(crmOpt.get());

        return customerCallTrackingRepo.save(cct);
    }
	

	@Override
	public List<CustomerCallTracking> getCctAll() {
		logger.info("!!! inside class : CustomerCallTrackingServicesImpl,!! method: getCctAll()  ");
		return customerCallTrackingRepo.findAll();
	}

	@Override
	public Page<CustomerCallTracking> findAllCrm(RequestDTO CctRequestDto) {
		logger.info("!!! inside class : CustomerCallTrackingServicesImpl,!! method: findAllCrm()  ");

		int pageNo = CctRequestDto.getPageNumber();
		int pageSize = CctRequestDto.getPageSize();
		String sortOrder = CctRequestDto.getSortOrder();
		String sortField = CctRequestDto.getSortField();
		String keyword = CctRequestDto.getKeyword();

		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

		if (sortField.equalsIgnoreCase("CREATEDATE")) {
			sortField = "createddate";
		} else if (sortField.equalsIgnoreCase("UPDATE")) {
			sortField = "updateddate";
		} else if (sortField.equalsIgnoreCase("END TIME")) {
			sortField = "end_time";
		} else if (sortField.equalsIgnoreCase("FEED BACK")) {
			sortField = "feed_back";
		} else if (sortField.equalsIgnoreCase("START TIME")) {
			sortField = "start_time";

		} else {
			sortField = "status";
		}
		Sort.Direction sortDirection = Sort.Direction.ASC;

		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
		}

		Sort sort = Sort.by(sortDirection, sortField);
		pageable = PageRequest.of(pageNo - 1, pageSize, sort);

		if (keyword.equalsIgnoreCase("empty")) {

			return customerCallTrackingRepo.findAll(pageable);
		} else {
			return customerCallTrackingRepo.getAllCctWithSearching(keyword, pageable);
		}

	}

	@Override
	public CustomerCallTracking updateCct(CustomerCallTracking callTracking) {
		return customerCallTrackingRepo.save(callTracking);
	}

	@Override
	public boolean deleteById(Long cCtid) {
		customerCallTrackingRepo.deleteById(cCtid);
		return true;
	}

	@Override
	public List<CustomerCallTracking> getById(Long crmId) {
		return customerCallTrackingRepo.findByCustomerRelationshipMangement_CrmId(crmId);
	}

}
