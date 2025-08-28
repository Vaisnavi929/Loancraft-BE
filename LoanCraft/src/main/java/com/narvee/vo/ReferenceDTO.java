package com.narvee.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReferenceDTO {
    private Long referenceId;
    private String referenceName;
    private String mobileNumber;
    private String referenceFeedBack;
    private String referenceStatus;
}
