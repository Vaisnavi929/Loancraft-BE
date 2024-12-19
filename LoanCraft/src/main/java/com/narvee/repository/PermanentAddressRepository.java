package com.narvee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narvee.entity.PermanentAddress;

public interface PermanentAddressRepository extends JpaRepository<PermanentAddress, Long> {

}
