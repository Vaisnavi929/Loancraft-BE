package com.narvee.Service;

import java.util.List;

import com.narvee.entity.CurrentAddress;
import com.narvee.entity.PermanentAddress;
import com.narvee.request.dto.AddressDto;

public interface AddressServices {
	
	public CurrentAddress saveCurrentAddress(AddressDto addressDto);
	public CurrentAddress updateCurrentAddress(AddressDto currentAddress);
	public CurrentAddress getByID(Long cAddressId);
	public List<CurrentAddress> getAll();
	public boolean deltedById(Long cAddressId);
	
	public PermanentAddress savePermanentAddress(AddressDto addressDto);
	public PermanentAddress getBypermanentId(Long pAddressId);
}
