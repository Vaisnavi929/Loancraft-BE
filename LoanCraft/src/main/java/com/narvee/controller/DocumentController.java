package com.narvee.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.narvee.Service.DocumentService;
import com.narvee.entity.Applications;
import com.narvee.entity.Document;
import com.narvee.repository.ApplicationsRepository;
import com.narvee.repository.DocumentRepository;
import com.narvee.util.EncryptionUtil;
@RestController
@RequestMapping("/api/documents")
public class DocumentController {

	@Autowired
	ApplicationsRepository applicationsRepository;
	
	@Autowired
	DocumentRepository documentRepository;
	
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
    private static final String UPLOAD_DIR = "C:\\FileUploads";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("customerId") long customerId,
            @RequestParam("documentType") String documentType,
            @RequestParam("loanId") Long loanId 
    ) {
        try {         
            String encryptedCustomerId = EncryptionUtil.encrypt(customerId);           
            String originalFilename = file.getOriginalFilename();
            String formattedFileName = documentType.toLowerCase() + "-" + encryptedCustomerId + "_" + originalFilename;
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            File destinationFile = Paths.get(UPLOAD_DIR, formattedFileName).toFile();
            try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
                fos.write(file.getBytes());
            }
            Applications loanApp = applicationsRepository.findById(loanId)
                    .orElseThrow(() -> new RuntimeException("Loan not found"));
            Document doc = new Document();
            doc.setFileName(formattedFileName);
            doc.setFileType(file.getContentType());  
            doc.setData(file.getBytes());
            doc.setLoanApplication(loanApp);
            documentRepository.save(doc);
            return ResponseEntity.ok("File uploaded and stored in DB: " + formattedFileName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }   
    @GetMapping("/download-file/{id}")
    public ResponseEntity<Resource> downloadFileFromDisk(@PathVariable Long id) {
        try {     
            Document doc = documentRepository.findById(id)
                    .orElseThrow(() -> new FileNotFoundException("File not found with ID: " + id));
            String filename = doc.getFileName();
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new FileNotFoundException("Could not read file: " + filename);
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(doc.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getFileName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }    
    @DeleteMapping("/delete-file/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long id) {
        try {
            Document doc = documentRepository.findById(id)
                    .orElseThrow(() -> new FileNotFoundException("File not found with ID: " + id));

            String fileName = doc.getFileName();
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName).normalize();
            File file = filePath.toFile();
            if (file.exists()) {
                if (!file.delete()) {
                    throw new RuntimeException("Failed to delete file from disk: " + fileName);
                }
            }
            documentRepository.deleteById(id);
            return ResponseEntity.ok("File and DB entry deleted successfully!");
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while deleting file: " + e.getMessage());
        }
    }


}