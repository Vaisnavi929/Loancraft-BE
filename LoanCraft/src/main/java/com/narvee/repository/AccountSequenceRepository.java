package com.narvee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.narvee.entity.AccountSequence;

@Repository
public interface AccountSequenceRepository extends JpaRepository<AccountSequence,Long>{

	Optional<AccountSequence> findByYear(String year);

}
