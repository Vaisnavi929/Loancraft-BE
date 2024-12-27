package com.narvee.Service;

import java.util.List;


import com.narvee.entity.States;
import com.narvee.request.dto.StateCodeDTO;

public interface StateCodeService {
	
	public States saveStatecode(StateCodeDTO stateCode);
	public States updateStatedCode(States stateCode);
    public States getById(Long stateId);
    public List<States> getAll();
    
    public boolean deltedById(Long stateId);
}
