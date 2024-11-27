package com.narvee.Service;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


public interface GeneratexcelService {
	
	   public ResponseEntity<InputStreamResource> generateExcel(Long loanId) throws IOException;
//   public ResponseEntity<InputStreamResource> generateExcelSheet(Long loanId) throws IOException;

	ResponseEntity<InputStreamResource> generateExcelSheet(Long loanId) throws IOException;
	
	public String processExcel(MultipartFile file) throws IOException;
	
	
}
