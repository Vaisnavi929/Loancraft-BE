package com.narvee.Service;


import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.narvee.entity.Verification;
import com.narvee.request.dto.VerificationTrackResponseDTO;


public interface VerificationService {
	
	public Verification saveVerification(Verification verification);
	public Verification updaterificationVerification(Verification verification);
	public Verification getById(Long verifiedId);
	public List<Verification> getAllVerification();
	public boolean deleteById(Long verifiedId);
	public Verification uploadDocumentsToVerification(Long verifiedId, List<MultipartFile> files) throws IOException ;
	public boolean  updateReviewed(Long verifiedId, Boolean value);
	public boolean updateCibil(Long verifiedId, Boolean value);
	public boolean updatePhysicalLocationVerified(Long verifiedId, Boolean value);
	public boolean updateSanction(Long verifiedId, Boolean value);
	public boolean updateDisbursed(Long verifiedId, Boolean value);
	public VerificationTrackResponseDTO getAllVericationdetailsByLoanId(Long loanId);
	
	
	  
}
