package com.narvee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narvee.entity.Penalty;
import com.narvee.request.dto.PenaltyDto;

public interface PenaltyRepository extends JpaRepository<Penalty, Long> {

	Penalty save(PenaltyDto penalty);

}
