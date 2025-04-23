package com.narvee.serviceimpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.narvee.Service.VerificationService;
import com.narvee.entity.Document;
import com.narvee.entity.Verification;
import com.narvee.repository.DocumentRepository;
import com.narvee.repository.VerificationRepository;
import com.narvee.request.dto.VerificationDTO;
import com.narvee.request.dto.VerificationTrackResponseDTO;

import jakarta.transaction.Transactional;

@Service
public class VerificationServiceImpl implements VerificationService{
	
	private static final Logger logger = LoggerFactory.getLogger(VerificationServiceImpl.class);
	private static final String UPLOAD_DIR = "C:\\FileUploads";
	@Autowired
	private VerificationRepository verificationRepository;

	@Autowired
	DocumentRepository documentRepository;
	@Override
	public Verification saveVerification(Verification verification) {
		logger.info("!!! inside class: VerificationServiceImpl, method: saveVerification");
		return verificationRepository.save(verification);
	}

	@Override
	public Verification updaterificationVerification(Verification verification) {
		logger.info("!!! inside class: VerificationServiceImpl, method: updaterificationVerification");
		return verificationRepository.save(verification);
	}

	@Override
	public Verification getById(Long verifiedId) {
		logger.info("!!! inside class: VerificationServiceImpl, method: getById");
		return verificationRepository.findById(verifiedId).get();
	}

	@Override
	public List<Verification> getAllVerification() {
		logger.info("!!! inside class: VerificationServiceImpl, method: getAllVerification");
		return verificationRepository.findAll();
	}

	@Override
	public boolean deleteById(Long verifiedId) {
		logger.info("!!! inside class: VerificationServiceImpl, method: deleteById");
		verificationRepository.deleteById(verifiedId);
		return false;
	}
	
	@Override
	public Verification uploadDocumentsToVerification(Long verifiedId, List<MultipartFile> files) throws IOException {
	    logger.info("!!! inside class: VerificationServiceImpl, method: uploadDocumentsToVerification");

	    Verification verification = verificationRepository.findById(verifiedId)
	            .orElseThrow(() -> new RuntimeException("Verification not found with ID: " + verifiedId));

	    // Ensure the upload directory exists
	    File uploadDir = new File(UPLOAD_DIR);
	    if (!uploadDir.exists()) {
	        uploadDir.mkdirs();
	    }

	    for (MultipartFile file : files) {
	        if (file != null && !file.isEmpty()) {
	            // Save file to filesystem
	            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
	            File destinationFile = new File(uploadDir, fileName);
	            try (FileOutputStream fos = new FileOutputStream(destinationFile)) {
	                fos.write(file.getBytes());
	            }

	            // Create Document entity and set metadata
	            Document document = new Document();
	            document.setFileName(fileName);
	            document.setFileType(file.getContentType());
	            document.setDocVerified(false);
	            document.setVerification(verification);

	            // Add document to verification
	            verification.getPhysicalLocationDocument().add(document);
	        }
	    }

	    // Save the verification (CascadeType.ALL saves documents too)
	    return verificationRepository.save(verification);
	}

	@Transactional
	@Override
	public boolean updateReviewed(Long verifiedId, Boolean value) {
	    logger.info("!!! inside class: VerificationServiceImpl, method: updateReviewed");

	    int updatedCount = verificationRepository.updateReviewed(verifiedId, value);

	    return updatedCount > 0;
	}
	@Override
	public boolean updateCibil(Long verifiedId, Boolean value) {
		logger.info("!!! inside class: VerificationServiceImpl, method: updateCibil");
		 int updatedCount = verificationRepository.updateCibil(verifiedId, value);

		    return updatedCount > 0; 
	}
	@Override
	public boolean updatePhysicalLocationVerified(Long verifiedId, Boolean value) {
		logger.info("!!! inside class: VerificationServiceImpl, method: updatePhysicalLocationVerified");
		int updatedCount = verificationRepository.updatePhysicalLocationVerified(verifiedId, value);

	    return updatedCount > 0; 
	}

	@Override
	public boolean updateSanction(Long verifiedId, Boolean value) {
		logger.info("!!! inside class: VerificationServiceImpl, method: updateSanction");
		int updatedCount = verificationRepository.updateSanction(verifiedId, value);

	    return updatedCount > 0; 
	}

	@Override
	public boolean updateDisbursed(Long verifiedId, Boolean value) {
		logger.info("!!! inside class: VerificationServiceImpl, method: updateDisbursed");
		int updatedCount = verificationRepository.updateDisbursed(verifiedId, value);

	    return updatedCount > 0; 
	}

	@Override
	public VerificationTrackResponseDTO getAllVericationdetailsByLoanId(Long loanId) {
		logger.info("!!! inside class: VerificationServiceImpl, method: getAllVericationdetailsByLoanId");
		List<Document> documents = documentRepository.findByLoanApplicationLoanId(loanId);
		boolean allVerified = documents.stream().allMatch(Document::getDocVerified);

	    VerificationDTO dto = verificationRepository.getAllVericationDetailsByLoanid(loanId);
	    VerificationTrackResponseDTO trackResponse=new VerificationTrackResponseDTO();
	    trackResponse.setDocumentsVerified(allVerified);
	    trackResponse.setCibil(dto.getCibil());
	    trackResponse.setDisbursed(dto.getDisbursed());
	    trackResponse.setPhysicalLocationVerified(dto.getPhysicalLocationVerified());
	    trackResponse.setReviewed(dto.getReviewed());
	    trackResponse.setSanction(dto.getSanction());
	   
	
		
		return trackResponse;
	}


}
