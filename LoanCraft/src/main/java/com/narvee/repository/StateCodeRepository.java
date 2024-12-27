package com.narvee.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.narvee.entity.States;
import com.narvee.request.dto.StateCodeDTO;

public interface StateCodeRepository extends JpaRepository<States, Long>{



}
