package com.narvee.Service;

import java.util.List;

import com.narvee.entity.CurrentAddress;
import com.narvee.entity.PermanentAddress;
import com.narvee.request.dto.AddressDto;
import com.narvee.request.dto.AddressUpdateRequest;

public interface AddressServices {

	public AddressDto saveCurrentAddress(AddressDto addressDto) throws Exception;

	public AddressUpdateRequest updateAddress(AddressUpdateRequest updateRequest, String customerId) throws Exception;

	public CurrentAddress getByID(Long cAddressId);

	public List<CurrentAddress> getAll();

	public boolean deltedById(Long cAddressId);

	public AddressDto savePermanentAddress(AddressDto addressDto);

	public PermanentAddress getBypermanentId(Long pAddressId);
}
