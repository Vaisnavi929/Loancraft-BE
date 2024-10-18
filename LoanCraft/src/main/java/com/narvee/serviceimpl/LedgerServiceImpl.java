package com.narvee.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.Service.LedgerService;
import com.narvee.entity.Ledger;
import com.narvee.entity.Payment;
import com.narvee.repository.LedgerRepository;
import com.narvee.request.dto.LedgerDTO;

@Service
public class LedgerServiceImpl implements LedgerService{
	
	public static final Logger logger=LoggerFactory.getLogger(LedgerServiceImpl.class); 
	
	@Autowired
	private LedgerRepository ledgerRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Ledger saveLedger(LedgerDTO ledgerDTO) {
	logger.info("!!! inside class : LedgerServiceImpl,method : saveLedger");
	Ledger ledger=modelMapper.map(ledgerDTO, Ledger.class);
	Payment payment=new Payment();
	payment.setPayamentId(ledgerDTO.getPayamentId());
	ledger.setPayment(payment);
	return ledgerRepository.save(ledger);
	}

	@Override
	public Ledger updateLedger(Ledger ledger) {
		logger.info("!!! inside class: LedgerServiceImpl, method : updateLedger");
		return ledgerRepository.save(ledger);
	}

	@Override
	public Ledger getById(Long ledgerId) {
		logger.info("!!! inside class : LedgerServiceImpl, method : getById");
		return ledgerRepository.findById(ledgerId).get();
	}

	@Override
	public List<Ledger> getAll() {
	logger.info("!!! inside class : LedgerServiceImpl, method : getAll");
	return ledgerRepository.findAll();
	}

	@Override
	public Boolean deleteById(Long ledgerId) {
	logger.info("!!! inside class : LedgerServiceImpl, method : getAll");
		ledgerRepository.deleteById(ledgerId);
		return true;
	}

}
