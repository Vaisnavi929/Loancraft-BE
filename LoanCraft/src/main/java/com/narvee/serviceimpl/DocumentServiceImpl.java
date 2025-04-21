package com.narvee.serviceimpl;

import java.io.IOException;

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

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private ApplicationsRepository loanApplicationRepository;

    @Override
    public Document saveDocument(MultipartFile file, Long loanId) throws IOException {
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
		
		return documentRepository.findById(id).get();
	}

	@Override
	public boolean updateDocVerified(Long id, Boolean value) {
		
		 int updatedCount = documentRepository.updateReviewed(id, value);

		    return updatedCount > 0;
	}

   
}