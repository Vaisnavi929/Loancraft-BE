package com.narvee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.narvee.entity.CustomerCallTracking;

@Repository
public interface CustomerCallTrackingRepo extends JpaRepository<CustomerCallTracking, Long> {

}
