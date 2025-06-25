package com.narvee.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.narvee.entity.Document;
import com.narvee.request.dto.CustomerDocumentDto;
import com.narvee.request.dto.LoanAppDocsResponse;

public interface DocumentService {

	public Document saveDocument(MultipartFile file, Long loanId) throws IOException;

	public Document getByDocumentId(Long id);

	public boolean updateDocVerified(Long id, Boolean value);

	public LoanAppDocsResponse  getLoanAppDocs(Long id);
	
	public  ResponseEntity<String> uploadPdfFile(MultipartFile file, String customerId, String documentType, Long loanId);
	
	public ResponseEntity<String> uploadMultipleFilesPdf(List<MultipartFile> files, Long loanId) throws Exception;
	public String uploadDocumentsPdf(MultipartFile panFile, MultipartFile aadhaarFile,
                List<MultipartFile> bankStatementFiles, List<MultipartFile> payslipFiles,
                List<MultipartFile> otherFiles, String customerId, Long loanId);

	public List<Document> getPhysicalVerificationDocuments(Long loanId, String fileType);

	boolean updatePhysicalDocVerified(Long id, Boolean value,Long loanId);
	public List<CustomerDocumentDto> getAllDocumentsByCustomerId(Long customerId);
}
