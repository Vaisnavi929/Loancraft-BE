package com.narvee.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.Service.PenaltyService;
import com.narvee.entity.Disbursment;
import com.narvee.entity.LmsPayment;
import com.narvee.entity.Penalty;
import com.narvee.repository.PaymentRepository;
import com.narvee.repository.PenaltyRepository;
import com.narvee.request.dto.PenaltyDto;

@Service
public class PenaltyServiceImpl implements PenaltyService{

	private final static Logger logger=LoggerFactory.getLogger(PenaltyServiceImpl.class);
	
	@Autowired
	private PenaltyRepository penaltyRepository;
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	ModelMapper mapper;
	
	@Override
	public Penalty savePenalty(PenaltyDto penaltyDto) {
		logger.info("!!! inside class : PenaltyServiceImpl,method : savePenalty");

		Penalty  penalty= mapper.map(penaltyDto, Penalty.class);
		LmsPayment lmsPayment = paymentRepository.findById(penaltyDto.getPaymentId())
				.orElseThrow(() -> new RuntimeException("Loan Application not found"));
		penalty.setLmsPayment(lmsPayment);
			return penaltyRepository.save(penalty);
	}

	@Override
	public Penalty updatePenalty(PenaltyDto penaltyDto) {
		logger.info("!!! inside class : PenaltyServiceImpl,method : updatePenalty");
		Penalty  penalty= mapper.map(penaltyDto, Penalty.class);
		LmsPayment lmsPayment = paymentRepository.findById(penaltyDto.getPaymentId())
				.orElseThrow(() -> new RuntimeException("Loan Application not found"));
		penalty.setLmsPayment(lmsPayment);
			return penaltyRepository.save(penalty);
	}

	@Override
	public Penalty getByPenaltyId(Long penaltyId) {
		logger.info("!!! inside class : PenaltyServiceImpl,method : getByPenaltyId");
		return penaltyRepository.findById(penaltyId).get();
	}

	@Override
	public List<Penalty> getAllPenalty() {
		logger.info("!!! inside class : PenaltyServiceImpl,method : getAllPenalty");
		return penaltyRepository.findAll();
	}

	@Override
	public Boolean delateById(Long penaltyId) {
		logger.info("!!! inside class : PenaltyServiceImpl,method : getAllPenalty");
		penaltyRepository.deleteById(penaltyId);
		return true;
	}
}
