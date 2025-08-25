package com.narvee.vo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Parent DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CctResponseDTO {
    private Long cctId;
    private String customerName;
    private LocalDate date;
    private String agentName;
    private String feedBack;
    private Long loanAccountNumber;
    private String status;
    private List<ReferenceDTO> references = new ArrayList<>();
}

// Child DTO

