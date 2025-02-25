package com.narvee.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.narvee.Service.DocumentService;
import com.narvee.entity.Document;



@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/docupload/{loanId}")
    public ResponseEntity<Document> uploadDocument(@RequestParam("file") MultipartFile file,@PathVariable Long loanId) throws IOException {
        try {
            Document savedDocument = documentService.saveDocument(file, loanId);
            return new ResponseEntity<>(savedDocument, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } 
    @GetMapping("/downloaddoc/{id}")
    public Document getById(@PathVariable Long id) {
		return documentService.getByDocumentId(id);
    	
    }
}