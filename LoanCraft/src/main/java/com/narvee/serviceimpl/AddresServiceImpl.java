package com.narvee.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.Service.AddressServices;
import com.narvee.entity.CurrentAddress;
import com.narvee.entity.Customers;
import com.narvee.entity.PermanentAddress;
import com.narvee.repository.AddressRepository;
import com.narvee.repository.PermanentAddressRepository;
import com.narvee.request.dto.AddressDto;

@Service
public class AddresServiceImpl implements AddressServices {
	
	public static final Logger logger=LoggerFactory.getLogger(AddresServiceImpl.class);
	
	@Autowired
	private AddressRepository currentAddressRepository;
	@Autowired
	ModelMapper mapper;
	
	@Autowired
	private PermanentAddressRepository permanentAddressRepository;
	
	@Override
	public CurrentAddress saveCurrentAddress(AddressDto addressDto) {
		CurrentAddress currentAddress = mapper.map(addressDto,CurrentAddress.class);
		Customers customer=new Customers();
		customer.setCustomerId(addressDto.getCustomerId());
		return currentAddressRepository.save(currentAddress);
	}

	@Override
	public CurrentAddress updateCurrentAddress(AddressDto currentAddress) {
		
		return currentAddressRepository.save(currentAddress);
	}

	@Override
	public CurrentAddress getByID(Long cAddressId) {
		
		return currentAddressRepository.findById(cAddressId).get();
	}

	@Override
	public List<CurrentAddress> getAll() {
	
		return currentAddressRepository.findAll();
	}

	@Override
	public boolean deltedById(Long cAddressId) {
		currentAddressRepository.deleteById(cAddressId);
		return true;
	}

	@Override
	public PermanentAddress savePermanentAddress(AddressDto addressDto) {
		PermanentAddress permanentAddress = mapper.map(addressDto,PermanentAddress.class);
		Customers customer=new Customers();
		customer.setCustomerId(addressDto.getCustomerId());
		permanentAddress.setCustomer(customer);
		
		
		return permanentAddressRepository.save(permanentAddress);
	}

	@Override
	public PermanentAddress getBypermanentId(Long pAddressId) {
		
		return permanentAddressRepository.findById(pAddressId).get();
	}
		
}
