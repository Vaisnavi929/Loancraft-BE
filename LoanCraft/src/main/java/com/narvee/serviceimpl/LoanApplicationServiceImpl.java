package com.narvee.serviceimpl;

import java.util.List;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.narvee.Service.ApplicationsService;
import com.narvee.Service.SortingRequestDto;
import com.narvee.config.MapperConfig;
import com.narvee.entity.Applications;
import com.narvee.repository.ApplicationsRepository;
import com.narvee.request.dto.LoanApplicationDTOS;
import com.narvee.request.dto.LoanApplicationDto;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LoanApplicationServiceImpl implements ApplicationsService {

	public static final Logger logger = LoggerFactory.getLogger(CustomerS.class);

	@Autowired
	private ApplicationsRepository applicationRepository;

	private static final int DIGIT_PADDING = 4;

	@Override
	public Applications saveLoanApplication(LoanApplicationDto loanApplicationDto) {
	    logger.info("Inside saveLoanApplication() method");

	    ModelMapper modelMapper = MapperConfig.getModelMapper();  
	    modelMapper.addConverter(new Converter<String, Long>() {
	        public Long convert(MappingContext<String, Long> context) {
	            try {
	                return context.getSource() != null ? Long.parseLong(context.getSource()) : null;
	            } catch (NumberFormatException e) {
	                return null;  
	            }
	        }
	    });

	   
	   Applications loan = modelMapper.map(loanApplicationDto, Applications.class);

	    
	    Long amaxnumber = applicationRepository.appmaxNumber();
	    if (amaxnumber == null) {
	        amaxnumber = 0L; 	    }

	  
	    String valueWithPadding = String.format("%0" + DIGIT_PADDING + "d", amaxnumber + 1);
	    String value = "BEACON" + valueWithPadding;
	    loan.setApplicationReferenceId(value);
//	    loan.setAmaxnum(amaxnumber + 1);

	    return applicationRepository.save(loan); 
	}


	@Override
	public Applications getById(Long loanId) {
		logger.info("!!! inside class: LoanApplicationServiceImpl , !! method: getById()");
		return applicationRepository.findById(loanId).get();
	}

	@Override
	public List<Applications> getAll() {
		logger.info("!!! inside class: LoanApplicationServiceImpl , !! method: getAll()");
		return applicationRepository.findAll();
	}

	@Override
	public boolean deltedById(Long loanId) {
		logger.info("!!! inside class: LoanApplicationServiceImpl , !! method: deltedById()");
		applicationRepository.deleteById(loanId);
		return true;
	}

	@Override
	public Applications updateLoanApplication(LoanApplicationDto loanApplication) {
		logger.info("!!! inside class: LoanApplicationserviceimpl , !! method: updateLoanApplication");
		ModelMapper mapper = MapperConfig.getModelMapper();
		Applications loan = mapper.map(loanApplication, Applications.class);
		return applicationRepository.save(loan);
	}

	@Override
	public boolean existsByAadharNumber(String aadharNumber) {
		logger.info("!!! inside class: LoanApplicationserviceimpl , !! method: updateLoanApplication");
		return false; //loanApplicationRepository.existsByAadharNumber(aadharNumber);
	}

	@Override
	public Page<LoanApplicationDTOS> getAllLoanApplicationPagination(SortingRequestDto sortingRequestDto) {
		String sortField = sortingRequestDto.getSortField();
		String sortOrder = sortingRequestDto.getSortOrder();
		String keyword = sortingRequestDto.getKeyword();
		Integer pageNumber = sortingRequestDto.getPageNumber();
		Integer pageSize = sortingRequestDto.getPageSize();
		if (sortField.equalsIgnoreCase("fullname"))
			sortField = "fullname";
		else if (sortField.equalsIgnoreCase("mail_id"))
			sortField = "mail_id";
		else if (sortField.equalsIgnoreCase("mobile_number"))
			sortField = "mobile_number";
		else if (sortField.equalsIgnoreCase("application_reference_id"))
			sortField = "application_reference_id";
		else if (sortField.equalsIgnoreCase("date_of_birth"))
			sortField = "date_of_birth";
		else
			sortField = "status";
		Sort.Direction sortDirection = Sort.Direction.ASC;
		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
		}

		Sort sort = Sort.by(sortDirection, sortField);
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
		if (keyword.equalsIgnoreCase("empty")) {
			return applicationRepository.getAllLoanApplication(pageable);
		} else {

			return applicationRepository.getAllLoanApplicationWithKeyword(pageable, keyword);

		}

	}
}
