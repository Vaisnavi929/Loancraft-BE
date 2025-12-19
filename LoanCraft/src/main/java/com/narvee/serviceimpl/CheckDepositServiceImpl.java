package com.narvee.serviceimpl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.narvee.Service.CheckDepositService;
import com.narvee.Service.SortingRequestDto;
import com.narvee.controller.DocumentController;
import com.narvee.entity.CheckDeposit;
import com.narvee.entity.Disbursment;
import com.narvee.entity.LmsRepayment;
import com.narvee.entity.LowCheckDepositApproval;
import com.narvee.entity.SkippedCheckDeposit;
import com.narvee.entity.UserInfo;
import com.narvee.repository.CheckDepositRepository;
import com.narvee.repository.DisbursmentRepository;
import com.narvee.repository.LowCheckDepositApprovalRespository;
import com.narvee.repository.RepaymentRepository;
import com.narvee.repository.SkippedCheckDepositRepository;
import com.narvee.repository.UserRepository;
import com.narvee.request.dto.CheckDepositDTO;
import com.narvee.request.dto.CheckDepositResponseDTO;
import com.narvee.request.dto.RepaymentDropdown;
import com.narvee.request.dto.SkippedChequeResponseDTO;


@Service
public class CheckDepositServiceImpl implements CheckDepositService {

    private final DocumentController documentController;

	private static final Logger logger = LoggerFactory.getLogger(CheckDepositServiceImpl.class);

	@Autowired
	private CheckDepositRepository depositRepository;
	
	@Autowired
	private RepaymentRepository repaymentRepository;
	
	//Abhinay
	@Autowired
    private LowCheckDepositApprovalRespository lowCheckDepositApprovalRespository;

	 @Autowired
	 private DisbursmentRepository disbursmentRepository;
	 
	 @Autowired
	 private UserRepository userRepository;
	 
	 @Autowired
	 private SkippedCheckDepositRepository skippedCheckDepositRepository;
	 
	 //---------

	@Autowired
	ModelMapper mapper;

    CheckDepositServiceImpl(DocumentController documentController) {
        this.documentController = documentController;
    }

	@Override
	public CheckDeposit saveDeposit(CheckDepositDTO checkDepositDTO) {
		logger.info("!!! inside class : CheckDepositServiceImpl,!! method: saveBounceEntry()  ");
		CheckDeposit checkDeposit = mapper.map(checkDepositDTO, CheckDeposit.class);
		Disbursment disbursment = new Disbursment();
		disbursment.setDisbursmentId(checkDepositDTO.getDisbursmentId());
		checkDeposit.setDisbursment(disbursment);
		LmsRepayment lmsRepayment = new LmsRepayment();
		lmsRepayment.setRepaymentId(checkDepositDTO.getRepaymentId());
		checkDeposit.setLmsRepayment(lmsRepayment);
		return depositRepository.save(checkDeposit);
	}

	@Override
	public CheckDeposit updateDeposit(CheckDepositDTO checkDepositDTO) {
		logger.info("!!! inside class : CheckDepositServiceImpl,!! method: updateBounceEntry()  ");
		CheckDeposit checkDeposit = mapper.map(checkDepositDTO, CheckDeposit.class);
		Disbursment disbursment = new Disbursment();
		disbursment.setDisbursmentId(checkDepositDTO.getDisbursmentId());
		checkDeposit.setDisbursment(disbursment);
		LmsRepayment lmsRepayment = new LmsRepayment();
		lmsRepayment.setRepaymentId(checkDepositDTO.getRepaymentId());
		checkDeposit.setLmsRepayment(lmsRepayment);
		return depositRepository.save(checkDeposit);
	}

	@Override
	public Page<CheckDepositResponseDTO> getAllCheckDeposit(SortingRequestDto sortingRequestDto) {
		logger.info("!!! inside class : CheckDepositServiceImpl, !! method : getAllCheckDeposit()");
		String sortField = sortingRequestDto.getSortField();
		String sortOrder = sortingRequestDto.getSortOrder();
		String keyword = sortingRequestDto.getKeyword();
		Integer pageNumber = sortingRequestDto.getPageNumber();
		Integer pageSize = sortingRequestDto.getPageSize();

		if (sortField.equalsIgnoreCase("Amount"))
			sortField = "amount";
		else if (sortField.equalsIgnoreCase("PostingDate"))
			sortField = "posting_date";
		else if (sortField.equalsIgnoreCase("ChequeNumber"))
			sortField = "cheque_number";
		else if (sortField.equalsIgnoreCase("Reason"))
			sortField = "reason";
		else if (sortField.equalsIgnoreCase("remarks"))
			sortField = "remarks";
		else if (sortField.equalsIgnoreCase("Status"))
			sortField = "status";
		else if (sortField.equalsIgnoreCase("ChequeClearanceDate"))
			sortField = "cheque_clearance_date";
		else if (sortField.equalsIgnoreCase("createddate"))
			sortField = "createddate";
		else if (sortField.equalsIgnoreCase("referenceId"))
			sortField = "referenceId";
		else if (sortField.equalsIgnoreCase("firstName"))
			sortField = "full_name";
		else
			sortField = "updateddate";
		Sort.Direction sortDirection = Sort.Direction.ASC;
		if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
			sortDirection = Sort.Direction.DESC;
		}

		Sort sort = Sort.by(sortDirection, sortField);
		Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
		if (keyword.equalsIgnoreCase("empty")) {
			return depositRepository.getAllCheckDepositWithPagination(pageable);

		} else {
			return depositRepository.getAllCheckDepositWithKeyword(pageable, keyword);

		}

	}

	@Override
	public String updateStatus(Long id, String status, String reason, LocalDate chequeClearanceDate, Long userid) {
		logger.info("!!! inside class:CheckDepositServiceImpl,!! method :updateStatus() ");
		LocalDateTime now = LocalDateTime.now();
		Double chequeReturnCharges = null;
		if (!status.equalsIgnoreCase("Cleared")) {
			chequeReturnCharges = 750.00;
		} else {
			chequeReturnCharges = 0.0;
		}

		int rows = depositRepository.updateStatus(id, status, reason, userid, chequeReturnCharges, now,chequeClearanceDate);
		if (rows > 0) {
			return "Update successful";
		} else {
			return "No record found to update";
		}
	}

	@Override
	public CheckDeposit getById(Long id) {
		logger.info("!!! inside class:CheckDepositServiceImpl,!! method :getById() ");
		return depositRepository.findById(id).get();
	}

	@Override
	@Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
	public void updatePostedChecksToInProgress() {
		logger.info("!!! inside class:CheckDepositServiceImpl,!! method :updatePostedChecksToInProgress() ");
		LocalDate yesterday = LocalDate.now().minusDays(1);
		List<CheckDeposit> checks = depositRepository.findByStatusAndPostingDate("Posted", yesterday);

		for (CheckDeposit check : checks) {
			check.setStatus("Pending");
		}

		depositRepository.saveAll(checks);
	}

	@Override
	public List<Map<String, Object>> getRepaymentMonthYear(Long disbursmentId) {
	    logger.info("!!! inside class:CheckDepositServiceImpl,!! method :getRepaymentMonthYear() ");

	    List<RepaymentDropdown> repaymentList = depositRepository.getRepaymentMonthAndYearByDisbursmentId(disbursmentId);
	    List<Map<String, Object>> responseList = new ArrayList<>();

	    for (RepaymentDropdown repaymentDropdown : repaymentList) {
	        // Build individual repayment info
	        Map<String, Object> repaymentMap = new LinkedHashMap<>();
	        repaymentMap.put("repaymentId", repaymentDropdown.getRepaymentId());
	        repaymentMap.put("dueMonthYear", repaymentDropdown.getDueMonthYear());

	        // Get requests for this repayment
	        List<Object[]> requests = depositRepository.getlowDepositRequests(repaymentDropdown.getRepaymentId());
	        List<Map<String, Object>> requestList = new ArrayList<>();

	        for (Object[] req : requests) {
	            Map<String, Object> requestMap = new HashMap<>();
	            requestMap.put("repaymentId", req[0]);
	            requestMap.put("requstedAmount", req[1]);
	            requestMap.put("isApproved", req[2]);
	            requestList.add(requestMap);
	        }

	        // Add requests to the repayment object
	        repaymentMap.put("requests", requestList);

	        // Add to final list
	        responseList.add(repaymentMap);
	    }

	    return responseList;
	}



	@Override
	public boolean existsByLmsRepaymentRepaymentId(Long rid) {
		logger.info("!!! inside class:CheckDepositServiceImpl,!! method : ExistsByLmsRepaymentRepaymentId() ");
		return false; //depositRepository.existsByLmsRepaymentRepaymentId(rid);
	}

	@Override
	public boolean updateStausofRequestedCheque(CheckDepositDTO checkDepositDTO) {
		depositRepository.updateStatusofRequestedCheque(checkDepositDTO.getPostingDate(), 
				checkDepositDTO.getChequeNumber(), checkDepositDTO.getAmount(),checkDepositDTO.getStatus());
		return true;
		
	}
	
	// -------Abhinay
	@Override
	@Transactional
	public List<SkippedChequeResponseDTO> saveCheckDepositExcel(MultipartFile file) {
	    logger.info("!!! saveCheckDepositExcel started");
	    List<SkippedChequeResponseDTO> skippedCheques = new ArrayList<>();
	    if (file == null || file.isEmpty()) {
	        return skippedCheques;
	    }
	    // ================= FETCH UPLOADER =================
	    Long uploaderId = null;
	    try {
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        if (auth != null && auth.isAuthenticated()) {
	            Optional<UserInfo> uopt = userRepository.findByEmail(auth.getName());
	            if (uopt.isPresent()) {
	                uploaderId = uopt.get().getUserId();
	            }
	        }
	    } catch (Exception ex) {
	        logger.debug("Security not available: {}", ex.getMessage());
	    }
	    try (InputStream is = file.getInputStream();
	         Workbook workbook = WorkbookFactory.create(is)) {
	        Sheet sheet = workbook.getSheetAt(0);
	        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
	            Row row = sheet.getRow(i);
	            if (row == null) continue;
	            int slNo = i; // SL.NO exactly as Excel
	            String loanAccountNo = getStringCell(row.getCell(1));
	            String chequeNumberRaw = getStringCell(row.getCell(2));
	            LocalDate postingDate = getLocalDateCell(row.getCell(3));
	            LocalDate clearanceDate = getLocalDateCell(row.getCell(4));
	            Double amount = getDoubleCell(row.getCell(5));
	            String status = getStringCell(row.getCell(6));
	            String reason = getStringCell(row.getCell(7));
	            if (loanAccountNo == null || chequeNumberRaw == null
	                    || postingDate == null || status == null) {
	                continue;
	            }
	            String chequeNumber = formatChequeNumber(chequeNumberRaw);
//	            Disbursment disbursment = disbursmentRepository
//	                    .findByLoanAccountNumber(loanAccountNo.trim())
//	                    .orElse(null);
//	            if (disbursment == null) continue;
	            List<Disbursment> disbursments =
	                    disbursmentRepository.findByLoanAccountNumber(loanAccountNo.trim());

	            if (disbursments == null || disbursments.isEmpty()) {
	                continue; // no disbursement found → skip row
	            }

	            // pick latest disbursement (safe LMS rule)
	            Disbursment disbursment =
	                    disbursments.stream()
	                            .max(Comparator.comparing(Disbursment::getDisbursmentId))
	                            .orElse(null);

	            // ================= EXISTING CHEQUES =================
	            List<CheckDeposit> existingDeposits =
	                    depositRepository.findByChequeNumberAndDisbursment_DisbursmentId(
	                            chequeNumber,
	                            disbursment.getDisbursmentId()
	                    );
	            boolean hasClearedAlready = existingDeposits.stream()
	                    .anyMatch(cd -> "Cleared".equalsIgnoreCase(cd.getStatus()));

	            // ================= SKIP RULES (BEFORE SAVE) =================
	            // ❌ Rejected after cleared
	            if (hasClearedAlready && "Rejected".equalsIgnoreCase(status)) {
	                saveSkipped(
	                        slNo, loanAccountNo, chequeNumber,
	                        postingDate, clearanceDate,
	                        amount, status, reason,
	                        "Rejected after cheque was cleared",
	                        skippedCheques
	                );
	                continue;
	            }
	            // ❌ Duplicate cleared
	            if (hasClearedAlready && "Cleared".equalsIgnoreCase(status)) {
	                saveSkipped(
	                        slNo, loanAccountNo, chequeNumber,
	                        postingDate, clearanceDate,
	                        amount, status, reason,
	                        "Duplicate cleared cheque",
	                        skippedCheques
	                );
	                continue;
	            }
	            // ================= EMI MAPPING =================
	            LocalDate emiDate = "Rejected".equalsIgnoreCase(status)
	                    ? postingDate
	                    : (clearanceDate != null ? clearanceDate : postingDate);

	            LmsRepayment repayment = repaymentRepository
	                    .findFirstByDisbursmentAndDueDateBetween(
	                            disbursment,
	                            emiDate.withDayOfMonth(1),
	                            emiDate.withDayOfMonth(emiDate.lengthOfMonth())
	                    )
	                    .orElse(null);
	            // ================= SAVE CHECK DEPOSIT =================
	            CheckDeposit deposit = new CheckDeposit();
	            deposit.setChequeNumber(chequeNumber);
	            deposit.setPostingDate(postingDate);
	            deposit.setChequeClearanceDate(clearanceDate);
	            deposit.setAmount(amount);
	            deposit.setReason(reason);
	            deposit.setStatus(status);
	            deposit.setDisbursment(disbursment);
	            deposit.setLmsRepayment(repayment);
	            CheckDeposit saved = depositRepository.save(deposit);
	            depositRepository.flush();
	            // ================= ₹500 BOUNCE =================
	            if ("Rejected".equalsIgnoreCase(status)) {
	                updateStatus(
	                        saved.getId(),
	                        "Rejected",
	                        reason,
	                        clearanceDate,
	                        uploaderId
	                );
	            }
	            // ================= LOW CHEQUE APPROVAL =================
	            LowCheckDepositApproval low = new LowCheckDepositApproval();
	            low.setChequeNumber(chequeNumber);
	            low.setPostedDate(postingDate);
	            low.setRequestedAmount(amount);
	            low.setStatus("Accepted");
	            low.setApproved(true);
	            low.setRemarks("Excel Upload");
	            low.setDisbursment(disbursment);
	            low.setLmsRepayment(repayment);
	            if ("Cleared".equalsIgnoreCase(status) && repayment != null && amount != null) {
	                low.setReceivableAmount(Math.min(amount, repayment.getDueAmount()));
	            } else {
	                low.setReceivableAmount(0.0);
	            }
	            if (uploaderId != null) {
	                low.setRequestById(uploaderId);
	                low.setApprovedById(uploaderId);
	            }
	            lowCheckDepositApprovalRespository.save(low);
	        }
	    } catch (Exception e) {
	        logger.error("Excel processing failed", e);
	        throw new RuntimeException("Failed to process uploaded Excel", e);
	    }
	    logger.info("Skipped cheque count = {}", skippedCheques.size());
	    return skippedCheques;
	}
    // ---------------- Helper methods ----------------

    private String getStringCell(Cell cell) {
        if (cell == null) return null;
        CellType type = cell.getCellType();
        if (type == CellType.STRING) {
            String v = cell.getStringCellValue();
            return (v != null) ? v.trim() : null;
        } else if (type == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) return null;
            double d = cell.getNumericCellValue();
            long l = (long) d;
            if (Double.compare(d, (double) l) == 0) {
                return String.valueOf(l);
            } else {
                return String.valueOf(d);
            }
        } else if (type == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (type == CellType.FORMULA) {
            try {
                return cell.getStringCellValue();
            } catch (IllegalStateException ex) {
                try {
                    double d = cell.getNumericCellValue();
                    long l = (long) d;
                    return String.valueOf(l);
                } catch (IllegalStateException ex2) {
                    return null;
                }
            }
        }
        return null;
    }
    
    private LocalDate getLocalDateCell(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            }
            String text = getStringCell(cell);
            if (text == null || text.isBlank()) return null;
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            return LocalDate.parse(text.trim(), fmt);
        } catch (Exception e) {
            logger.warn("Failed to parse date from cell: {}", cell, e);
            return null;
        }
    }
    
    private Double getDoubleCell(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getNumericCellValue();
            }
            String text = getStringCell(cell);
            if (text == null || text.isBlank()) return null;
            return Double.valueOf(text.replace(",", ""));
        } catch (Exception e) {
            logger.warn("Failed to parse numeric cell: {}", e.getMessage());
            return null;
        }
    }

    private String formatChequeNumber(String chequeNo) {
        if (chequeNo == null) return null;
        chequeNo = chequeNo.trim();
        if (chequeNo.matches("\\d+")) {
            try {
                return String.format("%06d", Long.parseLong(chequeNo));
            } catch (NumberFormatException e) {
                return chequeNo;
            }
        }
        return chequeNo;
    }
    
    private void saveSkipped(int slNo,String loanAccountNo,String chequeNumber,LocalDate postingDate,LocalDate clearanceDate,Double amount,String status,String reason,String skipReason,List<SkippedChequeResponseDTO> responseList) {

        // ---------- SAVE IN DB ----------
    	SkippedCheckDeposit entity = new SkippedCheckDeposit();
        entity.setRowNo(slNo+1);
        entity.setLoanAccountNumber(loanAccountNo);
        entity.setChequeNumber(chequeNumber);
        entity.setPostingDate(postingDate);
        entity.setChequeClearanceDate(clearanceDate);
        entity.setAmount(amount);
        entity.setStatus(status);
        entity.setReason(reason);
        entity.setSkipReason(skipReason);
        skippedCheckDepositRepository.save(entity);
        // ---------- ADD TO RESPONSE ----------
        SkippedChequeResponseDTO dto = new SkippedChequeResponseDTO();
        dto.setRowNo(slNo+1);
        dto.setLoanAccountNumber(loanAccountNo);
        dto.setChequeNumber(chequeNumber);
        dto.setPostingDate(postingDate);
        dto.setChequeClearanceDate(clearanceDate);
        dto.setAmount(amount);
        dto.setStatus(status);
        dto.setReason(reason);
        dto.setSkipReason(skipReason);

        responseList.add(dto);
    }

}