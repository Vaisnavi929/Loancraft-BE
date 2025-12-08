package com.narvee.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanStatusUpdateRequest {
    private String status;
    //Abhinay
    @NotBlank(message = "Remarks cannot be empty or just spaces")
    //----
    private String remarks;
    private Long loanId;
    private Long cbilScore;
    
    
	
}
