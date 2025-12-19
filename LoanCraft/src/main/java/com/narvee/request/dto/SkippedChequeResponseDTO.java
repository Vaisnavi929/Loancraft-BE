package com.narvee.request.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkippedChequeResponseDTO {
	
	private Integer rowNo;
    private String loanAccountNumber;
    private String chequeNumber;
    private LocalDate postingDate;
    private LocalDate chequeClearanceDate;
    private Double amount;
    private String status;
    private String reason;
    private String skipReason;
}
