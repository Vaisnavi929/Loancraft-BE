package com.narvee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narvee.entity.CurrentAddress;

public interface AddressRepository extends JpaRepository<CurrentAddress, Long> {



}
