package com.fabiang.documentweb.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fabiang.documentweb.entities.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {

}
