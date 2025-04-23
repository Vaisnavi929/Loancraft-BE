package com.narvee.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiConsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.narvee.Service.DocumentService;
import com.narvee.commons.RestAPIResponse;
import com.narvee.entity.Applications;
import com.narvee.entity.Document;
import com.narvee.repository.ApplicationsRepository;
import com.narvee.repository.DocumentRepository;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

	@Autowired
	ApplicationsRepository applicationsRepository;

	@Autowired
	DocumentRepository documentRepository;

	@Autowired
	private DocumentService documentService;

	@Value("${AppFilesDir}")
	private String UPLOAD_DIR;

	@PostMapping("/docupload/{loanId}")
	public ResponseEntity<Document> uploadDocument(@RequestParam("file") MultipartFile file, @PathVariable Long loanId)
			throws IOException {
		try {
			Document savedDocument = documentService.saveDocument(file, loanId);
			return new ResponseEntity<>(savedDocument, HttpStatus.CREATED);
		} catch (IOException e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam("customerId") String customerId, @RequestParam("documentType") String documentType,
			@RequestParam("loanId") Long loanId) {
		try {
			String originalFilename = file.getOriginalFilename();
			String formattedFileName = documentType.toLowerCase() + "-" + customerId + "_" + originalFilename;

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
			byte[] fileBytes = file.getBytes();

			// Logging the type of fileBytes for debugging

			doc.setFileName(formattedFileName);
			doc.setFileType(file.getContentType());

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
//			return ResponseEntity.ok().contentType(MediaType.parseMediaType(doc.getFileType()))
//					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getFileName() + "\"")
//					.body(resource);
			
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
					.body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error while deleting file: " + e.getMessage());
		}
	}



	@PostMapping("/upload-documents")
	public ResponseEntity<RestAPIResponse> uploadDocuments(
			@RequestParam(value = "pan", required = false) MultipartFile panFile,
			@RequestParam(value = "aadhaar", required = false) MultipartFile aadhaarFile,
			@RequestParam(value = "bankStatements", required = false) List<MultipartFile> bankStatementFiles,
			@RequestParam(value = "payslips", required = false) List<MultipartFile> payslipFiles,
			@RequestParam(value = "others", required = false) List<MultipartFile> otherFiles,
			@RequestParam("customerId") String customerId, @RequestParam("loanId") Long loanId) {

		System.err.println(UPLOAD_DIR);

		StringBuilder result = new StringBuilder();

		try {
			Applications loanApp = applicationsRepository.findById(loanId)
					.orElseThrow(() -> new RuntimeException("Loan not found"));

			File uploadDir = new File(UPLOAD_DIR);
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}

			// Helper method to save file with custom name
			BiConsumer<MultipartFile, String> processFile = (file, docType) -> {
				try {
					if (file == null || file.isEmpty())
						return;

					String originalFilename = file.getOriginalFilename();
					String formattedFileName = docType + "-" + customerId + "_" + originalFilename;

					File destinationFile = Paths.get(UPLOAD_DIR, formattedFileName).toFile();
					try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
						fos.write(file.getBytes());
					}

					Document doc = new Document();
					doc.setFileName(formattedFileName);
					doc.setFileType(docType);
					doc.setLoanApplication(loanApp);

					documentRepository.save(doc);
					result.append("Uploaded: ").append(formattedFileName).append("\n");

				} catch (Exception ex) {
					ex.printStackTrace();
					result.append("Failed to upload file: ").append(file.getOriginalFilename()).append("\n");
				}
			};

			// Process optional single files
			processFile.accept(panFile, "pan");
			processFile.accept(aadhaarFile, "aadhaar");

			// Process optional multiple bank statements
			if (bankStatementFiles != null) {

				for (MultipartFile bankFile : bankStatementFiles) {
					processFile.accept(bankFile, "bankstatement");
				}
			}

			// Process optional multiple payslips
			if (payslipFiles != null) {

				for (MultipartFile payslipFile : payslipFiles) {
					processFile.accept(payslipFile, "payslip");
				}
			}

			// Process optional multiple other documents
			if (otherFiles != null) {

				for (MultipartFile otherFile : otherFiles) {
					processFile.accept(otherFile, "others" );
				}
			}

			// Process optional single files
			processFile.accept(panFile, "pan");
			processFile.accept(aadhaarFile, "aadhaar");

			return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "Documents uploaded successfully",
					"Document Upload Summary: \n " + result.toString()), HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<RestAPIResponse>(
					new RestAPIResponse("failed", "Upload failed: " + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(value = "/updateDocVerified/{id}/{verify}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> updateDocVerified(@PathVariable Long id, @PathVariable Boolean verify) {
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "successfully update DocVerified  ",
				documentService.updateDocVerified(id, verify)), HttpStatus.OK);

	}

	@RequestMapping(value = "/getLoanAppDocs/{loanId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getLoanAppDocs(@PathVariable Long loanId) {
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("success", "Documents fetched successfully",
				documentService.getLoanAppDocs(loanId)), HttpStatus.OK);

	}

}
