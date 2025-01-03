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
import com.narvee.entity.States;
import com.narvee.repository.AddressRepository;
import com.narvee.repository.CustomersRepository;
import com.narvee.repository.PermanentAddressRepository;
import com.narvee.repository.StateCodeRepository;
import com.narvee.request.dto.AddressDto;
import com.narvee.request.dto.AddressUpdateRequest;
import com.narvee.util.EncryptionUtil;

@Service
public class AddresServiceImpl implements AddressServices {

	public static final Logger logger = LoggerFactory.getLogger(AddresServiceImpl.class);

	@Autowired
	private AddressRepository currentAddressRepository;

	@Autowired
	private CustomersRepository customerRepository;

	@Autowired
	private StateCodeRepository stateRepository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private PermanentAddressRepository permanentAddressRepository;

	@Override
	public AddressDto saveCurrentAddress(AddressDto addressDto) throws Exception {
		logger.info("!!! inside class: AddressServiceImpl, method: saveCurrentAddress()");

		// Decrypt customerId before saving
		long decryptedCustomerId = 2; // Long.parseLong(addressDto.getCustomerId());
		logger.info("Decrypted customerId: {}", decryptedCustomerId);

		// Fetch the customer by decrypted customerId
		Customers customer = customerRepository.findById(decryptedCustomerId)
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		// Map AddressDto to Address entity
		CurrentAddress address = mapper.map(addressDto, CurrentAddress.class);

		// Set the customer reference in the address
		address.setCustomer(customer);

		// Save the address
		CurrentAddress savedAddress = currentAddressRepository.save(address);

		// Map saved Address back to AddressDto
		AddressDto savedAddressDto = mapper.map(savedAddress, AddressDto.class);

		return savedAddressDto;
	}

	public AddressUpdateRequest updateAddress(AddressUpdateRequest updateRequest, String customerId) throws Exception {
		logger.info("!!! inside class: AddressServiceImpl, method: savePermanentAddress");
		CurrentAddress currentAddress = mapper.map(updateRequest.getCurrentAddress(), CurrentAddress.class);
		PermanentAddress permanentAddress = mapper.map(updateRequest.getPermanentAddress(), PermanentAddress.class);
		Long customreId = EncryptionUtil.decrypt(customerId);
		logger.info("!!! inside class: AddressServiceImpl, method: savePermanentAddress customreId {}", customreId);

		Customers customers = new Customers();
		customers.setCustomerId(customreId);

		States permanentState = stateRepository.findById(updateRequest.getPermanentAddress().getStateId()).get(); 
		permanentAddress.setState(permanentState);

		States currentState = stateRepository.findById(updateRequest.getCurrentAddress().getStateId()).get();
		currentAddress.setState(currentState);

		currentAddress.setCustomer(customers);
		currentAddress.setState(currentState);

		permanentAddress.setCustomer(customers);
		permanentAddress.setState(permanentState);

		permanentAddressRepository.save(permanentAddress);
		currentAddressRepository.save(currentAddress);

		return updateRequest;

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
	public AddressDto savePermanentAddress(AddressDto addressDto) {
		logger.info("!!! inside class: AddressServiceImpl, method: savePermanentAddress()");
		// Decrypt customerId before saving
		long decryptedCustomerId = 2;/// Long.parseLong(addressDto.getCustomerId());
		logger.info("Decrypted customerId: {}", decryptedCustomerId);

		// Fetch the customer by decrypted customerId
		Customers customer = customerRepository.findById(decryptedCustomerId)
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		// Map AddressDto to Address entity
		PermanentAddress address = mapper.map(addressDto, PermanentAddress.class);

		// Set the customer reference in the address
		address.setCustomer(customer);

		// Save the address
		PermanentAddress savedAddress = permanentAddressRepository.save(address);

		// Map saved Address back to AddressDto
		AddressDto savedAddressDto = mapper.map(savedAddress, AddressDto.class);

		return savedAddressDto;
	}

	@Override
	public PermanentAddress getBypermanentId(Long pAddressId) {
		logger.info("!!! inside class: AddressServiceImpl, method: savePermanentAddress");
		return permanentAddressRepository.findById(pAddressId).get();
	}

}
