package com.narvee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narvee.entity.CurrentAddress;
import com.narvee.request.dto.AddressDto;

public interface AddressRepository extends JpaRepository<CurrentAddress, Long> {

	CurrentAddress save(AddressDto currentAddress);


}
