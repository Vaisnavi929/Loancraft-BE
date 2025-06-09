package com.narvee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narvee.entity.CheckBounceEntry;
import com.narvee.request.dto.CheckBounceDTO;

public interface CheckBounceRepository extends JpaRepository<CheckBounceEntry, Long> {


}
