package com.narvee.Service;

import org.springframework.web.multipart.MultipartFile;

import com.narvee.entity.Document;

import java.io.IOException;

public interface DocumentService {
	
public Document saveDocument(MultipartFile file,Long loanId) throws IOException;

public Document getByDocumentId(Long id);

public boolean updateDocVerified(Long id,Boolean value);
}
