package com.narvee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narvee.entity.Reference;

public interface ReferenceRepository extends JpaRepository<Reference, Long> {

}
