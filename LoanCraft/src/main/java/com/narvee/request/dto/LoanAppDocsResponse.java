package com.narvee.request.dto;

import java.util.List;

import com.narvee.entity.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanAppDocsResponse {
	private List<Document> aadhaar;
	private List<Document> pan;
    private List<Document> payslips;
    private List<Document> bankStatements;
    private List<Document> otherDocs;

    
   
}
