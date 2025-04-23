package com.narvee.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationTrackResponseDTO {

	private boolean reviewed;
	private boolean cibil;
	private boolean physicalLocationVerified;
	private boolean sanction;
	private boolean disbursed;
	private Boolean documentsVerified;

}
