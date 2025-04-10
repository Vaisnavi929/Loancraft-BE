package com.narvee.Service;

import java.util.List;

import com.narvee.entity.Penalty;
import com.narvee.request.dto.PenaltyDto;

public interface PenaltyService {

	public Penalty savePenalty(PenaltyDto penaltyDto);
	
	public Penalty updatePenalty(PenaltyDto penaltyDto);
	
	public Penalty  getByPenaltyId(Long penaltyId);
	
	public List<Penalty> getAllPenalty();
	
	public Boolean delateById(Long penaltyId);
}
