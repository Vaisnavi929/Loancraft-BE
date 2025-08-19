package com.narvee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.narvee.entity.ReferenceInfo;

@Repository
public interface CreferenceInfo extends JpaRepository<ReferenceInfo,Long> {

}
