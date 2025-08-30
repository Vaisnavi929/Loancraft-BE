package com.narvee.serviceimpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.narvee.Service.CustomerCallTrackingService;
import com.narvee.entity.CustomerCallTracking;
import com.narvee.entity.ReferenceInfo;
import com.narvee.repository.CustomerCallTrackingRepo;
import com.narvee.repository.ReferenceRepository;
import com.narvee.request.dto.ReferenceRequestDto;
import com.narvee.request.dto.RequestDTO;
import com.narvee.request.dto.callRequestDTO;
import com.narvee.vo.CctResponseDTO;
import com.narvee.vo.ReferenceDTO;

@Service

public class CustomerCallTrackingServicesImpl implements CustomerCallTrackingService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerCallTrackingServicesImpl.class);

	@Autowired
	private CustomerCallTrackingRepo customerCallTrackingRepo;

	@Autowired
	private ReferenceRepository referenceRepository;

	@Override
	public Object saveCct(callRequestDTO callRequestDTO) {
	    logger.info("!!! inside class : CustomerRelationshipMangementImpl,!! method:saveCct()");

	    CustomerCallTracking call = new CustomerCallTracking();
	    
	    call.setCustomerName(callRequestDTO.getCustomerName());
	    call.setAgentName(callRequestDTO.getAgentName());
	    call.setDate(callRequestDTO.getDate());
	    call.setLoanAccountNumber(callRequestDTO.getLoanAccountNumber());
	    call.setStatus(callRequestDTO.getStatus());
        call.setFeedBack(callRequestDTO.getFeedBack());

	    if ("ANSWERED".equalsIgnoreCase(callRequestDTO.getStatus())) {      
	        return customerCallTrackingRepo.save(call);

	    } else if ("NOT ANSWERED".equalsIgnoreCase(callRequestDTO.getStatus())) {
	        List<ReferenceInfo> refs = new ArrayList<>();

	        for (ReferenceRequestDto refDto : callRequestDTO.getReferenceInfoDto()) {
	            ReferenceInfo ref = new ReferenceInfo();
	            ref.setReferenceName(refDto.getReferenceName());
	            ref.setMobileNumber(refDto.getMobileNumber());
	            ref.setReferenceFeedBack(refDto.getReferenceFeedBack());
	            ref.setReferenceStatus(refDto.getReferenceStatus());
	            ref.setCustomerCall(call); // set parent
	            refs.add(ref);
	        }
	        call.setReferences(refs);
	        return customerCallTrackingRepo.save(call);

	    } else {
	        throw new IllegalArgumentException("Invalid status value: " + callRequestDTO.getStatus());
	    }
	}


	@Override
	public CustomerCallTracking updateCct(callRequestDTO callRequestDTO) {
	    logger.info("!!! inside class : CustomerRelationshipMangementImpl,!! method: updateCct() ");

	    // 1. Load existing entity
	    CustomerCallTracking existing = customerCallTrackingRepo.findById(callRequestDTO.getCctId())
	            .orElseThrow(() -> new RuntimeException("CustomerCallTracking not found with id: " + callRequestDTO.getCctId()));

	    // 2. Update simple fields
	    existing.setCustomerName(callRequestDTO.getCustomerName());
	    existing.setAgentName(callRequestDTO.getAgentName());
	    existing.setDate(callRequestDTO.getDate());
	    existing.setLoanAccountNumber(callRequestDTO.getLoanAccountNumber());
	    existing.setStatus(callRequestDTO.getStatus());
	    existing.setFeedBack(callRequestDTO.getFeedBack());

	    // 3. Handle references
	    if ("ANSWERED".equalsIgnoreCase(callRequestDTO.getStatus())) {
	        // if answered → clear old references
	        existing.getReferences().clear();
	    } 
	    else if ("NOT ANSWERED".equalsIgnoreCase(callRequestDTO.getStatus())) {
	        existing.getReferences().clear(); // clear old references

	        if (callRequestDTO.getReferenceInfoDto() != null) {
	            for (ReferenceRequestDto refDto : callRequestDTO.getReferenceInfoDto()) {
	                ReferenceInfo ref = new ReferenceInfo();

	                // if refId exists → set it (update existing row)
	                if (refDto.getReferenceId() != null) {
	                    ref.setReferenceId(refDto.getReferenceId());
	                }

	                ref.setReferenceName(refDto.getReferenceName());
	                ref.setMobileNumber(refDto.getMobileNumber());
	                ref.setReferenceFeedBack(refDto.getReferenceFeedBack());
	                ref.setReferenceStatus(refDto.getReferenceStatus());
	                ref.setCustomerCall(existing); // back-reference
	                existing.getReferences().add(ref);
	            }
	        }
	    } 
	    else {
	        throw new IllegalArgumentException("Invalid status value: " + callRequestDTO.getStatus());
	    }

	    // 4. Save
	    return customerCallTrackingRepo.save(existing);
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
	public Page<CctResponseDTO> getAllCallTracking(RequestDTO requestDTO) {
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
		} else if (sortField.equalsIgnoreCase("LoanAccountNumber")) {
			sortField = "loan_account_number";
		} else {
			sortField = "date";
		}
		
		Sort.Direction sortDirection = Sort.Direction.ASC;
		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
		}

		Sort sort = Sort.by(sortDirection, sortField);
		pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		
		
		Page<Object[]> pageResult=null;
		
	    if ("empty".equalsIgnoreCase(keyword)) {
	    	pageResult = customerCallTrackingRepo.getcctinfo(pageable);
	    } else {
	    	pageResult = customerCallTrackingRepo.getAllCctWithSearching(keyword, pageable);
	    }
	    
	    
	    System.err.println(pageResult);
	    
	     
		 // Map of parentId -> DTO
        Map<Long, CctResponseDTO> map = new LinkedHashMap<>();

        for (Object[] row : pageResult.getContent()) {
        	 Long cctId = Long.valueOf(row[0].toString());
//            Long cctId = ((Long) row[0]).longValue();

            // Create parent if not exists
            CctResponseDTO parent = map.get(cctId);
            if (parent == null) {
                parent = new CctResponseDTO();
                parent.setCctId(cctId);
                
                if (row[1] != null) {
                    if (row[1] instanceof java.sql.Timestamp) {
                        parent.setCreateddate(((java.sql.Timestamp) row[1]).toLocalDateTime().toLocalDate());
                    } else if (row[1] instanceof java.sql.Date) {
                        parent.setCreateddate(((java.sql.Date) row[1]).toLocalDate());
                    }
                }

                if (row[2] != null) {
                    if (row[2] instanceof java.sql.Timestamp) {
                        parent.setUpdateddate(((java.sql.Timestamp) row[2]).toLocalDateTime().toLocalDate());
                    } else if (row[2] instanceof java.sql.Date) {
                        parent.setUpdateddate(((java.sql.Date) row[2]).toLocalDate());
                    }
                }

     
                parent.setCustomerName((String) row[3]);
                parent.setAgentName((String) row[4]);
                parent.setFeedBack((String) row[5]);
                parent.setLoanAccountNumber((String)row[6]);
                if (row[7] instanceof java.sql.Date) {
                    parent.setDate(((java.sql.Date) row[7]).toLocalDate());
                }
                parent.setStatus((String) row[8]);            
                parent.setReferences(new ArrayList<>());
                map.put(cctId, parent);
            }
System.err.println(map);
            // Add child        
            ReferenceDTO ref = new ReferenceDTO();

         ref.setReferenceId(row[9] != null ? ((Number) row[9]).longValue() : null);
         ref.setReferenceName((String) row[10]);
         ref.setMobileNumber((String) row[11]);
         ref.setReferenceFeedBack((String) row[12]);
         ref.setReferenceStatus((String) row[13]);

         if (row[9] != null || row[10] != null || row[11] != null || row[12] != null || row[13] != null) {
             parent.getReferences().add(ref);
         }

        }

        List<CctResponseDTO> finalList = new ArrayList<>(map.values());

        return new PageImpl<>(finalList, pageable, pageResult.getTotalElements());
    }
	  

	}



