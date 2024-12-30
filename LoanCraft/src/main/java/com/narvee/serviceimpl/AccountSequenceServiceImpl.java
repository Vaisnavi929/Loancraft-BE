package com.narvee.serviceimpl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.Service.AccountSequenceService;
import com.narvee.entity.AccountSequence;
import com.narvee.repository.AccountSequenceRepository;

@Service
public class AccountSequenceServiceImpl implements AccountSequenceService {

	private static final Logger logger = LoggerFactory.getLogger(AccountSequenceServiceImpl.class);

	@Autowired
	private AccountSequenceRepository accountSequenceRepository;

	public synchronized String generateAccountNumber() {

		String currentYearMonth = new SimpleDateFormat("yyyyMM").format(new Date());

		AccountSequence accountSequence = accountSequenceRepository.findByYear(currentYearMonth).orElseGet(() -> {
			AccountSequence newSequence = new AccountSequence();
			newSequence.setYear(currentYearMonth);
			newSequence.setLastSequence(0);
			return newSequence;
		});

		accountSequence.setLastSequence(accountSequence.getLastSequence() + 1);

		accountSequenceRepository.save(accountSequence);

		String sequenceNumber = String.format("%04d", accountSequence.getLastSequence());
		return currentYearMonth + sequenceNumber;
	}
}
