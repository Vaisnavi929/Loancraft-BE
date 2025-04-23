package com.narvee.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.narvee.entity.Document;
import com.narvee.request.dto.LoanAppDocsResponse;

public interface DocumentService {

	public Document saveDocument(MultipartFile file, Long loanId) throws IOException;

	public Document getByDocumentId(Long id);

	public boolean updateDocVerified(Long id, Boolean value);

	public LoanAppDocsResponse  getLoanAppDocs(Long id);
}
