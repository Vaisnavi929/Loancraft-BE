package com.narvee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narvee.entity.CustomerDetails;



public interface CustomerRepository extends JpaRepository<CustomerDetails, Long>{

}
