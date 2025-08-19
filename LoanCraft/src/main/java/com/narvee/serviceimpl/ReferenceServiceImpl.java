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

import com.narvee.Service.ReferenceService;
import com.narvee.entity.CustomerCallTracking;
import com.narvee.entity.ReferenceInfo;
import com.narvee.repository.CustomerCallTrackingRepo;
import com.narvee.repository.ReferenceRepository;
import com.narvee.request.dto.ReferenceRequestDto;
import com.narvee.request.dto.RequestDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReferenceServiceImpl implements ReferenceService {
	private static final Logger logger = LoggerFactory.getLogger(ReferenceServiceImpl.class);
	
@Autowired
private ReferenceRepository referenceRepository;

@Autowired
private CustomerCallTrackingRepo callTrackingRepo;

	
	@Override
	public ReferenceInfo saveRi(ReferenceRequestDto referenceRequestDto) {
		logger.info("!!! inside class :ReferenceServiceImpl , !! method :saveRi() ");
		
		CustomerCallTracking call = callTrackingRepo.findById(referenceRequestDto.getCctId()).orElseThrow(() -> new RuntimeException("customerCallTracing not"));
		
		ReferenceInfo reference = new ReferenceInfo();
        reference.setReferenceName(referenceRequestDto.getReferenceName());
        reference.setMobileNumber(referenceRequestDto.getMobileNumber());
        reference.setReferenceFeedBack(referenceRequestDto.getReferenceFeedBack());
        reference.setReferenceStatus(referenceRequestDto.getReferenceStatus());	
        reference.setCustomerCallTracking(call);
		return 	referenceRepository.save(reference);
	}

	@Override
	public ReferenceInfo updateRi(ReferenceInfo referenceInfo) {
		
		CustomerCallTracking callTracking=callTrackingRepo.findById(referenceInfo.getCustomerCallTracking().getCctId()).get();
		referenceInfo.setCustomerCallTracking(callTracking);
		
		return referenceRepository.save(referenceInfo);
	}

	@Override
	public Optional<ReferenceInfo> getByIdRi(Long referenceId) {
		
		return referenceRepository.findById(referenceId);
	}

	@Override
	public List<ReferenceInfo> getAllRi() {

		return referenceRepository.findAll();
	}

	@Override
	public Page<ReferenceInfo> paginationReferenceInfo(RequestDTO requestDTO) {
		
			int pageNo = requestDTO.getPageNumber();
			int pageSize = requestDTO.getPageSize();
			String sortOrder = requestDTO.getSortOrder();
			String sortField = requestDTO.getSortField();
			String keyword = requestDTO.getKeyword();
			
			Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
			
			if (sortField.equalsIgnoreCase("createddate")) {
				sortField = "createddate";
			} else if (sortField.equalsIgnoreCase("updateddate")) {
				sortField = "updateddate";
			} else if (sortField.equalsIgnoreCase("referenceId")) {
				sortField = "reference_id";
			} else if (sortField.equalsIgnoreCase("moblieNumber")) {
				sortField = "mobile_number";
			} else if (sortField.equalsIgnoreCase("referenceFeedBack")) {
				sortField = "reference_feed_back";
			} else if (sortField.equalsIgnoreCase("referenceName")) {
				sortField = "reference_name";
			} else if (sortField.equalsIgnoreCase("referenceStatus")) {
				sortField = "reference_status";
			} else {
				sortField = "cct_id";
			}
			Sort.Direction sortDirection = Sort.Direction.ASC;

			if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
				sortDirection = Sort.Direction.DESC;
			}
			
			Sort sort = Sort.by(sortDirection, sortField);
			pageable = PageRequest.of(pageNo - 1, pageSize, sort);

			if (keyword.equalsIgnoreCase("empty")) {

				return referenceRepository.getReferenceInfo(pageable);
			} else {
				return referenceRepository.getAllReferenceWithSearching(keyword, pageable);
			}

		}

		
	}


