package com.narvee.request.dto;

import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisbursmentDTO {
	
	private Long disbursmentId;

    private LocalDate date;

    private String accountNo;
    
    private String isfc;

    private Double disbursedAmount;

    private String remarks;

    private String branch;
    
    private Long loanSactionId;

}
