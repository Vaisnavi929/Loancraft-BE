package com.narvee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narvee.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {

}
