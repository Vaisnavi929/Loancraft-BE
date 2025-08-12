package com.narvee.serviceimpl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.properties.AreaBreakType;
import com.narvee.Service.DocumentService;
import com.narvee.entity.Applications;
import com.narvee.entity.Document;
import com.narvee.repository.ApplicationsRepository;
import com.narvee.repository.DocumentRepository;
import com.narvee.repository.VerificationRepository;
import com.narvee.request.dto.CustomerDocumentDto;
import com.narvee.request.dto.LoanAppDocsResponse;
import com.narvee.util.EncryptionUtil;

@Service
public class DocumentServiceImpl implements DocumentService {
	private static final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);

	@Value("${AppFilesDir}")
	private String UPLOAD_DIR;

	@Autowired
	private DocumentRepository documentRepository;
	@Autowired
	private ApplicationsRepository loanApplicationRepository;

	@Autowired
	private VerificationRepository verificationRepository;

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
	public boolean updatePhysicalDocVerified(Long id, Boolean value, Long loanId) {
		logger.info("!!! inside class: DocumentServiceImpl, method: updatePhysicalDocVerified");
		int updatedCount = documentRepository.updateReviewed(id, value);

		List<Document> documents = Optional.ofNullable(documentRepository.findByLoanApplicationLoanId(loanId))
				.filter(list -> !list.isEmpty()).orElse(null);

		boolean physicallocationVerified = false;
		if (documents != null) {
			List<Document> physicalDocs = documents.stream()
					.filter(doc -> "Physical Doc".equalsIgnoreCase(doc.getFileType())).collect(Collectors.toList());

			if (!physicalDocs.isEmpty()) {

				physicallocationVerified = physicalDocs.stream()
						.allMatch(doc -> Boolean.TRUE.equals(doc.getDocVerified()));
				verificationRepository.updatePhysicalLocationVerified(loanId, physicallocationVerified);

			}
		}

		return updatedCount > 0;
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
			String fullFileName = doc.getFileName();
			String originalName = fullFileName.substring(fullFileName.lastIndexOf("_") + 1);
			String prefix = fullFileName.contains("-") ? fullFileName.substring(0, fullFileName.indexOf("-")) : "";
			String extension = "";
			if (originalName.contains(".")) {
				extension = originalName.substring(originalName.lastIndexOf("."));
			}
			String cleanedName = prefix + extension;
			doc.setFileName(cleanedName);

			String fileName = cleanedName.toLowerCase();
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

		return new LoanAppDocsResponse(aadhaar, pan, payslips, bankStatements, otherDocs);
	}

	@Override
	public ResponseEntity<String> uploadPdfFile(MultipartFile file, String customerId, String documentType,
			Long loanId) {

		try {
			String originalFilename = file.getOriginalFilename();
			String baseName = originalFilename != null ? originalFilename.replaceAll("\\.[^.]*$", "") : "document";
			String formattedFileName = documentType.toLowerCase() + "-" + customerId + "_" + baseName + ".pdf";

			File uploadDir = new File(UPLOAD_DIR);
			if (!uploadDir.exists())
				uploadDir.mkdirs();

			File pdfFile = new File(uploadDir, formattedFileName);
			convertToPdf(file, pdfFile);

			Applications loanApp = loanApplicationRepository.findById(loanId)
					.orElseThrow(() -> new RuntimeException("Loan not found"));

			Document doc = new Document();
			byte[] fileBytes = Files.readAllBytes(pdfFile.toPath());

			doc.setFileName(formattedFileName);
			doc.setFileType("Physical Doc");
			doc.setLoanApplication(loanApp);

			documentRepository.save(doc);

			return ResponseEntity.ok("File converted to PDF and uploaded: " + formattedFileName);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
		}
	}

	private void convertToPdf(MultipartFile file, File pdfFile) throws IOException {
		String contentType = file.getContentType();
		String originalFilename = file.getOriginalFilename();

		if (contentType != null && contentType.startsWith("image/")) {
			// Convert image to PDF
			BufferedImage image = ImageIO.read(file.getInputStream());
			try (PDDocument doc = new PDDocument()) {
				PDPage page = new PDPage(new PDRectangle(image.getWidth(), image.getHeight()));
				doc.addPage(page);
				PDImageXObject pdImage = LosslessFactory.createFromImage(doc, image);
				try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
					contentStream.drawImage(pdImage, 0, 0);
				}
				doc.save(pdfFile);
			}
		} else if (originalFilename != null && originalFilename.endsWith(".docx")) {
			// Convert Word (.docx) to PDF using Apache POI and iText
			try (XWPFDocument docx = new XWPFDocument(file.getInputStream());
					OutputStream out = new FileOutputStream(pdfFile)) {

				PdfWriter writer = new PdfWriter(out);
				com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
				com.itextpdf.layout.Document pdfDocument = new com.itextpdf.layout.Document(pdfDoc);

				for (XWPFParagraph p : docx.getParagraphs()) {
					pdfDocument.add(new com.itextpdf.layout.element.Paragraph(p.getText()));
				}

				pdfDocument.close();
			}
		} else if (contentType != null && contentType.equals("text/plain")) {
			// Convert plain text to PDF
			try (OutputStream out = new FileOutputStream(pdfFile)) {
				PdfWriter writer = new PdfWriter(out);
				com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
				com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDoc);

				String text = new String(file.getBytes(), StandardCharsets.UTF_8);
				document.add(new com.itextpdf.layout.element.Paragraph(text));
				document.close();
			}
		} else if (originalFilename != null && originalFilename.endsWith(".xlsx")) {
			// Handle XLSX files (Excel spreadsheets)
			try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
					OutputStream out = new FileOutputStream(pdfFile)) {

				PdfWriter writer = new PdfWriter(out);
				com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
				com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDoc);

				for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
					Sheet sheet = workbook.getSheetAt(i);
					document.add(new com.itextpdf.layout.element.Paragraph("Sheet: " + sheet.getSheetName()));

					for (Row row : sheet) {
						StringBuilder rowText = new StringBuilder();
						for (Cell cell : row) {
							switch (cell.getCellType()) {
							case STRING:
								rowText.append(cell.getStringCellValue());
								break;
							case NUMERIC:
								rowText.append(cell.getNumericCellValue());
								break;
							case BOOLEAN:
								rowText.append(cell.getBooleanCellValue());
								break;
							default:
								rowText.append(" ");
							}
							rowText.append(" | ");
						}
						document.add(new com.itextpdf.layout.element.Paragraph(rowText.toString()));
					}
					document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
				}
				document.close();
			}
		} else {
			throw new IOException("Unsupported file type for conversion.");
		}
	}

	@Override
	public ResponseEntity<String> uploadMultipleFilesPdf(List<MultipartFile> files, Long loanId) throws Exception {
		StringBuilder responseMessage = new StringBuilder();

		// Encrypt loanId
		String encryptedLoanId = EncryptionUtil.encrypt(loanId);

		// Ensure upload dir exists
		File uploadDir = new File(UPLOAD_DIR);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}

		// Find loan application
		Applications loanApp = loanApplicationRepository.findById(loanId)
				.orElseThrow(() -> new RuntimeException("Loan not found"));

		// Process each file
		for (MultipartFile file : files) {
			if (file.isEmpty() || file.getOriginalFilename() == null) {
				continue;
			}

			String originalFilename = file.getOriginalFilename();

			// Split filename and extension
			String baseName = originalFilename;
			String extension = "";
			int dotIndex = originalFilename.lastIndexOf('.');
			if (dotIndex > 0) {
				baseName = originalFilename.substring(0, dotIndex);
				extension = originalFilename.substring(dotIndex);
			}

			// Construct new filename for PDF
			String formattedFileName = baseName + "_" + encryptedLoanId + ".pdf";

			// Convert file to PDF
			File pdfFile = new File(uploadDir, formattedFileName);
			convertToPdf(file, pdfFile);

			// Save document metadata to DB
			Document doc = new Document();

			byte[] fileBytes = Files.readAllBytes(pdfFile.toPath());

			doc.setFileName(formattedFileName);
			doc.setFileType("application/pdf");
			doc.setLoanApplication(loanApp);
			documentRepository.save(doc);

			responseMessage.append("Uploaded: ").append(formattedFileName).append("\n");
		}

		// Return response message
		return new ResponseEntity<>(responseMessage.toString().trim(), HttpStatus.OK);
	}

	@Override
	public String uploadDocumentsPdf(MultipartFile panFile, MultipartFile aadhaarFile,
			List<MultipartFile> bankStatementFiles, List<MultipartFile> payslipFiles, List<MultipartFile> otherFiles,
			String customerId, Long loanId) {

		StringBuilder result = new StringBuilder();

		try {
			Applications loanApp = loanApplicationRepository.findById(loanId)
					.orElseThrow(() -> new RuntimeException("Loan not found"));

			File dir = new File(UPLOAD_DIR);
			if (!dir.exists())
				dir.mkdirs();

			BiConsumer<MultipartFile, String> processFile = (file, docType) -> {
				try {
					if (file == null || file.isEmpty())
						return;

					String originalName = file.getOriginalFilename();
					String baseName = originalName != null ? originalName.replaceAll("\\.[^.]+$", "") : "unknown";
					String pdfFileName = docType + "-" + customerId + "_" + baseName + ".pdf";

					File pdfFile = Paths.get(UPLOAD_DIR, pdfFileName).toFile();
					convertToPdf(file, pdfFile);

					Document doc = new Document();
					doc.setFileName(pdfFileName);
					doc.setFileType(docType);
					doc.setLoanApplication(loanApp);
					documentRepository.save(doc);

					result.append("Uploaded & converted to PDF: ").append(pdfFileName).append("\n");

				} catch (Exception ex) {
					result.append("Failed to upload/convert: ").append(file.getOriginalFilename()).append("\n");
				}
			};

			processFile.accept(panFile, "pan");
			processFile.accept(aadhaarFile, "aadhaar");

			if (bankStatementFiles != null)
				bankStatementFiles.forEach(file -> processFile.accept(file, "bankstatement"));

			if (payslipFiles != null)
				payslipFiles.forEach(file -> processFile.accept(file, "payslip"));

			if (otherFiles != null)
				otherFiles.forEach(file -> processFile.accept(file, "others"));

		} catch (Exception e) {
			e.printStackTrace();
			return "Upload failed: " + e.getMessage();
		}

		return result.toString();
	}

	@Override
	public List<Document> getPhysicalVerificationDocuments(Long loanId, String fileType) {
		logger.info("!!! inside class: DocumentServiceImpl, method: getLoanAppDocs");
		List<Document> documents = documentRepository.findByLoanApplicationLoanIdAndFileType(loanId, fileType);

		// Map to DTO with cleaned fileName
		return documents.stream().map(doc -> {
			Document dto = new Document();
			dto.setId(doc.getId());
			dto.setFileType(doc.getFileType());
			dto.setDocVerified(doc.getDocVerified());
			dto.setRemarks(doc.getRemarks());
            dto.setStatus(doc.getLoanApplication().getStatus());
			// Clean the filename (take substring before first underscore and add ".pdf")
			String original = doc.getFileName();
			String cleanedFileName = original != null && original.contains("_") ? original.split("_")[0] + ".pdf"
					: original;

			dto.setFileName(cleanedFileName);

			return dto;
		}).toList();
	}

	@Override
	public List<Map<String, Object>> getAllDocumentsByCustomerId(String encryptedCustomerId) {
	    logger.info("!!! inside class: DocumentServiceImpl, method: getAllDocumentsByCustomerId");
	    try {
	        long customerId = EncryptionUtil.decrypt(encryptedCustomerId);
	        List<CustomerDocumentDto> rawDocs = documentRepository.getAllDocumentsByCustomerId(customerId);

	        return rawDocs.stream().map(doc -> {
	            Map<String, Object> map = new LinkedHashMap<>();
	            map.put("id", doc.getId());

	            // Clean fileName
	            String original = doc.getFileName();
	            String cleanedFileName = (original != null && original.contains("_"))
	                    ? original.substring(original.indexOf("_") + 1)
	                    : original;

	            map.put("fileName", cleanedFileName);
	            map.put("fileType", doc.getFileType());
	            map.put("customerId", doc.getCustomerId());
	            map.put("loanId", doc.getLoanId());

	            return map;
	        }).toList();

	    } catch (Exception e) {
	        logger.error("Failed to decrypt customer ID", e);
	        throw new IllegalArgumentException("Invalid encrypted customer ID");
	    }
	}

}