package com.narvee.request.dto;

import java.time.LocalDate;
import java.util.List;

import com.narvee.entity.ReferenceInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class callRequestDTO {
	
	private Long cctId;
	   private String customerName;
	    private String agentName;
	    private LocalDate date;
	    private String status;   // "ANSWERED" or "NOT_ANSWERED"
	    private String feedBack;
		private Long loanAccountNumber;
		
	  private List<ReferenceRequestDto> referenceInfoDto;
	
	
}
