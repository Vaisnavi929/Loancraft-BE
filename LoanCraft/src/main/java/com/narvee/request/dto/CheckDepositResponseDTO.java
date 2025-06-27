
package com.narvee.request.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface CheckDepositResponseDTO {
public String getId();
public String getChequeNumber();
public LocalDate getPostingDate();
public Double getAmount();
public String getReason(); 
public String getFullName();
public LocalDateTime getCreateddate();
public LocalDateTime getUpdateddate();
public String getRemarks();
public Double getRecievableAmount();
public String getStatus();
public Long getDisbursmentId();
public Long getRepaymentId();
public String getApplicationReferenceId();




}



