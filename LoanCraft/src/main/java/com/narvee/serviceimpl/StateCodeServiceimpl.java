package com.narvee.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.Service.StateCodeService;
import com.narvee.entity.CurrentAddress;
import com.narvee.entity.CustomersInfo;
import com.narvee.entity.States;
import com.narvee.repository.StateCodeRepository;
import com.narvee.request.dto.StateCodeDTO;

@Service
public class StateCodeServiceimpl implements StateCodeService{
	
	@Autowired
	private StateCodeRepository stateCodeRepository;
	@Autowired
	ModelMapper mapper;
	@Override
	public States saveStatecode(StateCodeDTO stateCodeDTO) {	
		States stateCode=mapper.map(stateCodeDTO,States.class);
		return stateCodeRepository.save(stateCode);
	}

	@Override
	public States updateStatedCode(States stateCode) {
	
		return stateCodeRepository.save(stateCode);
	}

	@Override
	public States getById(Long stateId) {		
		return stateCodeRepository.findById(stateId).get();
	}

	@Override
	public List<States> getAll() {
		return stateCodeRepository.findAll();
	}

	@Override
	public boolean deltedById(Long stateId) {
		stateCodeRepository.deleteById(stateId);
		return false;
	}

}
