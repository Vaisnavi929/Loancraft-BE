package com.narvee.serviceimpl;

import java.io.IOException;
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
	public List<Document> getLoanAppDocs(Long id) {
		logger.info("!!! inside class: DocumentServiceImpl, method: getLoanAppDocs");
		return documentRepository.findByLoanApplicationLoanId(id);
	}

   
}