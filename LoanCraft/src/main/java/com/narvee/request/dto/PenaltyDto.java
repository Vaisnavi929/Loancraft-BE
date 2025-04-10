package com.narvee.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PenaltyDto {
	private Long penaltyId;
	private Double penalty;
	private String remarks; 
	private Long paymentId;


}
