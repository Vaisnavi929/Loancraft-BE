//package com.narvee.controller;
//
//import java.io.IOException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.InputStreamResource;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.narvee.Service.GeneratexcelService;
//
//@RestController
//@CrossOrigin("*")
//@RequestMapping("/api/excel")
//public class ExcelController {
//
//	@Autowired
//	private GeneratexcelService generatexcelService;
//
//	@GetMapping("/download/{loanId}")
//	public ResponseEntity<InputStreamResource> downloadExcel(@PathVariable Long loanId) throws IOException {
//		return generatexcelService.generateExcel(loanId);
//	}
//
//	@GetMapping("/Excel/{loanId}")
//	public ResponseEntity<InputStreamResource> downloadExcelsheet(@PathVariable Long loanId) throws IOException {
//		return generatexcelService.generateExcelSheet(loanId);
//	}
//
//	@PostMapping("/upload")
//	public ResponseEntity<String> uploadExcel(@RequestParam("file") MultipartFile file) {
//		try {
//			if (file.isEmpty()) {
//				return ResponseEntity.badRequest().body("File is empty!");
//			}
//			// Pass the file for processing
//			String message = generatexcelService.processExcel(file);
//			return ResponseEntity.ok(message);
//		} catch (Exception e) {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Error processing file: " + e.getMessage());
//		}
//	}
//
//}
