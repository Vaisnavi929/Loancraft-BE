package com.narvee.serviceimpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.narvee.Service.DocumentService;
import com.narvee.entity.Applications;
import com.narvee.entity.Document;
import com.narvee.repository.ApplicationsRepository;
import com.narvee.repository.DocumentRepository;
import com.narvee.request.dto.LoanAppDocsResponse;

@Service
public class DocumentServiceImpl implements DocumentService {
	private static final Logger logger=LoggerFactory.getLogger(DocumentServiceImpl.class);
	
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private ApplicationsRepository loanApplicationRepository;

    @Override
    public Document saveDocument(MultipartFile file, Long loanId) throws IOException {
    	logger.info("!!! inside class: DocumentServiceImpl, method: saveDocument");
        Applications loanApplication = loanApplicationRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("LoanApplication not found with ID: " + loanId));
 
        Document document = new Document();
        document.setFileName(file.getOriginalFilename());
        document.setFileType(file.getContentType());
        document.setLoanApplication(loanApplication); 
        return documentRepository.save(document);
    }


	@Override
	public Document getByDocumentId(Long id) {
		logger.info("!!! inside class: DocumentServiceImpl, method: getByDocumentId");
		return documentRepository.findById(id).get();
	}

	@Override
	public boolean updateDocVerified(Long id, Boolean value) {
		logger.info("!!! inside class: DocumentServiceImpl, method: updateDocVerified");
		 int updatedCount = documentRepository.updateReviewed(id, value);

		    return updatedCount > 0;
	}


	@Override
	public LoanAppDocsResponse getLoanAppDocs(Long id) {
	    logger.info("!!! inside class: DocumentServiceImpl, method: getLoanAppDocs");

	    List<Document> allDocs = documentRepository.findByLoanApplicationLoanId(id);
	    List<Document> payslips = new ArrayList<>();
	    List<Document> bankStatements = new ArrayList<>();
	    List<Document> otherDocs = new ArrayList<>();
	    List<Document> aadhaar = new ArrayList<>();
	    List<Document> pan = new ArrayList<>();

	    for (Document doc : allDocs) {
	        String fileName = doc.getFileName().toLowerCase();

	        if (fileName.contains("payslip")) {
	            payslips.add(doc);
	        } else if (fileName.contains("bankstatement")) {
	            bankStatements.add(doc);
	        } else if (fileName.contains("aadhaar")) {
	            aadhaar.add(doc);
	        } else if (fileName.contains("pan")) {
	            pan.add(doc);
	        } else {
	            otherDocs.add(doc);
	        }
	    }

	    // Sort each list by fileType ascending (if fileType is a field in Document)
	    Comparator<Document> byFileType = Comparator.comparing(Document::getFileType);

	    aadhaar.sort(byFileType);
	    pan.sort(byFileType);
	    payslips.sort(byFileType);
	    bankStatements.sort(byFileType);
	    otherDocs.sort(byFileType);

	    return new LoanAppDocsResponse(aadhaar, pan, payslips, bankStatements, otherDocs);
	}


   
}